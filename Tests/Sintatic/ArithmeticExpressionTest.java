package Sintatic;

import compiladorl3.Compiler;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class ArithmeticExpressionTest {

    public Compiler compiler;
    public FileWriter file;
    public PrintWriter writeFile;
    public String path;

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
    public void correctSyntaxTest() throws Exception {
        this.writeFile.printf("int soma (int a, int b){\n\t");
        this.writeFile.printf("return a+b;\n\t");
        this.writeFile.printf("}\n\t");
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("int a = soma(10,20);\n");
        this.writeFile.printf("return 0;\n");
        this.writeFile.printf("}$");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
        } catch (RuntimeException e){
            Assert.fail();
        }
    }

    @Test
    public void withoutArithmeticOperatorTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("a = 10 20;\n");
        this.writeFile.printf("}$");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            // This test will pass case a RuntimeException is thrown
        }
    }

    @Test
    public void withoutInitialParentesisTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("a = (b*10;\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "[Error]: The token ')' is expected before ;";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }   
}
