package me.interair.lexer.mr.eval;

import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.MrParser;
import me.interair.lexer.mr.MrParserBaseVisitor;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Slf4j
public class EvalVisitor extends MrParserBaseVisitor<Value> {

    // store variables (there's only one global scope!)
    private final Map<String, Value> memory = new HashMap<>();

    @Override
    public Value visitAssignment(MrParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();

        Value value = visit(ctx.expression() != null ? ctx.expression() : ctx.statement());
        memory.put(id, value);
        return value;
    }

    @Override
    protected Value aggregateResult(Value prev, Value nextResult) {
        return nextResult != null ? nextResult : prev;
    }

    @Override
    public Value visitParenExpression(MrParser.ParenExpressionContext ctx) {
        return visit(ctx.expression());
    }

    public Value visitBinaryOperation(MrParser.BinaryOperationContext ctx) {
        Double leftVal = resolveVar(visit(ctx.left));
        Double rightVal = resolveVar(visit(ctx.right));
        switch (ctx.operator.getType()) {
            case MrParser.PLUS:
                return new Value(leftVal + rightVal);
            case MrParser.MINUS:
                return new Value(leftVal - rightVal);
            case MrParser.POWER:
                return new Value(Math.pow(leftVal, rightVal));
            case MrParser.ASTERISK:
                return new Value(leftVal * rightVal);
            case MrParser.DIVISION:
                return new Value(leftVal / rightVal);
        }

        return visitChildren(ctx);
    }

    @Override
    public Value visitVarReference(MrParser.VarReferenceContext ctx) {
        return new Value(ctx.ID().getSymbol().getText());
    }

    private Double resolveVar(Value val) {
        return val.isNumber() ? val.asDouble() : memory.get(val.asString()).asDouble();
    }

    @Override
    public Value visitRangeExpression(MrParser.RangeExpressionContext ctx) {
        Value left = visit(ctx.left);
        Value right = visit(ctx.right);
        return new Value(LongStream.range(resolveVar(left).longValue(), resolveVar(right).longValue()).boxed().collect(Collectors.toList()));
    }

    @Override
    public Value visitPrint(MrParser.PrintContext ctx) {
        Value value = visit(ctx.expression());
        System.out.println(resolveVar(value));
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value visitMap(MrParser.MapContext ctx) {
        Value source = visit(ctx.source);
        Value lambda = visit(ctx.lambda());
        Object collect = source.asCollection().stream().map(v -> lambda.asLambda().apply(v)).collect(Collectors.toList());
        return new Value(collect);
    }

    @Override
    public Value visitMinusExpression(MrParser.MinusExpressionContext ctx) {
        Value value = visitChildren(ctx);
        return new Value(-value.asDouble());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value visitReduceStatement(MrParser.ReduceStatementContext ctx) {
        Value value = memory.get(ctx.source.getText());
        Value lambda = visit(ctx.lambda());
        lambda.asLambda().setOutputVarVal(Double.parseDouble(ctx.initVal.getText()));
        value.asCollection().forEach(v -> lambda.asLambda().apply(v));
        return lambda.asLambda().getOutputVarVal();
    }

    @Override
    public Value visitLambda(MrParser.LambdaContext ctx) {
        int size = ctx.ID().size();
        Assert.isTrue(size > 0, "count of id params can't be less then 1");
        return new Value(Lambda.builder()
                .context(memory)
                .expression(() -> visit(ctx.expression()))
                .inputVar(ctx.ID(0).getSymbol().getText())
                .outputVar(size == 2 ? ctx.ID(1).getSymbol().getText() : null)
                .build());
    }

    @Override
    public Value visitIntLiteral(MrParser.IntLiteralContext ctx) {
        return Value.buildLong(ctx.getText());
    }

    @Override
    public Value visitDecimalLiteral(MrParser.DecimalLiteralContext ctx) {
        return Value.buildDouble(ctx.getText());
    }

}
