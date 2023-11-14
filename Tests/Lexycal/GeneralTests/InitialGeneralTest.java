package Lexycal.GeneralTests;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

import compiladorl3.CompiladorL3;
import compiladorl3.Lexico;

public class InitialGeneralTest {

	@Test 
	public void BasicInitialGeneral() throws Exception {
		String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("n1\n");
		writeFile.printf("n2\n");
		writeFile.printf("n123\n");
		writeFile.printf("1\n");
		writeFile.printf("123\n");
		writeFile.printf("1.0\n");
		writeFile.printf("11.0\n");
		writeFile.printf(")({},\n");
		writeFile.printf("	;\n");
		writeFile.printf("123abs\n");
		writeFile.printf("1_\n");
		writeFile.printf("$");
		
		file.close();
		
		CompiladorL3 c = new CompiladorL3();
		c.runLexico(path);
		Lexico lex = c.getLexico();
		
		
		assertEquals(3, lex.nextToken().getTipo());
		assertEquals(3, lex.nextToken().getTipo());
		assertEquals(3, lex.nextToken().getTipo());

		assertEquals(0, lex.nextToken().getTipo());
		assertEquals(0, lex.nextToken().getTipo());

		assertEquals(1, lex.nextToken().getTipo());
		assertEquals(1, lex.nextToken().getTipo());

		assertEquals(6, lex.nextToken().getTipo());
		assertEquals(6, lex.nextToken().getTipo());
		assertEquals(6, lex.nextToken().getTipo());
		assertEquals(6, lex.nextToken().getTipo());
		assertEquals(6, lex.nextToken().getTipo());
		assertEquals(6, lex.nextToken().getTipo());

		assertEquals(0, lex.nextToken().getTipo());

		assertEquals(3, lex.nextToken().getTipo());

		assertEquals(0, lex.nextToken().getTipo());

		assertEquals(3, lex.nextToken().getTipo()); 

		assertEquals(99, lex.nextToken().getTipo());
		

		File delete = new File(path);
		delete.delete();
	}

	@Test
    public void AssignmentExpression() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("char a = 'a';");
  

        file.close();

        compiler = new CompiladorL3();
        compiler.runLexico(path);
        lex = compiler.getLexico();

        assertEquals(7, lex.nextToken().getTipo());
        assertEquals(3, lex.nextToken().getTipo());
        assertEquals(8, lex.nextToken().getTipo());
        assertEquals(2, lex.nextToken().getTipo());
        assertEquals(6, lex.nextToken().getTipo());

        File delete = new File(path);
        delete.delete();
    }
}
