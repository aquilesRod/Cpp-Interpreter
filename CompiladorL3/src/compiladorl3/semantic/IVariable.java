package compiladorl3.semantic;

public interface IVariable {
    public IType getType();
    public void setType(IType type);
    public String getName();
    public void setName(String name);
    public String getValue();
    public void setValue(String value);
}
