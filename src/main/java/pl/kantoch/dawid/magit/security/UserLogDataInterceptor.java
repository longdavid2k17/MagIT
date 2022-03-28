package pl.kantoch.dawid.magit.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Component
public class UserLogDataInterceptor implements HandlerInterceptor
{
    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;

    public UserLogDataInterceptor(UserRepository userRepository, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {

        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            String token = authorizationHeaderValue.substring(7);
            String username = jwtUtils.getUsernameFromJwtToken(token);
            Optional<User> userOptional = userRepository.findByUsername(username);
            if(userOptional.isPresent())
            {
                User user = userOptional.get();
                user.setLastLogged(new Date());
                userRepository.save(user);
            }
        }
    }
}
