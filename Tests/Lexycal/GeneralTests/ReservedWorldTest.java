package Lexycal.GeneralTests;

import static org.junit.Assert.*;

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

public class ReservedWorldTest {
		
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
	public void ReservedWordsWithSpace () throws Exception {
		this.writeFile.printf("while\n");
		this.writeFile.printf("if\n");
		this.writeFile.printf("else\n");
		this.writeFile.printf("do\n");
		this.writeFile.printf("main\n");
		this.writeFile.printf("char\n");
		this.writeFile.printf("float\n");
		this.writeFile.printf("int\n");
		this.writeFile.printf("for\n");
		this.file.close();

		try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }		
	}
	
	@Test
	public void ReservedWordsWithAndWithoutSpace() throws Exception {
		this.writeFile.printf("while\n");
		this.writeFile.printf("if\n");
		this.writeFile.printf("else");
		this.writeFile.printf("do\n");
		this.writeFile.printf("main\n");
		this.writeFile.printf("char\n");
		this.writeFile.printf("float");
		this.writeFile.printf("int\n");
		this.writeFile.printf("for");
		this.file.close();
		
		try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }		
	}
	
	@Test
	public void OtherTokensWhithReservedWord() throws Exception {
		this.writeFile.printf("n122\n");
		this.writeFile.printf("if\n");
		this.writeFile.printf("else\n");
		this.writeFile.printf("mc232\n");
		this.writeFile.printf("mc222\n");
		this.writeFile.printf("the55\n");
		this.writeFile.printf("float\n");
		this.writeFile.printf("int\n");
		this.writeFile.printf("for\n");
		this.file.close();
		
		try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

			assertEquals(3, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(3, lex.nextToken().getType());
			assertEquals(3, lex.nextToken().getType());
			assertEquals(3, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
			assertEquals(7, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }	
	}
}