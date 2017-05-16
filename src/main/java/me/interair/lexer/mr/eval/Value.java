package me.interair.lexer.mr.eval;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class Value {

    private final Object value;

    public static Value buildDouble(String d) {
        return new Value(Double.valueOf(d));
    }

    public Double asDouble() {
        return ((Number) value).doubleValue();
    }

    public static Value buildLong(String i) {
        return new Value(Long.valueOf(i));
    }

    public Long asLong() {
        return ((Number) value).longValue();
    }

    public String asString() {
        return String.valueOf(value);
    }

    public Collection asCollection() {
        return (Collection)value;
    }

    public Lambda asLambda() {
        return (Lambda)value;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }


}