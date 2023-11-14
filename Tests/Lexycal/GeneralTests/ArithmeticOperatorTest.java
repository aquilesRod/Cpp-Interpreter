package Lexycal.GeneralTests;

import compiladorl3.CompiladorL3;
import compiladorl3.Lexico;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

public class ArithmeticOperatorTest {

    @Test
    public void OnlyArithmeticOperator() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("+\n");
        writeFile.printf("-\n");
        writeFile.printf("*\n");
        writeFile.printf("/\n");

        file.close();

        compiler = new CompiladorL3();
        compiler.runLexico(path);
        lex = compiler.getLexico();

        assertEquals(5, lex.nextToken().getTipo());
        assertEquals(5, lex.nextToken().getTipo());
        assertEquals(5, lex.nextToken().getTipo());
        assertEquals(5, lex.nextToken().getTipo());

        File delete = new File(path);
        delete.delete();

    }

    @Test
    public void OtherTokensWithArithmeticOperator() throws Exception {
        Lexico lex;
        CompiladorL3 compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("1.21\n");
        writeFile.printf("identifier\n");
        writeFile.printf("*\n");
        writeFile.printf("+\n");
        writeFile.printf("-\n");
        writeFile.printf("/\n");

        file.close();

        compiler = new CompiladorL3();
        compiler.runLexico(path);
        lex = compiler.getLexico();

        assertEquals(1, lex.nextToken().getTipo());
        assertEquals(3, lex.nextToken().getTipo());
        assertEquals(5, lex.nextToken().getTipo());
        assertEquals(5, lex.nextToken().getTipo());
        assertEquals(5, lex.nextToken().getTipo());
        assertEquals(5, lex.nextToken().getTipo());

        File delete = new File(path);
        delete.delete();

    }


}
