package Lexycal.GeneralTests;

import compiladorl3.Compiler;
import compiladorl3.lexical.Lexical;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

public class ArithmeticOperatorTest {

    @Test
    public void OnlyArithmeticOperator() throws Exception {
        Lexical lex;
        Compiler compiler;
        String path = "codigoCompilador.txt";
        FileWriter file = new FileWriter(path);
        PrintWriter writeFile = new PrintWriter(file);

        writeFile.printf("+\n");
        writeFile.printf("-\n");
        writeFile.printf("*\n");
        writeFile.printf("/\n");

        file.close();

        compiler = new Compiler();
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
        Lexical lex;
        Compiler compiler;
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

        compiler = new Compiler();
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
