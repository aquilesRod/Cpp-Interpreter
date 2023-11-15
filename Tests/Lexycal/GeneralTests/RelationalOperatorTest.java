package Lexycal.GeneralTests;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

import compiladorl3.Compiler;
import compiladorl3.lexical.Lexical;


public class RelationalOperatorTest {
    @Test
    public void OnlyRelationalOperators() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("<\n");
		writeFile.printf(">\n");
		writeFile.printf(">=\n");
		writeFile.printf("<=\n");
		writeFile.printf("==\n");
		writeFile.printf("!=");
        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }

	@Test
    public void OtherTokensWithRelationalOperators() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("1.21\n");
		writeFile.printf("identifier\n");
		writeFile.printf(">=\n");
		writeFile.printf("<=\n");
		writeFile.printf("==\n");
		writeFile.printf("!=");
        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(1, lex.nextToken().getTipo());
		assertEquals(3, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
	@Test
    public void LastTokenHasSpace() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("1.21\n");
		writeFile.printf("identifier\n");
		writeFile.printf(">=\n");
		writeFile.printf("<=\n");
		writeFile.printf("==\n");
		writeFile.printf("!=\n");
        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(1, lex.nextToken().getTipo());

		assertEquals(3, lex.nextToken().getTipo());
		
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());
		assertEquals(4, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
}
