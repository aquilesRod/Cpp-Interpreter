package compiladorl3.semantic;
public interface IScope {
    public boolean addDeclaredVariable(String variableName, IType type, String value);
    public boolean bothBelongToTheSameScope(IVariable firstVariable, IVariable secondVariable);
    public IVariable getVariable(IVariable firstVariable);
    public IVariable getVariable(String name);
}
