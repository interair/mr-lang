package me.interair.lexer.mr;

import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.eval.EvalVisitor;
import me.interair.lexer.mr.eval.Value;
import org.antlr.v4.runtime.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class MrParserTest {

    @Test
    public void testPrintMap() {
        printTree("var sequence = map({0,n}, i -> (-1)^i / (2 * i + 1))");
    }

    @Test
    public void testPrintReduce() {
        printTree("var pi = 4 * reduce(sequence, 0, x y -> x + y) ");
    }

    @Test
    public void testRange() {
        Value visit = parse("var a = {0,5}");
        log.info("result: {}", visit);
        Assert.assertThat(visit.getValue(), equalTo(LongStream.range(0, 5).boxed().collect(Collectors.toList())));
    }

    @Test
    public void testVar() {
        Value visit = parse("var pi = 3.14 ");
        log.info("result: {}", visit);
        Assert.assertThat(visit.asDouble(), equalTo(3.14D));
    }

    @Test
    public void testCalcSum() {
        Value visit = parse("print( 2 * (4 + 4) )");
        log.info("result: {}", visit);
        Assert.assertThat(visit.asLong(), equalTo(16L));
    }

    private MrParser printTree(String query) {
        MrLexer lexer = new MrLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MrParser parser = new MrParser(tokens);
        AstPrinter astPrinter = new AstPrinter();
        astPrinter.print(parser.mrFile());
        return parser;
    }

    private Value parse(String query) {
        MrLexer lexer = new MrLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MrParser parser = new MrParser(tokens);
        EvalVisitor visitor = new EvalVisitor();
        Value visit = visitor.visit(parser.mrFile());
        return visit;
    }
}
