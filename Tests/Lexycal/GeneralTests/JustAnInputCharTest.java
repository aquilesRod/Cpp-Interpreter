package Lexycal.GeneralTests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import compiladorl3.Compiler;
import compiladorl3.lexical.Lexical;

public class JustAnInputCharTest {

    public Compiler compiler;
    public FileWriter file;
    public PrintWriter writeFile;
    public String path;
    public Lexical lex;

    @Before
    public void setUp() throws IOException {
        this.compiler = new Compiler();
        this.path = "codigoCompiladorTeste.txt";
        this.file = new FileWriter(this.path);
        this.writeFile = new PrintWriter(this.file);
    }

    @After
    public void tearDown() {
        File delete = new File(this.path);
        delete.delete();
    }

    @Test
    public void JustAnIntegerChar() throws Exception {
        this.writeFile.printf("1");
        this.file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(0, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }
    }

    @Test
    public void JustAnArithmeticOperatorChar() throws Exception {
        this.writeFile.printf("+");
        this.file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(5, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }		
    }

    @Test
    public void JustAnIdentifierOperatorChar() throws Exception {
        this.writeFile.printf("a");
        this.file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(3, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }		
    }

    @Test
    public void JustASpecialCharacterChar() throws Exception {
        this.writeFile.printf("{");
        this.file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(6, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }		
    }
    
    @Test
    public void JustARelationalOperatorChar() throws Exception {
        this.writeFile.printf("<");
        this.file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(4, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }	
    }

    @Test
    public void JustAAssignmentOperatorChar() throws Exception {
        this.writeFile.printf("=");
        this.file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(8, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }		
    }
}
