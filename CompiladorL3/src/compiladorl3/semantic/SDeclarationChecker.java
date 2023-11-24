package compiladorl3.semantic;

import java.util.ArrayList;
import java.util.List;

import compiladorl3.lexical.Token;

public class SDeclarationChecker  {
	private Scope currentScope;
	private Semantic semanticSubject;
	private String currentDeclarationStatus;
	private List<String> currentVariableDeclaration;


	public SDeclarationChecker(Scope currentScope, Semantic subject) {
		this.currentScope = currentScope;
		this.semanticSubject = subject;
		this.currentVariableDeclaration = new ArrayList<String>(5);
	}

	public void isPartOfADeclaration(Token currentToken) throws Exception {
		
		addPartOfVariableDeclaration(currentToken);
	}

	public boolean checkIF(Token currentToken) throws Exception {
		if (currentToken.getLexeme().equals(getCurrentDeclarationName())) {
			addPartOfVariableDeclaration(currentToken);
			return true;
		}
		return false;
	}
	
	public void addPartOfVariableDeclaration(Token currentPart) throws Exception {

		if (isVariableType(currentPart) && currentVariableDeclaration.size() == 0 ) {
			currentVariableDeclaration.add(currentPart.getLexeme());
		} else if (hasTheVarTypeAlreadyBeenEntered() && isAValidIdentifier(currentPart) && currentVariableDeclaration.size() == 1) {
			currentVariableDeclaration.add(currentPart.getLexeme());
		} else if (hasTheVarIdentifierAlreadyBeenEntered() && isADeclarationTerminator(currentPart)  && currentVariableDeclaration.size() == 2) {
			setNotInitializedStatusToDeclaration();
		} else if (hasTheVarIdentifierAlreadyBeenEntered() && isAnAssignment(currentPart)  && currentVariableDeclaration.size() == 2) {
			currentVariableDeclaration.add(currentPart.getLexeme());
		} else if (hasTheAssignmentAlreadyBeenEntered() && checkIfTheValueTypeMatchesTheVarType(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexeme());
		} else if(isAVariableToCombine(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexeme());
		} else if (isAnArithmeticOperator(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexeme());
		} else if (hasTheValueAlreadyBeenEntered() && isADeclarationTerminator(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexeme());
			setInitializedStatusToDeclaration();
		}else if (isAnReassignment(currentPart)) {
			currentDeclarationStatus = "reassignment";
		} else if(isAValidIdentifier(currentPart) && this.currentScope.getVariable(currentPart.getLexeme()) == null && !currentDeclarationStatus.equals("reassignment")){
			throw new Exception("Variable does not exist '" + currentPart.getLexeme() + "' <-");
		}
	}

	private boolean isAVariableToCombine(Token currentPart) throws Exception {
		int lastIndex = this.getCurrentVariableDeclaration().size() -1;

		if (lastIndex >= 0) {
			String lastChar = this.getCurrentVariableDeclaration().get(lastIndex);
			return isAnArithmeticOperator(lastChar.charAt(0)) && isAnIdentifier(currentPart) && checkIfTheValueTypeMatchesTheVarType(currentPart);
		}
		return false;
	}

	private boolean checkIfTheValueTypeMatchesTheVarType(Token currentPart) throws Exception {
		if (getcurrentVariableDeclarationType().equals("int") && isValueInt(currentPart)) {
			return true;
		} else if (getcurrentVariableDeclarationType().equals("float") && isValueReal(currentPart)) {
			return true;
		} else if (getcurrentVariableDeclarationType().equals("char") && isValueChar(currentPart)) {
			return true;
		} else if (checkOperationsBetweenVariables(currentPart)) {
			return true;
		} else if(isAnArithmeticOperator(currentPart)){
			return false;
		} else if(isADeclarationTerminator(currentPart)) {
			return false;
		}

		throw new Exception(
				"Type not matched with value in the declaration: '" + this.toString() + "' <-");
	}

	private void addVariableInScope() {
		Type type = new Type(Type.checkIfVariableTypeIsValid(getcurrentVariableDeclarationType()));
		String name = getCurrentDeclarationName();
		String value = getcurrentVariableDeclarationValue();

		this.currentScope.addDeclaredVariable(name, type, value);
	}

