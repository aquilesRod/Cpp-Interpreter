package Lexycal.GeneralTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class AssignmentOperatorAndCharTest {

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
	public void charTest() throws Exception {
		this.writeFile.printf("'a'\n");
		this.writeFile.printf("'1'\n");
		this.file.close();
		
		try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(2, lex.nextToken().getType());
			assertEquals(2, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }
	}
	
	@Test
	public void charTestError() throws Exception{
		this.writeFile.printf("a''\n");
		this.writeFile.printf("''a\n");
		this.writeFile.printf("'@'\n");
		this.file.close();

		String errorMessage = "Error: invalid char \"'\"";
		
		try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(2, lex.nextToken().getType());
			assertEquals(2, lex.nextToken().getType());
			assertEquals(2, lex.nextToken().getType());
			Assert.fail();
        } catch (RuntimeException e) {
            assertEquals(errorMessage, e.getMessage());
        }
	}
	
	@Test
	public void AssignmentTest() throws Exception {
		this.writeFile.printf("== ");
		this.writeFile.printf("=\n");
		this.writeFile.printf("= ");
		this.writeFile.printf("==");
		this.file.close();

		try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(4, lex.nextToken().getType());
			assertEquals(8, lex.nextToken().getType());
			assertEquals(8, lex.nextToken().getType());
			assertEquals(4, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }
	}

	
	@Test
	public void AssignmentAndCharWithOthersTypesTest() throws Exception {
		this.writeFile.printf("a=3\n");
		this.writeFile.printf("a= 'a'\n");
		this.writeFile.printf("= 2.0\t");
		this.writeFile.printf("if ='a'");
		this.file.close();
		
		try {
			this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();
			
			assertEquals(3, lex.nextToken().getType());
			assertEquals(8, lex.nextToken().getType());
			assertEquals(0, lex.nextToken().getType());
			
			assertEquals(3, lex.nextToken().getType());
			assertEquals(8, lex.nextToken().getType());
			assertEquals(2, lex.nextToken().getType());
			
			assertEquals(8, lex.nextToken().getType());
			assertEquals(1, lex.nextToken().getType());
			
			assertEquals(7, lex.nextToken().getType());
			assertEquals(8, lex.nextToken().getType());
			assertEquals(2, lex.nextToken().getType());
		} catch (Exception e) {
			Assert.fail();
		}
	}


}
