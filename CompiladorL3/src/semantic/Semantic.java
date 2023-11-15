package semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import compiladorl3.lexical.Token;

public class Semantic {
	private int state; // 1 - declaration, 2 - operation
	private SOperationChecker operationChecker;
	private SDeclarationChecker currentSDeclarationChecker;
	private List<SDeclarationChecker> declarationCheckers;
	private Token[] last2TokensIsDeclarationOrOperation;
	private Stack<Scope> scopes;
	private Scope currentScope;

	public Semantic() {
		this.scopes =  new Stack<Scope>();
		this.last2TokensIsDeclarationOrOperation = new Token[2];
		this.declarationCheckers = new ArrayList<SDeclarationChecker>();
	}

	public Scope getCurrentScope() {
		return this.currentScope;
	}
	public void runSemantic(Token token) throws Exception {	
		if(isAnOpenKey(token)) {
			this.currentScope = new Scope();
			this.scopes.push(currentScope);
			this.operationChecker = new SOperationChecker(this);
			this.currentSDeclarationChecker = new SDeclarationChecker(currentScope, this);
			return;
		} else if(isAnCloseKey(token)) {
			if(this.scopes.size() != 0) {
				this.currentScope = this.scopes.lastElement();
				return;
			}
		}
		
		if (isAnVariableType(token)) {
			this.currentSDeclarationChecker.isPartOfADeclaration(token);
			return;
		} else if (isAnIdentifier(token) && !theDecisionMakingArrayIsFull()) {
			addTokensForDecisionMaking(token);
			return;
		} else if (!theDecisionMakingArrayIsFull() && !(token.getLexeme().equals(";"))) {
			addTokensForDecisionMaking(token);
			return;
		} else if (this.state == 0) {
			checkIfIsDeclarationOrOparation(token);
			return;
		} 
		executeCurrentState(token);
	}

	private boolean isAnCloseKey(Token token) {
		return token.getLexeme().equals("}");
	}

	private boolean isAnOpenKey(Token token) {
		return token.getLexeme().equals("{");
	}

	private boolean isAnVariableType(Token token) {
		return token.getLexeme().equals("int")||token.getLexeme().equals("char") || token.getLexeme().equals("float");
	}

	private boolean isAnIdentifier(Token token) {
		return token.getType() == 3;
	}

	public boolean tokenIsAnUninitializedVariable(Token currentToken) {
		for (SDeclarationChecker sDeclarationChecker : declarationCheckers) {
			if (sDeclarationChecker.getCurrentDeclarationName().equals(currentToken.getLexeme())) {
				this.currentSDeclarationChecker = sDeclarationChecker;
				return true;
			}
		}
		return false;
	}

	public void addTokensForDecisionMaking(Token currentToken) {

		if (last2TokensIsDeclarationOrOperation[0] == null) {
			last2TokensIsDeclarationOrOperation[0] = currentToken;
		} else {
			last2TokensIsDeclarationOrOperation[1] = currentToken;
		}
	}

	public boolean theDecisionMakingArrayIsFull() {
		return !(last2TokensIsDeclarationOrOperation[1] == null);
	}

	public void checkIfIsDeclarationOrOparation(Token token) throws Exception {
		if (isOperation()) {
			this.state = 2;
			executeStateOnlast2Tokens();
			executeCurrentState(token);
			return;
		}
		
		this.state = 1;
		executeStateOnlast2Tokens();
		executeCurrentState(token);
	}

	private boolean isOperation() {
		if(last2TokensIsDeclarationOrOperation[1] != null) {
			return last2TokensIsDeclarationOrOperation[1].getType() == 5 || last2TokensIsDeclarationOrOperation[1].getType() == 4 ;
		}
		return false;
	}

	public void executeCurrentState(Token token) throws Exception {
		if (state == 1) {
			this.currentSDeclarationChecker.isPartOfADeclaration(token);
			return;
		}
		this.operationChecker.currentTokenIsValidForOperation(token);
	}

	public void updateDeclarationCheckers() {

		if (this.currentSDeclarationChecker.getCurrentDeclarationStatus().equals("EMPTY")) {
			this.clearDecisionMakingArray();
			this.state = 0;
			return;
		}
		
		declarationCheckers.add(currentSDeclarationChecker);
		SDeclarationChecker newDeclarationChecker = new SDeclarationChecker(currentScope, this);
		declarationCheckers.add(newDeclarationChecker);
		currentSDeclarationChecker = newDeclarationChecker;
		this.state = 0;
		this.clearDecisionMakingArray();

	}

	public void updateOperationChecker() {
		this.state = 0;
		this.clearDecisionMakingArray();
	}

	public List<SDeclarationChecker> getDeclarationCheckers() {
		return declarationCheckers;
	}

	public SDeclarationChecker getCurrentSDeclarationChecker() {
		return currentSDeclarationChecker;
	}

	public void setDeclarationCheckers(List<SDeclarationChecker> declarationCheckers) {
		this.declarationCheckers = declarationCheckers;
	}

	public void setCurrentSDeclarationChecker(SDeclarationChecker currentSDeclarationChecker) {
		this.currentSDeclarationChecker = currentSDeclarationChecker;
	}

	public void clearDecisionMakingArray() {
		this.last2TokensIsDeclarationOrOperation = new Token[2];
	}
	
	public void executeStateOnlast2Tokens() throws Exception {
		
		if(!tokenIsAnUninitializedVariable(last2TokensIsDeclarationOrOperation[0])) {
			executeCurrentState(last2TokensIsDeclarationOrOperation[0]);
		}
		if(last2TokensIsDeclarationOrOperation[1] != null){
			executeCurrentState(last2TokensIsDeclarationOrOperation[1]);
		}

	}

}
