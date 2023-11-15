package Sintatic;

import compiladorl3.Compiler;
import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class CommandTest {

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
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("if (id < 4) {id = 0;} else {id = 4;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
        } catch (RuntimeException e){
            Assert.fail();
        }
    }

    @Test
    public void withoutIFTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("(id < 4) {id = 0;} else {id = 4;}\n");
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
        this.writeFile.printf("if id < 4) {id = 0;} else {id = 4;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Ei comparça! Bora, abre o parênteses do if.";

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
        this.writeFile.printf("if (id < 4 {id = 0;} else {id = 4;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Ei comparça! Bora, fecha o parênteses do if.";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutElseTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("if (id < 4) {id = 0;} {id = 4;} ?\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Ei comparça, cade o ELSE do teu if?";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutInterrogationTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("if (id < 4) {id = 0;} else id = 4;}\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Ei comparça! Bora, abre o '{' do else.";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

}
