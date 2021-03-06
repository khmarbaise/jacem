package com.soebes.cralinkr.grammar.expression;

import java.util.Map;

public class SymbolTable {
  private final Map<String, Long> symbolTable;

  public SymbolTable(Map<String, Long> symbolTable) {
    this.symbolTable = symbolTable;
  }

  public SymbolTable add(String symbolName, Long value) {
    this.symbolTable.put(symbolName, value);
    return this;
  }
}
