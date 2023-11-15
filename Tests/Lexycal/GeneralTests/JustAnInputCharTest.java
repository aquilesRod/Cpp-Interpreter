package Lexycal.GeneralTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

import compiladorl3.Compiler;
import compiladorl3.lexical.Lexical;

public class JustAnInputCharTest {
    @Test
    public void JustAnIntegerChar() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("1");

        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(0, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustAnArithmeticOperatorChar() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("+");

        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(5, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustAnIdentifierOperatorChar() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("a");

        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(3, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustASpecialCharacterChar() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("{");

        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(6, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustARelationalOperatorChar() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("<");

        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(4, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }

    @Test
    public void JustAAssignmentOperatorChar() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("=");

        
        file.close();

        compiler = new Compiler();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(8, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
}
