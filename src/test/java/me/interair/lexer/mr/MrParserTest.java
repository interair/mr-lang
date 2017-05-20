package me.interair.lexer.mr;

import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.eval.EvalVisitor;
import me.interair.lexer.mr.eval.Value;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static java.util.Arrays.asList;
import static org.antlr.v4.runtime.CharStreams.fromString;
import static org.hamcrest.Matchers.closeTo;
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
    public void testMapReduce() {
        Value visit = parse("var sequence = map({0,5}, i -> (i + 1)) \n" +
                " var pi = 4 * reduce(sequence, 0, x y -> x + y) ");
        log.info("result: {}", visit);
        Assert.assertThat(visit.getValue(), equalTo(60D));
    }

    @Test
    public void testMapReduce2() {
        Value visit = parse("var n = 100000 \n" +
                "    var sequence = map({0,n}, i -> (-1)^i / (2 * i + 1)) \n"+
                "    var pi = 4 * reduce(sequence, 0, x y -> x+y)");
        log.info("result: {}", visit);
        Assert.assertThat(visit.asDouble(), closeTo(3.14D, 0.01D));
    }

    @Test
    public void testMap() {
        Value visit = parse("var sequence = map({0,5}, i -> (2 * i + 1))");
        log.info("result: {}", visit);
        Assert.assertThat(visit.getValue(), equalTo(asList(1D, 3D, 5D, 7D, 9D)));
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

    private void printTree(String query) {
        CommonTokenStream tokens = new CommonTokenStream(new MrLexer(fromString(query)));
        new AstPrinter().print(new MrParser(tokens).mrFile());
    }

    private Value parse(String query) {
        CommonTokenStream tokens = new CommonTokenStream(new MrLexer(fromString(query)));
        return new EvalVisitor().visit(new MrParser(tokens).mrFile());
    }
}
