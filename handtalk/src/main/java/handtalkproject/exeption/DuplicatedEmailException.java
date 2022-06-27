package handtalkproject.exeption;

public class DuplicatedEmailException extends RuntimeException{
    public DuplicatedEmailException(String message) {
        super(message);
    }
}
