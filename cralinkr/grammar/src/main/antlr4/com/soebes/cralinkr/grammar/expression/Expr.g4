grammar Expr;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
