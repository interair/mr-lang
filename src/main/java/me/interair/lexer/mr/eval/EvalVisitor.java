package me.interair.lexer.mr.eval;

import me.interair.lexer.mr.MrParser;
import me.interair.lexer.mr.MrParserBaseVisitor;

import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends MrParserBaseVisitor<Value> {

    // store variables (there's only one global scope!)
    private Map<String, Value> memory = new HashMap<>();

    @Override
    public Value visitAssignment(MrParser.AssignmentContext ctx) {
        String id = ctx.ID().getText();
        Value value = this.visit(ctx.expression());
        memory.put(id, value);
        return value;
    }

    public Value visitBinaryOperation(MrParser.BinaryOperationContext ctx) {
        Value left = this.visit(ctx.left);
        Value right = this.visit(ctx.right);
        switch (ctx.operator.getType()) {
            case MrParser.PLUS:
                return new Value(left.asDouble() + right.asDouble());
            case MrParser.MINUS:
                return new Value(left.asDouble() - right.asDouble());
            case MrParser.POWER:
                return new Value(Math.pow(left.asDouble(), right.asDouble()));
            case MrParser.ASTERISK:
                return new Value(left.asDouble() * right.asDouble());
            case MrParser.DIVISION:
                return new Value(left.asDouble() / right.asDouble());
        }

        return visitChildren(ctx);
    }

    @Override
    public Value visitPrint(MrParser.PrintContext ctx) {
        Value value = this.visit(ctx.expression());
        System.out.println(value.getValue());
        return value;
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
