package semantic;

import java.util.ArrayList;
import java.util.List;

import compiladorl3.Token;

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
		if (currentToken.getLexema().equals(getCurrentDeclarationName())) {
			addPartOfVariableDeclaration(currentToken);
			return true;
		}
		return false;
	}
	
	public void addPartOfVariableDeclaration(Token currentPart) throws Exception {

		if (isVariableType(currentPart) && currentVariableDeclaration.size() == 0 ) {
			currentVariableDeclaration.add(currentPart.getLexema());
		} else if (hasTheVarTypeAlreadyBeenEntered() && isAValidIdentifier(currentPart) && currentVariableDeclaration.size() == 1) {
			currentVariableDeclaration.add(currentPart.getLexema());
		} else if (hasTheVarIdentifierAlreadyBeenEntered() && isADeclarationTerminator(currentPart)  && currentVariableDeclaration.size() == 2) {
			setNotInitializedStatusToDeclaration();
		} else if (hasTheVarIdentifierAlreadyBeenEntered() && isAnAssignment(currentPart)  && currentVariableDeclaration.size() == 2) {
			currentVariableDeclaration.add(currentPart.getLexema());
		} else if (hasTheAssignmentAlreadyBeenEntered() && checkIfTheValueTypeMatchesTheVarType(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexema());
		} else if(isAVariableToCombine(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexema());
		} else if (isAnArithmeticOperator(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexema());
		} else if (hasTheValueAlreadyBeenEntered() && isADeclarationTerminator(currentPart)) {
			currentVariableDeclaration.add(currentPart.getLexema());
			setInitializedStatusToDeclaration();
		} else {
			throw new Exception("Finish the variable declaration: '" + this.toString() + "' <-");
		}
	}

	private boolean isAVariableToCombine(Token currentPart) throws Exception {
		return isAnArithmeticOperator(this.getCurrentVariableDeclaration().get(this.getCurrentVariableDeclaration().size() -1).charAt(0)) && isAnIdentifier(currentPart) && checkIfTheValueTypeMatchesTheVarType(currentPart);
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

		if(currentPart.getTipo() == 3) {
			Variable compositionVar = currentScope.getVariable(currentPart.getLexema());

			if (compositionVar == null) {
				return false;
			}
			return compositionVar.getType().getTypeNumber() == Type
					.checkIfVariableTypeIsValid(currentVariableDeclaration.get(0));
		}
		return false;
	
	}

	public void notifySubject() {
		semanticSubject.updateDeclarationCheckers();
	}

	public void setInitializedStatusToDeclaration() {
		this.currentDeclarationStatus = "EMPTY";
		addVariableInScope();
		notifySubject();
		currentVariableDeclaration = new ArrayList<String>();
	}

	public void setNotInitializedStatusToDeclaration() {
		this.currentDeclarationStatus = "NOT_INITIALIZED";
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
		return currentPart.getTipo() == 0;
	}

	public boolean isAnArithmeticOperator(Token currentPart) {
		return currentPart.getTipo() == 5;
	}
	private boolean isAnArithmeticOperator(char c){
		return c == '+' || c == '-' || c == '*' || c == '/';
	}
	private boolean isValueReal(Token currentPart) {

		return currentPart.getTipo() == 1;
	}

	private boolean isValueChar(Token currentPart) {

		return currentPart.getTipo() == 2;
	}

	private boolean isADeclarationTerminator(Token currentPart) throws Exception {
		return currentPart.getLexema().equals(";");
	}

	private boolean isAnAssignment(Token currentPart) {
		return currentPart.getTipo() == 8;
	}

	private boolean isAValidIdentifier(Token currentPart) throws Exception {
		if(this.currentScope.getVariable(currentPart.getLexema()) != null) {
			throw new Exception(
					"Variable already exists: '" + currentPart.getLexema() + "' <-");
		}
		return currentPart.getTipo() == 3;
	}
	
	private boolean isAnIdentifier(Token currentPart) throws Exception {
		return currentPart.getTipo() == 3;
	}

	private boolean isVariableType(Token currentPart) {
		return this.currentVariableDeclaration.size() == 0 && Type.checkIfVariableTypeIsValid(currentPart) != -1;
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
