package compiladorl3.semantic;

import compiladorl3.lexical.Token;

public class Type {
		private int type;
		public static final int intType = 0;
	    public static final int realType = 1;
	    public static final int charType = 2;
	    public static final int stringType = 3;
	    
	    public Type(int typeNumber) {
	    	this.type =  typeNumber;
		}
	    
	    public int getTypeNumber() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getString() {
			switch (this.type) {
				case 0:
					return "int";
				case 1:
					return "real";
				case 2:
					return "char";
				case 3:
					return "char";
				default:
					return null;
			}
		}
	   
	    public static int checkIfVariableTypeIsValid(Token token) {
	    	if ("int".equals(token.getLexeme())) {
				return intType;
			} else if ("float".equals(token.getLexeme())) {
				return realType;
			} else if("char".equals(token.getLexeme())) {
				return charType;
			}
	    	return -1;
	    }
		
	    public static int checkIfVariableTypeIsValid(String type) {
	    	if ("int".equals(type)) {
				return intType;
			} else if ("float".equals(type)) {
				return realType;
			} else if("char".equals(type)) {
				return charType;
			}
	    	return -1;
	    }
	    
	    @Override
	    public boolean equals(Object obj) {
	    	if(obj instanceof Type) {
	    		Type otherType = (Type)obj;
	    		return otherType.getTypeNumber() == this.getTypeNumber();
	    	}
	    	return super.equals(obj);
	    }
}

