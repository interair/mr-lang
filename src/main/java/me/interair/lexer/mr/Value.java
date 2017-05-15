package me.interair.lexer.mr;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value {

    public static final Value VOID = new Value(new Object());

    private final Object value;

    public Boolean asBoolean() {
        return (Boolean) value;
    }

    public Double asDouble() {
        return (Double) value;
    }

    public Integer asInteger() {
        return (Integer) value;
    }

    public String asString() {
        return String.valueOf(value);
    }

    public boolean isDouble() {
        return value instanceof Double;
    }

}