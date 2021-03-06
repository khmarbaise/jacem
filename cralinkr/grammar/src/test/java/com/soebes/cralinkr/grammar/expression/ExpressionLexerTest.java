package com.soebes.cralinkr.grammar.expression;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExpressionLexerTest {

  static Stream<Arguments> tokens() {
    return Stream.of(
        arguments("Addition", "3+5", List.of("NUM", "ADD", "NUM")),
        arguments("Subtraction", "12-5", List.of("NUM", "SUB", "NUM")),
        arguments("Division", "12/5", List.of("NUM", "DIV", "NUM")),
        arguments("Multiplication", "12*5", List.of("NUM", "MUL", "NUM")),
        arguments("Hex", "$100", List.of("HEX")),
        arguments("Binary", "0b1000", List.of("BINARY")),
        arguments("Octal", "0o1120", List.of("OCTAL")),
        arguments("Identifier", "JOSH", List.of("IDENTIFIER")),
        arguments("Combined", "5*(2+5)", List.of("NUM", "MUL", "LPARENT", "NUM", "ADD", "NUM", "RPARENT"))
    );
  }

  @ParameterizedTest(name = "{0}: expression: ''{1}'' expectedTokens: ''{2}''")
  @MethodSource("tokens")
  void name(String description, String expression, List<String> expectedTokens) {
    var input = CharStreams.fromString(expression);
    var tokenStream = new CommonTokenStream(new ExprLexer(input));

    var tokenList = new ArrayList<String>();

    TokenSource tokenSource = tokenStream.getTokenSource();
    Token token = tokenSource.nextToken();
    while (token.getType() != Token.EOF) {
      String ruleName = ExprLexer.ruleNames[token.getType() - 1];
      tokenList.add(ruleName);
      token = tokenSource.nextToken();
    }

    assertThat(tokenList).as("Expected: %s but got: %s", expectedTokens, tokenList)
        .containsExactly(expectedTokens.toArray(new String[]{}));
  }

}
