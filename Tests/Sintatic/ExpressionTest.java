package Sintatic;

import compiladorl3.CompiladorL3;
import compiladorl3.Sintatico;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class ExpressionTest {

    public CompiladorL3 compiler;
    public FileWriter file;
    public PrintWriter writeFile;
    public String path;

    @Before
    public void setUp() throws IOException {
        this.compiler = new CompiladorL3();
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
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("while (a > 10){}\n");
        this.writeFile.printf("}$");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
        } catch (RuntimeException e){
            Assert.fail();
        }
    }

    @Test
    public void withoutRelacionalOperatorTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("while (a * 10){}\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Erro, relaciona ai o que foi! Ta faltando o operador relacional mermao...";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }
}