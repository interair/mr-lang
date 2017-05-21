package me.interair.lexer.mr.eval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class Result {
    private final Value value;
    private final List<Issue> issues;
}
