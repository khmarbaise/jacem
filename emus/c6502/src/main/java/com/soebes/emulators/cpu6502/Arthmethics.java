package com.soebes.emulators.cpu6502;

import com.soebes.emulators.common.register.Register8Bit;
import com.soebes.emulators.cpu6502.register.ArithmeticFlags;

class Arthmethics {

  private Register8Bit input;
  private ArithmeticFlags flags;

  Arthmethics(Register8Bit input, ArithmeticFlags flags) {
    this.input = input;
    this.flags = flags;
  }

  Arthmethics sbcDecimal(byte operand, boolean carryFlag) {
    byte carryB = 0x00;

    if (carryFlag) {

    }
    byte carryIn = (byte) (carryFlag == true ? 1 : 0);

    byte low = (byte) ((this.input.value() & 0x0F) - (operand & 0x0F) - carryIn);
//    if (low & 0x10) != 0 {
//      low -= 6
//    }
//    if (low & 0x10) != 0 {
//      carryB = 1
//    }
//
//    high := (a >> 4) - (b >> 4) - carryB
//    if (high & 0x10) != 0 {
//      high -= 6
//    }
//
//    result := (low & 0x0F) | (high << 4)

//    c.setCarry((high & 0xFF) < 15)
//    c.setZero(result == 0)
    this.flags.setNegativeFlag(false); // BCD never sets negative
    this.flags.setOverflowFlag(false); // BCD never sets overflow

//    c.A = result

    return this;
  }

  Register8Bit intput() {
    return input;
  }

  ArithmeticFlags flags() {
    return flags;
  }
}
