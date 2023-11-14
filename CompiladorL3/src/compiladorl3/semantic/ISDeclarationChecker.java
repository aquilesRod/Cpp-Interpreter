package compiladorl3.semantic;

import java.util.List;

import compiladorl3.Token;

public interface ISDeclarationChecker {
    public List<String> getCurrentVariableDeclaration();
    public void openNewScope();
    public void notifySubject();
    public void setInitializedStatusToDeclaration();
    public void setNotInitializedStatusToDeclaration();
    public void isPartOfADeclaration(Token currentToken) throws Exception;
    public boolean equalsToCurrentDeclarationName(Token currentToken) throws Exception;
    public boolean operationPartOfVariableDeclaration(Token currentPart);
    public void addPartOfVariableDeclaration(Token currentPart) throws Exception;
    public String getCurrentDeclarationStatus();
    public String getCurrentDeclarationName();
}
