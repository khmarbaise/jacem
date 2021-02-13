package com.soebes.emulators.register;

public class Register16Bit {

  private short value;

  public Register16Bit(short value) {
    this.value = value;
  }

  public void incrementBy(short value) {
    this.value += value;
  }
}
