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

import org.antlr.v4.runtime.tree.ErrorNode;

import java.util.Map;
import java.util.function.BinaryOperator;

import static java.util.Map.entry;

class ExpressionVisitor extends ExprBaseVisitor<Long> {

  private static final Map<String, BinaryOperator<Long>> operators = Map.ofEntries(
      entry("+", Long::sum),
      entry("-", (x, y) -> x - y),
      entry("*", (x, y) -> x * y),
      entry("/", (x, y) -> x / y),
      entry("%", (x, y) -> x % y));

  private final SymbolTable symbolTable;

  /**
   * Create an instance of the expression visitor with an empty symbol table.
   */
  public ExpressionVisitor() {
    this.symbolTable = new SymbolTable();
  }

  /**
   * Create an instance of the expression visitor with the given symbol table.
   *
   * @param symbolTable The predefined symbol table.
   * @see SymbolTable
   */
  public ExpressionVisitor(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  private Long eval(String operator, Long left, Long right) {
    return operators.get(operator).apply(left, right);
  }

  @Override
  public Long visitGRPNUM(ExprParser.GRPNUMContext ctx) {
    return Long.valueOf(ctx.getText().replace("_", ""));
  }

  @Override
  public Long visitGRPBINARY(ExprParser.GRPBINARYContext ctx) {
    String s = ctx.BINARY().getText().substring(2).replace("_", "");
    return Long.valueOf(s, 2);
  }

  @Override
  public Long visitGRPADDITION(ExprParser.GRPADDITIONContext ctx) {
    return eval(ctx.additionOp().getText(), visit(ctx.expr(0)), visit(ctx.expr(1)));
  }

  @Override
  public Long visitGRPUNARY(ExprParser.GRPUNARYContext ctx) {
    return -visit(ctx.expr());
  }

  @Override
  public Long visitGRPMULTIPLICATION(ExprParser.GRPMULTIPLICATIONContext ctx) {
    return eval(ctx.multiplicationOp().getText(), visit(ctx.expr(0)), visit(ctx.expr(1)));
  }

  @Override
  public Long visitGRPPARENT(ExprParser.GRPPARENTContext ctx) {
    return visit(ctx.expr());
  }

  @Override
  public Long visitGRPIDENTIFIER(ExprParser.GRPIDENTIFIERContext ctx) {
    //Access symbol table. Not Yet implemented.
    System.out.println("MyVisitor.visitGRPIDENTIFIER");
    return null;
  }

  @Override
  public Long visitGRPHEX(ExprParser.GRPHEXContext ctx) {
    String s = ctx.HEX().getText().substring(1).replace("_", "");
    return Long.valueOf(s, 16);
  }

  @Override
  public Long visitGRPOCTAL(ExprParser.GRPOCTALContext ctx) {
    String s = ctx.OCTAL().getText().substring(2).replace("_", "");
    return Long.valueOf(s, 8);
  }

  @Override
  public Long visitErrorNode(ErrorNode node) {
    throw new IllegalStateException("can not parse");
    //    System.out.println("MyVisitor.visitErrorNode");
    //    return null;
  }
}
