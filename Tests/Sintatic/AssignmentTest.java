package Sintatic;

import compiladorl3.CompiladorL3;

import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class AssignmentTest {

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
    public void NotTerminalAssignmentTest1() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("a = 10;\n");
        this.writeFile.printf("}$");
        this.file.close();

        try {
            this.compiler.runSintatic(path);
        } catch (RuntimeException e){
            Assert.fail();
        }
    }

    @Test
    public void NotTerminalAssignmentTest2() throws Exception {
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

    @Test
    public void NotTerminalAssignmentTest3() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("a 10;\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Lascou! Cade o operador de atribuição bença?";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

    @Test
    public void NotTerminalAssignmentTest4() throws Exception {
        this.writeFile.printf("int main (){\n\t");
        this.writeFile.printf("a = 10\n");
        this.writeFile.printf("}$");
        this.file.close();

        String phrase = "Finaliza a atribuição ae bença, coloca o ';'";

        try {
            this.compiler.runSintatic(path);
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getMessage());
        }
    }

}
