package me.interair.lexer.mr.eval;

import lombok.extern.slf4j.Slf4j;
import me.interair.lexer.mr.MrParser;
import me.interair.lexer.mr.MrParserBaseVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
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

    @Override public Value visitVarReference(MrParser.VarReferenceContext ctx) {
        return new Value(ctx.ID().getSymbol().getText());
    }

    private Double resolveVar(Value val) {
        return val.isNumber() ? val.asDouble() : memory.get(val.asString()).asDouble();
    }

    @Override
    public Value visitRange(MrParser.RangeContext ctx) {
        Value left = visit(ctx.left);
        Value right = visit(ctx.right);
        return new Value(LongStream.range(left.asLong(), right.asLong()).boxed().collect(Collectors.toList()));
    }

    @Override
    public Value visitPrint(MrParser.PrintContext ctx) {
        Value value = visit(ctx.expression());
        System.out.println(value.getValue());
        return value;
    }

    @Override
    public Value visitMap(MrParser.MapContext ctx) {
        Value source = visit(ctx.source);
        Value lambda = visit(ctx.lambda());
        Object collect = source.asCollection().stream().map(v -> lambda.asLambda().apply(v)).collect(Collectors.toList());
        return new Value(collect);
    }

    @Override
    public Value visitLambda(MrParser.LambdaContext ctx) {
        int size = ctx.ID().size();
        Lambda lambda = new Lambda(() -> visit(ctx.expression()), memory, ctx.ID(0).getSymbol().getText(),
                size == 2 ? ctx.ID(1).getSymbol().getText() : null);
        return new Value(lambda);
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
