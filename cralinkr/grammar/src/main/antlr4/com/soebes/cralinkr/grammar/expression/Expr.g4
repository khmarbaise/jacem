grammar Expr;

start: expr*;

expr: '-' expr # GRPUNARY
    | expr multiplicationOp expr # GRPMULTIPLICATION
    | expr additionOp expr # GRPADDITION
    | '(' expr ')' # GRPPARENT
    | NUM # GRPNUM
    | HEX # GRPHEX
    | BINARY # GRPBINARY
    | OCTAL # GRPOCTAL
    | IDENTIFIER # GRPIDENTIFIER
    ;

OCTAL: ('0o'|'0O') [0-7][0-7_]*;// 0o1023_3333
BINARY: ('0b'|'0B') [01][_01]*; // 0b1010_1010
HEX : '$' [a-fA-F0-9][_a-fA-F0-9]*; // This allows $1000_0000_0000_0000
NUM : '-'?[0-9][_0-9]*;
IDENTIFIER: [a-zA-Z]+[a-zA-Z0-9]*;

WS: [ \t]+ -> skip;
COMMENT: (';'|'//') .*? '\r'? '\n' -> skip;

LPARENT: '(';
RPARENT: ')';
multiplicationOp: '*' | '/' | '%' ;
additionOp: '+' | '-';
