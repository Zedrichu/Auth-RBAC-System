package util;

public class InvalidAccessException extends RuntimeException {
    public InvalidAccessException(String error) {
        super(error);
    }
}
