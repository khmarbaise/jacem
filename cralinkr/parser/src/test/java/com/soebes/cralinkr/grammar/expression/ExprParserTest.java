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
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class ExprParserTest {

  private static final String FIRST_EXPRESSION = "1_024 + 35/7+3*$1000+0b1000_0000 % $10 + 0o1234_567";

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
