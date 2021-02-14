package com.soebes.emulators;

public class OperationCode {

  private final OpCode opCode;
  private final AddressingMode addressingMode;
  private final int instructionSize;
  private final int cycles;

  private OperationCode(OpCode opCode, AddressingMode addressingMode, int instructionSize, int cycles) {
    this.opCode = opCode;
    this.addressingMode = addressingMode;
    this.instructionSize = instructionSize;
    this.cycles = cycles;
  }

  public static final OperationCode of(OpCode opCode, AddressingMode addressingMode, int instructionSize, int cycles) {
    return new OperationCode(opCode, addressingMode, instructionSize, cycles);
  }

  public OpCode getOpCode() {
    return opCode;
  }

  public AddressingMode getAddressingMode() {
    return addressingMode;
  }

  public int getInstructionSize() {
    return instructionSize;
  }

  public int getCycles() {
    return cycles;
  }
}
