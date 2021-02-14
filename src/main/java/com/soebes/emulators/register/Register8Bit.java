package com.soebes.emulators.register;

public class Register8Bit {

  private byte value;

  public Register8Bit(byte value) {
    this.value = value;
  }

  public byte value() {
    return this.value;
  }

  public void setValue(byte value) {
    this.value = value;
  }
  public void incr() {
    Integer integer = Integer.valueOf(this.value);
    Integer result = integer + 1;
    this.value = result.byteValue();
  }
}
