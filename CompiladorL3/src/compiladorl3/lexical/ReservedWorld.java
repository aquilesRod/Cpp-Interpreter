package compiladorl3.lexical;

public class ReservedWorld {
	
	// Types
	public static final String RESERVEDWORLD_INT = "int";
	public static final String RESERVEDWORLD_FLOAT = "float";
	public static final String RESERVEDWORLD_CHAR = "char";
	public static final String RESERVEDWORLD_VOID = "void";

	// Flow Control
	public static final String RESERVEDWORLD_IF = "if";
	public static final String RESERVEDWORLD_ELSE = "else";
	public static final String RESERVEDWORLD_DO = "do";
	public static final String RESERVEDWORLD_WHILE = "while";
	public static final String RESERVEDWORLD_FOR = "for";

	// OOP
	public static final String RESERVEDWORLD_CLASS = "class";
	public static final String RESERVEDWORLD_PUBLIC = "public";
	public static final String RESERVEDWORLD_PRIVATE = "private";
	public static final String RESERVEDWORLD_PROTECTED = "protected";

	// Function
	public static final String RESERVEDWORLD_RETURN = "return";
	public static final String RESERVEDWORLD_MAIN = "main";
	
	public static boolean isReservedWorld(String world) {

		return world.equals(RESERVEDWORLD_MAIN) ||
				world.equals(RESERVEDWORLD_IF) || 
				world.equals(RESERVEDWORLD_ELSE)||
				world.equals(RESERVEDWORLD_WHILE) || 
				world.equals(RESERVEDWORLD_DO) || 
				world.equals(RESERVEDWORLD_FOR) || 
				world.equals(RESERVEDWORLD_INT) || 
				world.equals(RESERVEDWORLD_FLOAT) || 
				world.equals(RESERVEDWORLD_CHAR) ||
				world.equals(RESERVEDWORLD_VOID) ||
				world.equals(RESERVEDWORLD_CLASS) ||
				world.equals(RESERVEDWORLD_PUBLIC) ||
				world.equals(RESERVEDWORLD_PRIVATE) ||
				world.equals(RESERVEDWORLD_PROTECTED) ||
				world.equals(RESERVEDWORLD_RETURN);
	}

	public static boolean isFunctionType(String world) {
		return world.equals(RESERVEDWORLD_INT) || 
				world.equals(RESERVEDWORLD_FLOAT) || 
				world.equals(RESERVEDWORLD_CHAR) || 
				world.equals(RESERVEDWORLD_VOID);
	}

	public static boolean isVariableType(String world) {
		return world.equals(RESERVEDWORLD_INT) || 
				world.equals(RESERVEDWORLD_FLOAT) || 
				world.equals(RESERVEDWORLD_CHAR);
	}

}
