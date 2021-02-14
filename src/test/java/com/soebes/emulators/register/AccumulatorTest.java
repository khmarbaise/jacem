package com.soebes.emulators.register;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccumulatorTest {

  @Test
  void positive_value() {
    Accumulator accumulator = new Accumulator(Integer.valueOf(0).byteValue());
    accumulator.setValue(Integer.valueOf(0x7f).byteValue());
    assertThat(accumulator.getFlags().isZeroFlag()).isFalse();
    assertThat(accumulator.getFlags().isNegativeFlag()).isFalse();
  }

  @Test
  void negative_value() {
    Accumulator accumulator = new Accumulator(Integer.valueOf(0).byteValue());
    accumulator.setValue(0x80);
    assertThat(accumulator.getFlags().isZeroFlag()).isFalse();
    assertThat(accumulator.getFlags().isNegativeFlag()).isTrue();

    assertThat(accumulator.getFlags().isCarryFlag()).isFalse();
  }
}