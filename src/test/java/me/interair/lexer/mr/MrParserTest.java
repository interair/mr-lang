package me.interair.lexer.mr;

import org.antlr.v4.runtime.*;
import org.junit.Test;

public class MrParserTest {

    @Test
    public void test() {
        String query = "var a=1+2";
        MrLexer lexer = new MrLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MrParser parser = new MrParser(tokens);
        parser.print();
        AstPrinter astPrinter = new AstPrinter();
        astPrinter.print(parser.mrFile());
    }
}
