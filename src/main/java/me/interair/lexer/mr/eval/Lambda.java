package me.interair.lexer.mr.eval;

import lombok.Builder;

import java.util.Map;
import java.util.function.Supplier;

@lombok.Value
@Builder
public class Lambda {

    private final Supplier<Value> expression;
    private final Map<String, Value> context;
    private final String inputVar;
    private final String outputVar;

    public Object apply(Object val) {
        setInputVarVal(val);
        Value result = expression.get();
        removeInputVar();
        setOutputVarVal(result);
        return result.getValue();
    }

    private void removeInputVar() {
        context.remove(inputVar);
    }

    private void setInputVarVal(Object val) {
        context.put(inputVar, wrap(val));
    }

    public void setOutputVarVal(Object val) {
        context.put(outputVar, wrap(val));
    }

    public Value getOutputVarVal() {
        return context.get(outputVar);
    }

    private Value wrap(Object val) {
        if (val instanceof Value) {
            return (Value) val;
        } else {
            return new Value(val);
        }
    }
}
