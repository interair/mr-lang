package me.interair.lexer.mr.eval;

import lombok.AllArgsConstructor;
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
        context.put(inputVar, new Value(val));
        Value result = expression.get();
        context.put(outputVar, result);
        return result.getValue();
    }
}
