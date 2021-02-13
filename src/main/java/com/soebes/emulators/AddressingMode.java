package com.soebes.emulators;

public enum AddressingMode {
  absolute("absolute"),
  absoluteX("absoluteX"),
  absoluteY("absoluteY"),
  accumulator("accumulator"),
  immediate("immediate"),
  implied("immediate"),
  indirect("(indirect)"),
  indirectX("(indirect,X)"),
  indirectY("(indirect,Y)"),
  relative("relative"),
  zeropage("zeropage"),
  zeropageX("zeropageX"),
  zeropageY("zeropageY");

  private String info;

  AddressingMode(String info) {
    this.info = info;
  }

  public String getInfo() {
    return info;
  }
}
