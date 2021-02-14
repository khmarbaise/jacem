package com.soebes.emulators;

import com.soebes.emulators.memory.Ram;
import com.soebes.emulators.register.AddressBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CPU6502Test {

  private Ram ram;

  private CPU6502 cpu6502;

  @BeforeEach
  void beforeEach() {
    // 16 Byte of RAM
    this.ram = new Ram(0x0010);
    AddressBus addressBus = new AddressBus();
    addressBus.attach(ram, 0x0000);
    this.cpu6502 = new CPU6502(addressBus);
  }

  @Test
  @DisplayName("NOP")
  void nop() {
    // LDA #$33
    ram.write(0x0000, new int[]{0xEA});

    cpu6502.step();

    assertThat(cpu6502.getRegisterA().value()).isEqualTo(Integer.valueOf(0x0).byteValue());
    assertThat(cpu6502.getRegisterX().value()).isEqualTo(Integer.valueOf(0x00).byteValue());
    assertThat(cpu6502.getRegisterY().value()).isEqualTo(Integer.valueOf(0x00).byteValue());
    assertThat(cpu6502.getRegisterPC().value()).isEqualTo(0x0001);

  }
  @Test
  @DisplayName("LDA #$33")
  void lda_immediate() {
    // LDA #$33
    ram.write(0x0000, new int[]{0xa9, 0x33});

    cpu6502.step();

    assertThat(cpu6502.getRegisterA().value()).isEqualTo((byte) 0x33);
    assertThat(cpu6502.getRegisterPC().value()).isEqualTo(0x0002);
    assertThat(cpu6502.getRegisterX().value()).isEqualTo(Integer.valueOf(0x00).byteValue());
    assertThat(cpu6502.getRegisterY().value()).isEqualTo(Integer.valueOf(0x00).byteValue());

  }
}