package com.soebes.emulators.cpu6502;

import com.soebes.emulators.cpu6502.register.ArithmeticFlags;
import com.soebes.emulators.cpu6502.register.Register8Bit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArthmethicsTest {

  private ArithmeticFlags flags;
  private Arthmethics arthmethics;

  @BeforeEach
  void beforeEach() {
    Register8Bit r = new Register8Bit((byte) 0x00);
    flags = new ArithmeticFlags();
    arthmethics = new Arthmethics(r, flags);
  }

  @Test
  void subtract_zero_from_zero() {

    Arthmethics result = arthmethics.sbcDecimal((byte) 0x00, false);

    assertThat(result.intput().value()).isEqualTo(Integer.valueOf(0x00).byteValue());
    result.flags().isDecimalModeFlag();
    result.flags().isCarryFlag();
    result.flags().isZeroFlag();
    result.flags().isNegativeFlag();
  }

  @Test
  @Disabled
  void subtract_without_carry() {

    Register8Bit r = new Register8Bit((byte) 0x42);
    flags = new ArithmeticFlags();
    arthmethics = new Arthmethics(r, flags);
    Arthmethics result = arthmethics.sbcDecimal((byte) 0x00, true);


    assertThat(result.intput().value()).isEqualTo((byte)0x41);
    assertThat(result.flags().isCarryFlag()).isTrue();
//    assert.EqualValues(t, 0x0302, cpu.PC)
//    assert.EqualValues(t, 0x41, cpu.A)
//    assert.True(t, cpu.getCarry())

  }
}