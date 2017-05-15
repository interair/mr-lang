package me.interair.lexer.mr.eval;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value {

    public static final Value VOID = new Value(new Object());

    private final Object value;

    public static Value buildDouble(String d) {
        return new Value(Double.valueOf(d));
    }

    public static Value buildLong(String i) {
        return new Value(Long.valueOf(i));
    }

    public static Value buildBoolean(String i) {
        return new Value(Boolean.valueOf(i));
    }

    public Boolean asBoolean() {
        return (Boolean) value;
    }

    public Double asDouble() {
        return ((Number) value).doubleValue();
    }

    public Long asLong() {
        return ((Number) value).longValue();
    }

    public String asString() {
        return String.valueOf(value);
    }

    public boolean isDouble() {
        return value instanceof Double;
    }

}