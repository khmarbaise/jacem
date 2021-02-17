package com.soebes.emulators.cpu8085.register;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Register16BitTest {

  @Test
  void initial_value_should_be_set() {
    Register16Bit register16Bit = new Register16Bit();

    assertThat(register16Bit.value()).isZero();
  }

  @Test
  void given_value_should_be_set() {
    Register16Bit register16Bit = new Register16Bit(1285);

    assertThat(register16Bit.value()).isEqualTo(1285);
  }
}