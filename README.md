# MR-lang
lexer/parser for simple expressions based on https://tomassetti.me/getting-started-with-antlr-building-a-simple-expression-language/

example:
```
var n = 100000 
var sequence = map({0,n}, i -> (-1)^i / (2 * i + 1)) 
var pi = 4 * reduce(sequence, 0, x y -> x+y)
```

For getting UI run:
```
java -jar mr-lang-1.0-SNAPSHOT.jar
```

## UI
![status code with result](https://github.com/interair/mr-lang/master/doc/img/pi.png)

![status code with error](https://github.com/interair/mr-lang/master/doc/img/error.png)