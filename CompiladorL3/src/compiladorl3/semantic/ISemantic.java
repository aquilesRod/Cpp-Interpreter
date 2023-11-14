package compiladorl3.semantic;
import java.util.List;

import compiladorl3.Token;

public interface ISemantic {
    public List<ISDeclarationChecker> getDeclarationCheckers();
    public void setDeclarationCheckers(List<ISDeclarationChecker> declarationCheckers);
    public void CurrentTokenIsUninitializedVar(Token currentToken);
    public void addTokensForDecisionMaking(Token currentToken);
    public boolean TokensForDecisionMakingArrayIsFull();
    public ISDeclarationChecker getCurrentSDeclarationChecker();
    public void setCurrentSDeclarationChecker(ISDeclarationChecker currentSDeclarationChecker);
    public void checkIfIsDeclarationOrOparation(Token token) throws Exception;
    public void updateDeclarationCheckers();
    public void updateOperationChecker();
    public void runState(Token token) throws Exception;
}
