package compiladorl3.semantic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import compiladorl3.lexical.Token;

public class Semantic {
	private int state; // 1 - declaration, 2 - operation
	private SOperationChecker operationChecker;
	private SDeclarationChecker currentSDeclarationChecker;
	private List<SDeclarationChecker> declarationCheckers;
	private Token[] last3TokensIsDeclarationOrOperation;
	private Stack<Scope> scopes;
	private Scope currentScope;

	public Semantic() {
		this.scopes =  new Stack<Scope>();
		this.last3TokensIsDeclarationOrOperation = new Token[3];
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
			this.last3TokensIsDeclarationOrOperation[2] = token;
			this.currentSDeclarationChecker.isPartOfADeclaration(token);
			return;
		} else if (isAnIdentifier(token) && !theDecisionMakingArrayIsFull() || isAnReassignment()) {
			Variable variableAlreadyDeclared = null;

			if (last3TokensIsDeclarationOrOperation[0] != null) {
				for (Scope currentScope : scopes) {
					variableAlreadyDeclared = currentScope.getVariable(last3TokensIsDeclarationOrOperation[0].getLexeme());
				}
				if (variableAlreadyDeclared != null ) {
					this.state = 1;
					executeStateOnlast2Tokens();
				}
				executeCurrentState(token);

			}else{
				addTokensForDecisionMaking(token);
			}

			return;
		} else if (!theDecisionMakingArrayIsFull() && this.last3TokensIsDeclarationOrOperation[1] ==null && currentSDeclarationChecker.getCurrentVariableDeclaration().size() < 3) {
			addTokensForDecisionMaking(token);
			
			if(token.getLexeme().equals(";") && this.currentScope.getVariable(last3TokensIsDeclarationOrOperation[0].getLexeme()) != null ) {
				this.state = 1;
				executeStateOnlast2Tokens();
				return;
			} else if(last3TokensIsDeclarationOrOperation[0] !=  null && last3TokensIsDeclarationOrOperation[1] !=  null && last3TokensIsDeclarationOrOperation[2] !=  null) {
				this.state = 1;
				executeStateOnlast2Tokens();
				clearDecisionMakingArray();
			} else if(last3TokensIsDeclarationOrOperation[1] != null && last3TokensIsDeclarationOrOperation[2] != null && last3TokensIsDeclarationOrOperation[1].getLexeme().equals(";") && this.currentScope.getVariable(token.getLexeme()) == null&& tokenIsAnUninitializedVariable(token) == false && this.last3TokensIsDeclarationOrOperation[2] == null || this.currentScope.getVariable(last3TokensIsDeclarationOrOperation[0].getLexeme()) == null && tokenIsAnUninitializedVariable(token) && token.getLexeme().equals("=") ) {
				throw new Exception("Variable does not exist '" + last3TokensIsDeclarationOrOperation[0].getLexeme() + "'");
			} 
			return;
		}
		
		 else if (this.state == 0) {
			checkIfIsDeclarationOrOparation(token);
			return;
		} 



		executeCurrentState(token);
	}

	private boolean isAnReassignment() {
		if (last3TokensIsDeclarationOrOperation[1] == null || last3TokensIsDeclarationOrOperation[0] == null) {
			return false;
		}
		return last3TokensIsDeclarationOrOperation[0].getType() == 3 && last3TokensIsDeclarationOrOperation[1].getType() == 8 && last3TokensIsDeclarationOrOperation[2] == null;
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
			String name = sDeclarationChecker.getCurrentDeclarationName();
			if (name.equals(currentToken.getLexeme()) && currentSDeclarationChecker.getSemanticSubject().last3TokensIsDeclarationOrOperation[2].getType() < 3) {
				this.currentSDeclarationChecker = sDeclarationChecker;
				return true;
			}
		}
		return false;
	}

	public void addTokensForDecisionMaking(Token currentToken) {

		if (last3TokensIsDeclarationOrOperation[0] == null) {
			last3TokensIsDeclarationOrOperation[0] = currentToken;
		} else {
			last3TokensIsDeclarationOrOperation[1] = currentToken;
		}
	}

	public boolean theDecisionMakingArrayIsFull() {
		return !(last3TokensIsDeclarationOrOperation[1] == null);
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
		if(last3TokensIsDeclarationOrOperation[1] != null) {
			return last3TokensIsDeclarationOrOperation[1].getType() == 5 || last3TokensIsDeclarationOrOperation[1].getType() == 4 ;
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
		this.last3TokensIsDeclarationOrOperation = new Token[3];
	}
	
	public void executeStateOnlast2Tokens() throws Exception {
		
		if(last3TokensIsDeclarationOrOperation[0] != null) {
			executeCurrentState(last3TokensIsDeclarationOrOperation[0]);
		}
		if(last3TokensIsDeclarationOrOperation[1] != null){
			executeCurrentState(last3TokensIsDeclarationOrOperation[1]);
		}
	}
}