package me.interair.lexer.mr;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Value {

    public static final Value VOID = new Value(new Object());

    private final Object value;

    public static Value doubleFromString(String d) {
        return new Value(Double.valueOf(d));
    }

    public static Value longFromString(String i) {
        return new Value(Integer.valueOf(i));
    }

    public boolean asBoolean() {
        return (boolean) value;
    }

    public double asDouble() {
        return (double) value;
    }

    public long asLong() {
        return (long) value;
    }

    public String asString() {
        return String.valueOf(value);
    }

    public boolean isDouble() {
        return value instanceof Double;
    }

}