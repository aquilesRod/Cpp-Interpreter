package Lexycal.GeneralTests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.Test;

import compiladorl3.Compiler;
import compiladorl3.lexical.Lexical;

public class ReservedWorldTest {
		//[REPORT TO @C03lh0]
	
		//[POSITIVE CASES]
	 	@Test
	    public void ReservedWordsWithSpace () throws Exception {
	        Lexical lex;
	        Compiler compiler;
	        String path = "codigoCompilador.txt";
			FileWriter file = new FileWriter(path);
			PrintWriter writeFile = new PrintWriter(file);

			writeFile.printf("while\n");
			writeFile.printf("if\n");
			writeFile.printf("else\n");
			writeFile.printf("do\n");
			writeFile.printf("main\n");
			writeFile.printf("char\n");
			writeFile.printf("float\n");
			writeFile.printf("int\n");
			writeFile.printf("for\n");
	        
	        file.close();

	        compiler = new Compiler();
			compiler.runLexico(path);
			lex = compiler.getLexico();

	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        
	      
	        File delete = new File(path);
			delete.delete();		
	    }
	 	
	 	@Test
	    public void ReservedWordsWithAndWithoutSpace() throws Exception {
	        Lexical lex;
	        Compiler compiler;
	        String path = "codigoCompilador.txt";
			FileWriter file = new FileWriter(path);
			PrintWriter writeFile = new PrintWriter(file);

			writeFile.printf("while\n");
			writeFile.printf("if\n");
			writeFile.printf("else");
			writeFile.printf("do\n");
			writeFile.printf("main\n");
			writeFile.printf("char\n");
			writeFile.printf("float");
			writeFile.printf("int\n");
			writeFile.printf("for");
	        
	        file.close();

	        compiler = new Compiler();
			compiler.runLexico(path);
			lex = compiler.getLexico();

	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	      
	        File delete = new File(path);
			delete.delete();		
	    }
	 	
	 	@Test
	    public void OtherTokensWhithReservedWord() throws Exception {
	        Lexical lex;
	        Compiler compiler;
	        String path = "codigoCompilador.txt";
			FileWriter file = new FileWriter(path);
			PrintWriter writeFile = new PrintWriter(file);

			writeFile.printf("n122\n");
			writeFile.printf("if\n");
			writeFile.printf("else\n");
			writeFile.printf("mc232\n");
			writeFile.printf("mc222\n");
			writeFile.printf("the55\n");
			writeFile.printf("float\n");
			writeFile.printf("int\n");
			writeFile.printf("for\n");
	        
	        file.close();

	        compiler = new Compiler();
			compiler.runLexico(path);
			lex = compiler.getLexico();

	        assertEquals(3, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(3, lex.nextToken().getTipo());
	        assertEquals(3, lex.nextToken().getTipo());
	        assertEquals(3, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	        assertEquals(7, lex.nextToken().getTipo());
	      
	        File delete = new File(path);
			delete.delete();		
	    }
}
