lexer grammar MrLexer;

// Whitespace
NEWLINE            : '\r\n' | 'r' | '\n' ;
WS                 : [ \t] -> channel(1);

// Keywords
VAR                : 'var' ;
PRINT              : 'print';
INT                : 'Int';
DECIMAL            : 'Decimal';
MAP                : 'map';
REDUCE             : 'reduce';

// Literals
INTLIT             : '0'|[1-9][0-9]* ;
DECLIT             : '0'|[1-9][0-9]* '.' [0-9]+ ;

// Operators
PLUS               : '+' ;
MINUS              : '-' ;
ASTERISK           : '*' ;
DIVISION           : '/' ;
POWER              : '^' ;
ASSIGN             : '=' ;
LPAREN             : '(' ;
RPAREN             : ')' ;
LRANGE             : '{' ;
RRANGE             : '}' ;
LAMBDA             : '->';
COMMA              : ',' ;

// Identifiers
ID                 : [_]*[a-z][A-Za-z0-9_]* ;

UNMATCHED : . -> channel(1);