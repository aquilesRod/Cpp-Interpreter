package semantic;

import java.util.ArrayList;
import java.util.List;

public class Scope {
	private List<Variable> scope = new ArrayList<Variable>();

	public boolean addDeclaredVariable(String variableName, Type type, String value) {
		Variable newVar = new Variable(variableName, type, value);
		scope.add(newVar);
		return true;
	}

	public boolean bothBelongToTheSameScope(Variable firstVariable, Variable secondVariable) {
		
		if(getVariable(firstVariable) != null || getVariable(secondVariable) != null) {
			return getVariable(firstVariable) == getVariable(secondVariable);
		}
		
		return false;
	}

	public Variable getVariable(Variable firstVariable) {
		if(scope.indexOf(firstVariable) != -1) {
			return null;
		}
		return scope.get(scope.indexOf(firstVariable));
	}
	
	public Variable getVariable(String name) {
		Variable var =  new Variable(name, null, null);
		if(scope.indexOf(var) == -1) {
			return null;
		}
		return scope.get(scope.indexOf(var));
	}

}
