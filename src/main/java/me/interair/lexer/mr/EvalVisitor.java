package me.interair.lexer.mr;

import org.antlr.v4.runtime.tree.*;

import java.util.HashMap;
import java.util.Map;

public class EvalVisitor extends MrParserBaseVisitor<Value> {

    // used to compare floating point numbers
    public static final double SMALL_VALUE = 0.00000000001;

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
                return left.isDouble() || right.isDouble() ?
                        new Value(left.asDouble() + right.asDouble()) :
                        new Value(left.asInteger() + right.asInteger());

        }

        return visitChildren(ctx);
    }

    @Override
    public Value visitIntLiteral(MrParser.IntLiteralContext ctx) {
        return new Value(Integer.valueOf(ctx.getText()));
    }
    @Override
    public Value visitDecimalLiteral(MrParser.DecimalLiteralContext ctx) {
        return new Value(Double.valueOf(ctx.getText()));
    }

}
