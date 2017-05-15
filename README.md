# mr-lang
lexer/parser for simple expressions based on https://tomassetti.me/getting-started-with-antlr-building-a-simple-expression-language/

example:
```
var n = 500
var sequence = map({0, n}, i -> (-1)^i / (2 * i + 1))
var pi = 4 * reduce(sequence, 0, x y -> x + y)
```
