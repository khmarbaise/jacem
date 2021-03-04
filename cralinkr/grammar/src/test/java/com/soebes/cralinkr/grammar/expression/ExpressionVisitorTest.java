package com.soebes.cralinkr.grammar.expression;

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

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExpressionVisitorTest {

  static Stream<Arguments> createExpressionToParse() {
    return Stream.of(
        arguments("Addition", "3+5", 8L),
        arguments("Multiplication", "3*5", 15L),
        arguments("Subtraction", "1024-256", 768L),
        arguments("Division", "1024/256", 4L),
        arguments("Unary simple", "-1024+3072", 2048L),
        arguments("Unary complex", "-1024-(-1024)", 0L),
        arguments("Hex value", "$1000", 4096L),
        arguments("Hex value", "$FFFF", 65535L),
        arguments("Hex value max", "$7FFFFFFFFFFFFFFF", Long.MAX_VALUE),
        arguments("Hex value max (lower case)", "$7fffffffffffffff", Long.MAX_VALUE),
        arguments("Hex value with separator", "$1_000", 4096L),
        arguments("binary value", "0b10000000", 128L),
        arguments("binary value with separator", "0b1000_0000", 128L),
        arguments("binary value with separator max value", "0b0111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111", Long.MAX_VALUE),
        arguments("octal value", "0o200", 128L),
        arguments("octal value with separator", "0o2_00", 128L),
        arguments("octal value with separator max value", "0o777_777_777_777_777_777_777", Long.MAX_VALUE),
        arguments("Basic calculation expression with parenthese", "1_024/256+3*(5+3)", 28L),
        arguments("Modulo Operation", "128%16", 0L),
        arguments("Modulo Operation", "129%15", 9L),
        arguments("Complex expression", "1_024 + 35/7+3*$1000+0b1000_0000 % $10 + 0o1234_567", 355708L)
    );
  }

  @ParameterizedTest(name = "{0}. expression: ''{1}'' expectedResult: ''{2}''")
  @MethodSource("createExpressionToParse")
  void name(String description, String expression, Long expectedResult) {
    var input = CharStreams.fromString(expression);
    var parser = new ExprParser(new CommonTokenStream(new ExprLexer(input)));
    var tree = parser.start();
    var visitor = new ExpressionVisitor();
    var result = visitor.visit(tree);

    assertThat(result).describedAs(description, expectedResult).isEqualTo(expectedResult);
  }

}
