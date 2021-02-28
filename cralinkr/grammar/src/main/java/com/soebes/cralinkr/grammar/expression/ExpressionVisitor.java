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

  private static final Map<String, BinaryOperator<Long>> operators =
      Map.ofEntries(
          entry("+", Long::sum),
          entry("-", (x, y) -> x - y),
          entry("*", (x, y) -> x * y),
          entry("/", (x, y) -> x / y),
          entry("%", (x, y) -> x % y)
      );

  public ExpressionVisitor() {
    //
  }

  private Long eval(String operator, Long left, Long right) {
    return operators.get(operator).apply(left, right);
  }

  @Override
  public Long visitGRPNUM(ExprParser.GRPNUMContext ctx) {
    System.out.println("MyVisitor.visitGRPNUM");
    System.out.println("ctx.getText() = " + ctx.getText());
    return Long.valueOf(ctx.getText().replaceAll("_", ""));
  }

  @Override
  public Long visitGRPBINARY(ExprParser.GRPBINARYContext ctx) {
    System.out.println("MyVisitor.visitGRPBINARY");
    String text = ctx.BINARY().getText();
    Long aLong = Long.valueOf(text.substring(2).replaceAll("_", ""), 2);
    System.out.printf("aLong = %x\n", aLong.longValue());
    return aLong;
  }

  @Override
  public Long visitGRPADDITION(ExprParser.GRPADDITIONContext ctx) {
    System.out.println("MyVisitor.visitGRPADDITION");
    System.out.println("ctx.getText() = " + ctx.getText());
    System.out.println("ctx.additionOp.getText() = " + ctx.additionOp().getText());
    return eval(ctx.additionOp().getText(), visit(ctx.expr(0)), visit(ctx.expr(1)));
  }

  @Override
  public Long visitGRPUNARY(ExprParser.GRPUNARYContext ctx) {
    System.out.println("MyVisitor.visitGRPUNARY");
    return -visit(ctx.expr());
  }

  @Override
  public Long visitGRPMULTIPLICATION(ExprParser.GRPMULTIPLICATIONContext ctx) {
    System.out.println("MyVisitor.visitGRPMULTIPLICATION");
    System.out.println("ctx.getText() = " + ctx.getText());
    System.out.println("ctx.multiplicationOp() = " + ctx.multiplicationOp().getText());
    return eval(ctx.multiplicationOp().getText(), visit(ctx.expr(0)), visit(ctx.expr(1)));
  }

  @Override
  public Long visitGRPPARENT(ExprParser.GRPPARENTContext ctx) {
    System.out.println("MyVisitor.visitGRPPARENT");
    return visit(ctx.expr());
  }

  @Override
  public Long visitGRPIDENTIFIER(ExprParser.GRPIDENTIFIERContext ctx) {
    //Access symbol table.
    System.out.println("MyVisitor.visitGRPIDENTIFIER");
    return null;
  }

  @Override
  public Long visitGRPHEX(ExprParser.GRPHEXContext ctx) {
    System.out.println("MyVisitor.visitGRPHEX");
    String text = ctx.HEX().getText();
    Long aLong = Long.valueOf(text.substring(1).replaceAll("_", ""), 16);
    System.out.printf("aLong = %x\n", aLong.longValue());
    return aLong;
  }

  @Override
  public Long visitGRPOCTAL(ExprParser.GRPOCTALContext ctx) {
    System.out.println("MyVisitor.visitGRPOCTAL");
    String text = ctx.OCTAL().getText();
    Long aLong = Long.valueOf(text.substring(2).replaceAll("_", ""), 8);
    System.out.printf("aLong = '%s' = %x ***\n", text.substring(2).replaceAll("_", ""), aLong.longValue());
    return aLong;
  }

  @Override
  public Long visitErrorNode(ErrorNode node) {
    System.out.println("MyVisitor.visitErrorNode");
    return null;
  }
}
