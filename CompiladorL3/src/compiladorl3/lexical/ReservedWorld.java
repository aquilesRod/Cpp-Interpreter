package compiladorl3.lexical;

public class ReservedWorld {
	
	// Types
	public static final String RESERVEDWORLD_INT = "int";
	public static final String RESERVEDWORLD_FLOAT = "float";
	public static final String RESERVEDWORLD_CHAR = "char";

	// Flow Control
	public static final String RESERVEDWORLD_IF = "if";
	public static final String RESERVEDWORLD_ELSE = "else";
	public static final String RESERVEDWORLD_DO = "do";
	public static final String RESERVEDWORLD_WHILE = "while";
	public static final String RESERVEDWORLD_FOR = "for";

	// Function
	public static final String RESERVEDWORLD_RETURN = "return";
	public static final String RESERVEDWORLD_MAIN = "main";

	// 
	public static final String RESERVEDWORLD_PRINTF = "printf";
	public static final String RESERVEDWORLD_SCANF = "scanf";
	public static final String RESERVEDWORLD_INCLUDE = "include";

	
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
				world.equals(RESERVEDWORLD_PRINTF) ||
				world.equals(RESERVEDWORLD_SCANF) ||
				world.equals(RESERVEDWORLD_INCLUDE) ||
				world.equals(RESERVEDWORLD_RETURN);
	}

	public static boolean isType(String world) {
		return world.equals(RESERVEDWORLD_INT) || 
				world.equals(RESERVEDWORLD_FLOAT) || 
				world.equals(RESERVEDWORLD_CHAR);
	}

}
