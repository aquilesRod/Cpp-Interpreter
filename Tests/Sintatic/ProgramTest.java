package Sintatic;

import compiladorl3.Compiler;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class ProgramTest {

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
        this.writeFile.printf("int main (){}$\n");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
        } catch (RuntimeException e){
            Assert.fail();
        }
    }

    @Test
    public void withoutReturnTypeTest() throws Exception {
        this.writeFile.printf("float main (){}$\n");
        this.file.close();

        String phrase = "Iih rapaz! Cadê o tipo de retorno do main?";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutMainTest() throws Exception {
        this.writeFile.printf("int int (){}$\n");
        this.file.close();

        String phrase = "Iih rapaz! Cadê o main?";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e) {
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutInitialParentesisTest() throws Exception {
        this.writeFile.printf("int main )\n");
        this.file.close();

        String phrase = "Ai você me quebra! Abre o parênteses do main.";

        try {
            compiler.runSintatic(path);

            Assert.fail();
        }  catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutFinalParentesisTest() throws Exception {
        this.writeFile.printf("int main )\n");
        this.file.close();

        String phrase = "Ai você me quebra! Abre o parênteses do main.";

        try {
            compiler.runSintatic(path);

            Assert.fail();
        }  catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

}
