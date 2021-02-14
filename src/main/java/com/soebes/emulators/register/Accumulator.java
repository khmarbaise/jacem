package com.soebes.emulators.register;

public class Accumulator extends Register8Bit {
  private ArithmethicFlags flags;
  public Accumulator(byte value) {
    super(value);
    this.flags = new ArithmethicFlags();
  }

  public ArithmethicFlags getFlags() {
    return flags;
  }

  public void setValue(int value) {
    super.setValue(Integer.valueOf(value).byteValue());
    flags.setNegativeFlag((value >> 7) == 1);
    flags.setZeroFlag(value == 0);
  }

}
