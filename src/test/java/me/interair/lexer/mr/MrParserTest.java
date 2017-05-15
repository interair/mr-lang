package me.interair.lexer.mr;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;
import org.junit.Test;

@Slf4j
public class MrParserTest {

    @Test
    public void testMap() {
        parse("var sequence = map({0,n}, i -> (-1)^i / (2 * i + 1))");
    }

    @Test
    public void testReduce() {
        parse("var pi = 4 * reduce(sequence, 0, x y -> x + y) ");
    }

    @Test
    public void testCalcSum() {
        MrParser parse = parse("var a = 4 + 4 ");
        EvalVisitor visitor = new EvalVisitor();
        Value visit = visitor.visit(parse.mrFile());
        log.info("result: {}", visit);
    }

    private MrParser parse(String query) {
        MrLexer lexer = new MrLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MrParser parser = new MrParser(tokens);
//        AstPrinter astPrinter = new AstPrinter();
//        astPrinter.print(parser.mrFile());
        return parser;
    }
}
