package Lexycal.GeneralTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Test;

import compiladorl3.CompiladorL3;
import compiladorl3.Lexico;

public class JustAnInputCharTest {
    @Test
    public void JustAnIntegerChar() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("1");

        
        file.close();

        compiler = new CompiladorL3();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(0, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustAnArithmeticOperatorChar() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("+");

        
        file.close();

        compiler = new CompiladorL3();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(5, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustAnIdentifierOperatorChar() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("a");

        
        file.close();

        compiler = new CompiladorL3();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(3, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustASpecialCharacterChar() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("{");

        
        file.close();

        compiler = new CompiladorL3();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(6, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
    @Test
    public void JustARelationalOperatorChar() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("<");

        
        file.close();

        compiler = new CompiladorL3();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(4, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }

    @Test
    public void JustAAssignmentOperatorChar() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
		FileWriter file = new FileWriter(path);
		PrintWriter writeFile = new PrintWriter(file);

		writeFile.printf("=");

        
        file.close();

        compiler = new CompiladorL3();
		compiler.runLexico(path);
		lex = compiler.getLexico();

        assertEquals(8, lex.nextToken().getTipo());

        File delete = new File(path);
		delete.delete();		
    }
}
