package com.soebes.emulators;

public class Instruction {

  private OperationCode opc;

  private byte Op8;
  private int Op16;
  private int Address;

  public Instruction(OperationCode opc, byte op8, int op16, int address) {
    this.opc = opc;
    Op8 = op8;
    Op16 = op16;
    Address = address;
  }

  public OperationCode getOpc() {
    return opc;
  }

  public byte getOp8() {
    return Op8;
  }

  public int getOp16() {
    return Op16;
  }

  public int getAddress() {
    return Address;
  }
}
