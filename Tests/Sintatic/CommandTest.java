package Sintatic;

import compiladorl3.CompiladorL3;
import compiladorl3.Sintatico;

// import compiladorl3.Lexico;
import org.junit.*;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

//Reports to @aquilesRod

public class CommandTest {

    @Test
    public void correctSyntaxTest() throws Exception {
        // Lexico lex;
        Sintatico sintatico;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("int main (){\n\t");
        writeFile.printf("if (id < 4) {id = 0} else {id = 4} ?\n");
        writeFile.printf("}");
        file.close();

        try {
            compiler = new CompiladorL3();
            compiler.runSintatic(path);
            // lex = compiler.getLexico();
            sintatico = compiler.getSintatico();
            
            Assert.assertTrue(false);
        } catch (RuntimeException e){
            Assert.fail();
        } 

        File delete = new File(path);
        delete.delete();
    }

    @Test
    public void withoutIFTest() throws Exception {
        // Lexico lex;
        Sintatico sintatico;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("int main (){\n\t");
        writeFile.printf("(id < 4) {id = 0} else {id = 4} ?\n");
        writeFile.printf("}");
        file.close();

        String lexema = "int main (){\n\t";
        String phrase = "Poxa comparça! Estava esperando você declara algum comando pertinho de "+lexema;

        try {
            compiler = new CompiladorL3();
            compiler.runSintatic(path);
            // lex = compiler.getLexico();
            sintatico = compiler.getSintatico();
            
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getStackTrace());
        }

        File delete = new File(path);
        delete.delete();
    }

    @Test
    public void withoutInitialParentesisTest() throws Exception {
        // Lexico lex;
        Sintatico sintatico;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("int main (){\n\t");
        writeFile.printf("if id < 4) {id = 0} else {id = 4} ?\n");
        writeFile.printf("}");
        file.close();

        String phrase = "Ei comparça! Bora, abre o parênteses do if.";

        try {
            compiler = new CompiladorL3();
            compiler.runSintatic(path);
            // lex = compiler.getLexico();
            sintatico = compiler.getSintatico();
            
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getStackTrace());
        }

        File delete = new File(path);
        delete.delete();
    }

    @Test
    public void withoutFinalParentesisTest() throws Exception {
        // Lexico lex;
        Sintatico sintatico;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("int main (){\n\t");
        writeFile.printf("if (id < 4 {id = 0} else {id = 4} ?\n");
        writeFile.printf("}");
        file.close();

        String phrase = "Ei comparça! Bora, fecha o parênteses do if.";

        try {
            compiler = new CompiladorL3();
            compiler.runSintatic(path);
            // lex = compiler.getLexico();
            sintatico = compiler.getSintatico();
            
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getStackTrace());
        }

        File delete = new File(path);
        delete.delete();
    }

    @Test
    public void withoutElseTest() throws Exception {
        // Lexico lex;
        Sintatico sintatico;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("int main (){\n\t");
        writeFile.printf("if (id < 4) {id = 0} {id = 4} ?\n");
        writeFile.printf("}");
        file.close();

        String phrase = "Ei comparça, cade o ELSE do teu if?";

        try {
            compiler = new CompiladorL3();
            compiler.runSintatic(path);
            // lex = compiler.getLexico();
            sintatico = compiler.getSintatico();
            
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getStackTrace());
        }

        File delete = new File(path);
        delete.delete();
    }

    @Test
    public void withoutInterrogationTest() throws Exception {
        // Lexico lex;
        Sintatico sintatico;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("int main (){\n\t");
        writeFile.printf("if (id < 4) {id = 0} else {id = 4}\n");
        writeFile.printf("}");
        file.close();

        String phrase = "Ei comparça, teu else acaba não é? Coloca ? no final.";

        try {
            compiler = new CompiladorL3();
            compiler.runSintatic(path);
            // lex = compiler.getLexico();
            sintatico = compiler.getSintatico();
            
            Assert.fail();
        } catch (RuntimeException e){
            assertEquals(phrase, e.getStackTrace());
        }

        File delete = new File(path);
        delete.delete();
    }

}
