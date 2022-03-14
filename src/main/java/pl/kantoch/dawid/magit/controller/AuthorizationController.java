package pl.kantoch.dawid.magit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.kantoch.dawid.magit.models.AppConstants;
import pl.kantoch.dawid.magit.models.events.OnRegistrationCompleteEvent;
import pl.kantoch.dawid.magit.models.exceptions.UserAlreadyExistException;
import pl.kantoch.dawid.magit.models.payloads.requests.LoginRequest;
import pl.kantoch.dawid.magit.models.payloads.requests.SetNewPasswordRequest;
import pl.kantoch.dawid.magit.models.payloads.requests.SignupRequest;
import pl.kantoch.dawid.magit.models.payloads.responses.JwtResponse;
import pl.kantoch.dawid.magit.models.payloads.responses.SimpleApiResponse;
import pl.kantoch.dawid.magit.security.JWTUtils;
import pl.kantoch.dawid.magit.security.user.User;
import pl.kantoch.dawid.magit.security.user.UserDetailsImpl;
import pl.kantoch.dawid.magit.security.user.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController
{
    private final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    public AuthorizationController(UserService userService, ApplicationEventPublisher eventPublisher, AuthenticationManager authenticationManager, JWTUtils jwtUtils)
    {
        this.userService = userService;
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest,HttpServletRequest request)
    {
        try
        {
            if(loginRequest!=null)
            {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtils.generateJwtToken(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList());

                return ResponseEntity.ok(new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
            }
            else
                return ResponseEntity.badRequest().body("Żądanie jest niepoprawne!");
        }
        catch (BadCredentialsException e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Błędne dane logowania!");
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUserAccount(@RequestBody SignupRequest signupRequest,
            HttpServletRequest request)
    {
        User registered = null;
        try
        {
            registered = userService.registerNewUserAccount(signupRequest);

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        }
        catch (UserAlreadyExistException uaeEx)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Użytkownik z takimi danymi istnieje!");
        }
        catch (RuntimeException ex)
        {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas rejestrowania użytkownika! Komunikat: "+ ex.getMessage());
        }

        return ResponseEntity.ok().body(registered);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@NotEmpty @RequestBody String email)
    {
        return userService.resetPassword(email);
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<?> setNewPasword(@RequestBody SetNewPasswordRequest request)
    {
        return userService.setNewPasword(request.getToken(), request.getPassword());
    }

    @PostMapping("/token/verify")
    public ResponseEntity<?> confirmRegistration(@NotEmpty @RequestBody String token) {
        final String result = userService.validateVerificationToken(token);
        return ResponseEntity.ok().body(new SimpleApiResponse(true, result));
    }

    @PostMapping("/token/resend")
    @ResponseBody
    public ResponseEntity<?> resendRegistrationToken(@NotEmpty @RequestBody String expiredToken) {
        if (!userService.resendVerificationToken(expiredToken)) {
            return new ResponseEntity<>(new SimpleApiResponse(false, "Nie przekazano wartości tokenu!"), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(new SimpleApiResponse(true, AppConstants.SUCCESS));
    }
}
