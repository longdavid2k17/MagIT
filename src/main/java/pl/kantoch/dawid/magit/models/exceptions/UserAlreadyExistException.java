package pl.kantoch.dawid.magit.models.exceptions;

public class UserAlreadyExistException extends Exception{
    public UserAlreadyExistException(String errorMessage) {
        super(errorMessage);
    }
}
