package lang;

public class RecoverableErrorException extends FatalErrorException {
    public RecoverableErrorException(String message) {
        super(message);
    }
}
