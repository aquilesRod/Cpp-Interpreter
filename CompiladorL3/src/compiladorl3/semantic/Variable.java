package compiladorl3.semantic;

public class Variable {
	private String name;
	private String value;
    private Type type;

    public Variable(String name, Type type, String value) {
    	this.name = name;
    	this.type =  type;
    	this.value = value;
    }

    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Variable) {
			Variable var2 = (Variable)obj;
			return var2.getName().equals(this.name);
		}
			
		return false;
	}
}

