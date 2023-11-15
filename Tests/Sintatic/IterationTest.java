package Sintatic;

import compiladorl3.CompiladorL3;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class IterationTest {

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
        this.writeFile.printf("while (id == 1) {j = 3;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
        } catch (RuntimeException e){
            Assert.fail();
        }
    }

    @Test
    public void withoutWhileTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("(id == 1) {j = 3;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Cade a palavra reservada da condicional pra começar bença?";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutInitialParentesisTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("while id == 1) {j = 3;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Ô Bença coloca o ( para iniciar a expreção relacional do while";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutFinalParentesisTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("while (id == 1 {j = 3;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Ô Bença coloca o ) para finalizar a expreção relacional do while";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }
}
