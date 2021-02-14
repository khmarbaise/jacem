package com.soebes.emulators.register;

public class Register16Bit {

  private int value;

  public Register16Bit(int value) {
    this.value = value;
  }

  public void incrementBy(int value) {
    this.value += value;
  }

  public int value() {
    return this.value;
  }
}
