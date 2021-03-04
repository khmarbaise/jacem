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
import org.antlr.v4.runtime.tree.ParseTree;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExpressionVisitorTest {

  private static final String FIRST_EXPRESSION = "1_024 + 35/7+3*$1000+0b1000_0000 % $10 + 0o1234_567";

  static Stream<Arguments> createTestParameters() {
    return Stream.of(
        arguments("Addition", "3+5", 8L),
        arguments("Multiplication", "3*5", 15L),
        arguments("Hex value", "$1000", 4096L),
        arguments("Hex value", "$FFFF", 65535L),
        arguments("Hex value max", "$7FFFFFFFFFFFFFFF", Long.MAX_VALUE),
        arguments("Hex value with separator", "$1_000", 4096L),
        arguments("binary value", "0b10000000", 128L),
        arguments("binary value with separator", "0b1000_0000", 128L),
        arguments("octal value", "0o200", 128L),
        arguments("octal value with separator", "0o2_00", 128L)
    );
  }

  @ParameterizedTest(name = "{0}. expression: ''{1}'' expectedResult: ''{2}''")
  @MethodSource("createTestParameters")
  void name(String description, String expression, Long expectedResult) {
    CodePointCharStream input = CharStreams.fromString(expression);
    var parser = new ExprParser(new CommonTokenStream(new ExprLexer(input)));
    ParseTree tree = parser.start();
    ExpressionVisitor visitor = new ExpressionVisitor();
    Long result = visitor.visit(tree);

    Assertions.assertThat(result).isEqualTo(expectedResult);

  }

  @Test
  void first_grammer_test_method() {

    System.out.println("FirstGrammarTest.first_grammer_test_method");
    Map<String, Long> symbolTable = new HashMap<>();
    //TODO: Do we need to define a size of the symbol?
    symbolTable.put("ANTON", Long.valueOf(0x10000));


    CodePointCharStream input = CharStreams.fromString(FIRST_EXPRESSION);
    var parser = new ExprParser(new CommonTokenStream(new ExprLexer(input)));

    ParseTree tree = parser.start();
    ExpressionVisitor visitor = new ExpressionVisitor();
    Long result = visitor.visit(tree);

    System.out.printf("result = %x\n", result);

  }

}