	private boolean checkOperationsBetweenVariables(Token currentPart) {

		if(currentPart.getType() == 3) {
			Variable compositionVar = currentScope.getVariable(currentPart.getLexeme());

			if (compositionVar == null) {
				return false;
			}
			currentPart.setLexeme(compositionVar.getValue());
			currentPart.setType(compositionVar.getType().getTypeNumber());
			return compositionVar.getType().getTypeNumber() == Type
					.checkIfVariableTypeIsValid(currentVariableDeclaration.get(0));
		}
		return false;
	
	}

	public void notifySubject() {
		semanticSubject.updateDeclarationCheckers();
	}

	public boolean isAnReassignment(Token token) {
		Variable variable = currentScope.getVariable(token.getLexeme());
		boolean isAnReassignment = this.currentVariableDeclaration.size() == 0 && variable != null;
		currentVariableDeclaration.add(variable.getType().getString());		
		currentVariableDeclaration.add(token.getLexeme());
		currentScope.removeDeclaredVariable(token.getLexeme());
		return isAnReassignment;
	}

	public void setInitializedStatusToDeclaration() {
		this.currentDeclarationStatus = "EMPTY";
		addVariableInScope();
		notifySubject();
		currentVariableDeclaration = new ArrayList<String>();
	}

	public void setNotInitializedStatusToDeclaration() {
		this.currentDeclarationStatus = "NOT_INITIALIZED";
		Type currentType = new Type(Type.checkIfVariableTypeIsValid(currentVariableDeclaration.get(0)));
		this.currentScope.unDeclaredVariable(currentVariableDeclaration.get(1), currentType, null);
		notifySubject();
	}

	public String getCurrentDeclarationStatus() {
		return currentDeclarationStatus;
	}

	private String getcurrentVariableDeclarationType() {
		return this.currentVariableDeclaration.get(0);
	}

	private String getcurrentVariableDeclarationValue() {
		return this.currentVariableDeclaration.get(3);
	}

	public String getCurrentDeclarationName() {
		
		if(this.currentVariableDeclaration.size() <= 1)
			return " ";
		return this.currentVariableDeclaration.get(1);
	}

	public List<String> getCurrentVariableDeclaration() {
		return currentVariableDeclaration;
	}

	private boolean hasTheValueAlreadyBeenEntered() {
		return this.currentVariableDeclaration.size() == 4 || this.currentVariableDeclaration.size() > 4 ;
	}

	private boolean hasTheAssignmentAlreadyBeenEntered() {
		return this.currentVariableDeclaration.size() == 3 || this.currentVariableDeclaration.size() > 4 ;
	}

	private boolean hasTheVarIdentifierAlreadyBeenEntered() {
		return this.currentVariableDeclaration.size() == 2;
	}

	private boolean hasTheVarTypeAlreadyBeenEntered() {
		return this.currentVariableDeclaration.size() == 1;
	}

	private boolean isValueInt(Token currentPart) {
		return currentPart.getType() == 0;
	}

	public boolean isAnArithmeticOperator(Token currentPart) {
		return currentPart.getType() == 5;
	}
	private boolean isAnArithmeticOperator(char c){
		return c == '+' || c == '-' || c == '*' || c == '/';
	}
	private boolean isValueReal(Token currentPart) {

		return currentPart.getType() == 1;
	}

	private boolean isValueChar(Token currentPart) {

		return currentPart.getType() == 2;
	}

	private boolean isADeclarationTerminator(Token currentPart) throws Exception {
		return currentPart.getLexeme().equals(";");
	}

	private boolean isAnAssignment(Token currentPart) {
		return currentPart.getType() == 8;
	}

	private boolean isAValidIdentifier(Token currentPart) throws Exception {
		if(this.currentScope.getVariable(currentPart.getLexeme()) != null) {
			throw new Exception(
					"Variable already exists: '" + currentPart.getLexeme() + "' <-");
		}
		return currentPart.getType() == 3;
	}
	
	private boolean isAnIdentifier(Token currentPart) throws Exception {
		return currentPart.getType() == 3;
	}

	private boolean isVariableType(Token currentPart) {
		return this.currentVariableDeclaration.size() == 0 && Type.checkIfVariableTypeIsValid(currentPart) != -1;
	}

	public Semantic getSemanticSubject() {
		return semanticSubject;
	}
	
	@Override
	public String toString() {
		String toString = "";
		for (String currentPart : currentVariableDeclaration) {
			toString += currentPart + " ";
		}
		return toString;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SDeclarationChecker) {
			SDeclarationChecker semantic = (SDeclarationChecker) obj;
			return semantic.getCurrentVariableDeclaration().get(1).equals(this.getCurrentVariableDeclaration().get(1));
		}
		return false;
	}
}