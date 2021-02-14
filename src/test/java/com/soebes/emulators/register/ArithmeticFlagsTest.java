package com.soebes.emulators.register;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArithmeticFlagsTest {

  @Test
  void positive_value() {
    ArithmethicFlags arithmethicFlags = new ArithmethicFlags();
    arithmethicFlags.setValue(Integer.valueOf(0x7f).byteValue());
    assertThat(arithmethicFlags.isZeroFlag()).isFalse();
    assertThat(arithmethicFlags.isNegativeFlag()).isFalse();
  }

  @Test
  void negative_value() {
    ArithmethicFlags arithmethicFlags = new ArithmethicFlags();
    arithmethicFlags.setValue(0x80);
    assertThat(arithmethicFlags.isZeroFlag()).isFalse();
    assertThat(arithmethicFlags.isNegativeFlag()).isTrue();
    assertThat(arithmethicFlags.isCarryFlag()).isFalse();
  }
}