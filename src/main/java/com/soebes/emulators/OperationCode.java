package com.soebes.emulators;

public class OperationCode {

  private final OpCode opCode;
  private final AddressingMode addressingMode;
  private final int size;
  private final int cycles;

  private OperationCode(OpCode opCode, AddressingMode addressingMode, int size, int cycles) {
    this.opCode = opCode;
    this.addressingMode = addressingMode;
    this.size = size;
    this.cycles = cycles;
  }

  public static final OperationCode of(OpCode opCode, AddressingMode addressingMode, int size, int cycles) {
    return new OperationCode(opCode, addressingMode, size, cycles);
  }
}
