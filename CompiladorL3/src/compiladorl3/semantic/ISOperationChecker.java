package compiladorl3.semantic;
import compiladorl3.lexical.Token;

public interface ISOperationChecker {
    public void currentTokenIsValidForOperation(Token currentToken) throws Exception;
}
