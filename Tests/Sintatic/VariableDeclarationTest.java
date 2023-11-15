package Sintatic;

import compiladorl3.Compiler;
import compiladorl3.sintatic.Sintatic;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class VariableDeclarationTest {

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
        this.writeFile.printf("int a = 10;\n");
        this.writeFile.printf("}$");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
        } catch (RuntimeException e){
            Assert.fail();
        }
    }

    @Test
    public void withoutReceivedValueTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("int b;\n");
        this.writeFile.printf("b = ;\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Sim, essa expressao vai receber o que? coloca o valor bença!";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void withoutIdentifierTest() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("= 10;\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Lascou! Qual é o identificador bença?";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

}
