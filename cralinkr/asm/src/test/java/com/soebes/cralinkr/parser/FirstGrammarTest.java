package com.soebes.cralinkr.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

class FirstGrammarTest {

  private static final String FIRST_EXPRESSION = "5*7";

  @Test
  void first_grammer_test_method() throws IOException {
    StringReader in = new StringReader(FIRST_EXPRESSION);

    System.out.println("FirstGrammarTest.first_grammer_test_method");

    CharStream input = CharStreams.fromReader(in);
    com.soebes.cralinkr.parser.ExprLexer lexer = new com.soebes.cralinkr.parser.ExprLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    com.soebes.cralinkr.parser.ExprParser parser = new com.soebes.cralinkr.parser.ExprParser(tokens);
    com.soebes.cralinkr.parser.ExprParser.ProgContext prog = parser.prog();// STAGE 1

    while(!prog.isEmpty()) {
      System.out.println("prog = " + prog);
    }
  }

  class MyListener extends com.soebes.cralinkr.parser.ExprBaseListener {

    @Override
    public void visitTerminal(TerminalNode node) {
      System.out.println("node = " + node);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
      System.out.println("node = " + node);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
      System.out.println("ctx = " + ctx);
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
      System.out.println("ctx = " + ctx);
    }
  }
}
