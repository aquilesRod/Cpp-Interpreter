package compiladorl3;

public class Main {

	public static void main(String[] args) {
		String path = "CompiladorL3\\src\\compiladorl3\\codigo.txt";
		CompiladorL3 c = new CompiladorL3();
		try {
			// c.runLexico(path);
			c.runSintatic(path);
			//c.runSemantic(path);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
