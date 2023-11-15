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

public class InitialGeneralTest {

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
	public void BasicInitialGeneral() throws Exception {
		this.writeFile.printf("n1\n");
		this.writeFile.printf("n2\n");
		this.writeFile.printf("n123\n");
		this.writeFile.printf("1\n");
		this.writeFile.printf("123\n");
		this.writeFile.printf("1.0\n");
		this.writeFile.printf("11.0\n");
		this.writeFile.printf(")({},\n");
		this.writeFile.printf("	;\n");
		this.writeFile.printf("123abs\n");
		this.writeFile.printf("1_\n");
		this.writeFile.printf("$");
		this.file.close();

		try {
            this.compiler.runLexico(path);
            this.lex = compiler.getLexicon();

            assertEquals(3, lex.nextToken().getType());
			assertEquals(3, lex.nextToken().getType());
			assertEquals(3, lex.nextToken().getType());
			assertEquals(0, lex.nextToken().getType());
			assertEquals(0, lex.nextToken().getType());
			assertEquals(1, lex.nextToken().getType());
			assertEquals(1, lex.nextToken().getType());
			assertEquals(6, lex.nextToken().getType());
			assertEquals(6, lex.nextToken().getType());
			assertEquals(6, lex.nextToken().getType());
			assertEquals(6, lex.nextToken().getType());
			assertEquals(6, lex.nextToken().getType());
			assertEquals(6, lex.nextToken().getType());
			assertEquals(0, lex.nextToken().getType());
			assertEquals(3, lex.nextToken().getType());
			assertEquals(0, lex.nextToken().getType());
			assertEquals(3, lex.nextToken().getType()); 
			assertEquals(99, lex.nextToken().getType());
        } catch (RuntimeException e) {
            Assert.fail();
        }
	}
}
