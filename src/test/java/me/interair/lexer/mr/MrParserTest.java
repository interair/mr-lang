package me.interair.lexer.mr;

import org.antlr.v4.runtime.*;
import org.junit.Test;

public class MrParserTest {

    @Test
    public void testMap() {
        parse("var sequence = map({0,n}, i -> (-1)^i / (2 * i + 1))");
    }

    @Test
    public void testReduce() {
        parse("var pi = 4 * reduce(sequence, 0, x y -> x + y) ");
    }

    private void parse(String query) {
        MrLexer lexer = new MrLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MrParser parser = new MrParser(tokens);
        AstPrinter astPrinter = new AstPrinter();
        astPrinter.print(parser.mrFile());
    }
}
