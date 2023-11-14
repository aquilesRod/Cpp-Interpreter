package compiladorl3.semantic;
import compiladorl3.Token;

public interface ISOperationChecker {
    public void currentTokenIsValidForOperation(Token currentToken) throws Exception;
}
