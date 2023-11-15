package Lexycal.GeneralTests;

import compiladorl3.Compiler;
import compiladorl3.lexical.Lexical;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

public class ArithmeticOperatorTest {

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
    public void OnlyArithmeticOperator() throws Exception {
        this.writeFile.printf("+\n");
        this.writeFile.printf("-\n");
        this.writeFile.printf("*\n");
        this.writeFile.printf("/\n");
        this.file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(5, lex.nextToken().getType());
            assertEquals(5, lex.nextToken().getType());
            assertEquals(5, lex.nextToken().getType());
            assertEquals(5, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }
    }

    @Test
    public void OtherTokensWithArithmeticOperator() throws Exception {
        this.writeFile.printf("1.21\n");
        this.writeFile.printf("identifier\n");
        this.writeFile.printf("*\n");
        this.writeFile.printf("+\n");
        this.writeFile.printf("-\n");
        this.writeFile.printf("/\n");
        file.close();

        try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(1, lex.nextToken().getType());
            assertEquals(3, lex.nextToken().getType());
            assertEquals(5, lex.nextToken().getType());
            assertEquals(5, lex.nextToken().getType());
            assertEquals(5, lex.nextToken().getType());
            assertEquals(5, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }
    }


}
