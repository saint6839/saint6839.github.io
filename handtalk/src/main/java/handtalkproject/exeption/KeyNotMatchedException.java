package handtalkproject.exeption;

public class KeyNotMatchedException extends Exception {
    private final String MESSAGE = "인증키가 일치하지 않습니다";

    public KeyNotMatchedException() {
        System.out.println(MESSAGE);
    }
}
