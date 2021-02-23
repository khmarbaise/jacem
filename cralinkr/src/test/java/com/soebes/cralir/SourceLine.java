package com.soebes.cralir;

class SourceLine {
  private final Integer lineNumber;
  private final String line;

  public SourceLine(Integer lineNumber, String line) {
    this.lineNumber = lineNumber;
    this.line = line;
  }

  public Integer getLineNumber() {
    return lineNumber;
  }

  public String getLine() {
    return line;
  }
}
