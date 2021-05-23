package com.soebes.emulators.cpu6502;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import com.soebes.emulators.common.memory.AddressBus;
import com.soebes.emulators.common.memory.Ram;
import com.soebes.emulators.cpu6502.register.StatusRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumSet;

import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Carry;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Decimal;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Interrupt;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Negative;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Overflow;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Zero;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * Flags:NV-BDIZC
 *       !!!!!!!!
 *       !!!!!!!+-- Carry
 *       !!!!!!+--- Zero
 *       !!!!!+---- Interruppt
 *       !!!!+----- Decimal
 *       !!!+------ Break?
 *       !!+------- Undefined
 *       !+-------- Overflow
 *       +--------- Negative
 * </pre>
 *
 * @author Karl Heinz Marbaise
 */
class C6502Test {

  private Ram ram;

  private Ram stack;

  private Ram zeroPage;

  private C6502 cpu;

  private AddressBus addressBus;

  @BeforeEach
  void beforeEach() {
    // Page 0: zero page.
    this.zeroPage = new Ram(0x0100);
    // Page 1: stack memory
    this.stack = new Ram(0x0100);
    // 16 Byte of RAM which is needed for the tests.
    this.ram = new Ram(0x0010);
    this.addressBus = new AddressBus();

    //Page 0 zero page
    addressBus.attach(zeroPage, 0x0000);
    //Page 1 with stack memory.
    addressBus.attach(stack, 0x0100);
    //Add ram at 0x1000
    addressBus.attach(ram, 0x1000);
    //TODO: consider attaching a ROM/RAM at 0xFF00 which contains reset/irq vector.
    this.cpu = new C6502(addressBus);
  }

  @Test
  @DisplayName("NOP")
  void nop() {
    // NOP
    ram.write(0x0000, new int[]{0xEA});

    cpu.step();

    assertThatRegister(0x00, 0x00, 0x00, 0x1001);
    assertThatFlags();
  }

  private void assertThatRegister(int regA, int regX, int regY, int pc) {
    assertThat(cpu.regA().value()).as("The register A has not the expected value: '%s' instead of '%s'", regA, cpu.regA().value()).isEqualTo(Integer.valueOf(regA).byteValue());
    assertThat(cpu.regX().value()).as("The register X has not the expected value: '%s' instead of '%s'", regX, cpu.regX().value()).isEqualTo(Integer.valueOf(regX).byteValue());
    assertThat(cpu.regY().value()).as("The register Y has not the expected value: '%s' instead of '%s'", regY, cpu.regY().value()).isEqualTo(Integer.valueOf(regY).byteValue());
    assertThat(cpu.pc().value()).as("The registry pc has not the expected value: '%s' instead of '%s'", pc, cpu.pc()).isEqualTo(pc);
  }

  private void assertThatFlags(StatusRegister.Status... states) {
    EnumSet<StatusRegister.Status> all = EnumSet.allOf(StatusRegister.Status.class);
    Arrays.stream(states).forEach(state -> {
          assertThat(cpu.getPsr().isSet(state)).as("The " + state.name() + " flag expected to be set.").isEqualTo(true);
        }
    );
    all.removeAll(Arrays.asList(states));
    all.forEach(state -> {
          assertThat(cpu.getPsr().isNotSet(state)).as("The " + state.name() + " flag expected not being set.").isTrue();
        }
    );
  }

  @Nested
  class LDA {

    @Test
    @DisplayName("LDA #$33")
    void lda_immediate() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x33});

      cpu.step();

      assertThatRegister(0x33, 0x00, 0x00, 0x1002);
      assertThatFlags();
    }

    @Test
    @DisplayName("LDA #$00")
    void lda_immediate_zero() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x00});

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1002);
      assertThatFlags(Zero);
    }

    @Test
    @DisplayName("LDA #$80")
    void lda_immediate_negative() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x80});

      cpu.step();

      assertThatRegister(0x80, 0x00, 0x00, 0x1002);
      assertThatFlags(Negative);
    }

    @Test
    @DisplayName("LDA $80")
    void lda_zero_page() {
      // LDA $80
      ram.write(0x0000, new int[]{0xA5, 0x80});
      zeroPage.write(0x80, new int[]{0x10});

      cpu.step();

      assertThatRegister(0x10, 0x00, 0x00, 0x1002);
      assertThatFlags();
    }

    @Test
    @DisplayName("LDA $10,X")
    void lda_zero_page_x() {
      // LDA $10,X
      ram.write(0x0000, new int[]{0xB5, 0x10});
      cpu.regX().setValue((byte) 0x01);
      zeroPage.write(0x10, new int[]{0x80, 0x10});

      cpu.step();

      assertThatRegister(0x10, 0x01, 0x00, 0x1002);
      assertThatFlags();
    }

    @Test
    @DisplayName("LDA $1008")
    void lda_absolute() {
      ram.write(0x0000, new int[]{0xAD, 0x08, 0x10});
      ram.write(0x0008, new int[]{0x10});

      cpu.step();

      assertThatRegister(0x10, 0x00, 0x00, 0x1003);
      assertThatFlags();
    }

    @Test
    @DisplayName("LDA $1007,X")
    void lda_absolute_x() {
      ram.write(0x0000, new int[]{0xBD, 0x07, 0x10});
      ram.write(0x0008, new int[]{0x37});
      cpu.regX().setValue((byte) 0x01);

      cpu.step();

      assertThatRegister(0x37, 0x01, 0x00, 0x1003);
      assertThatFlags();
    }

    @Test
    @DisplayName("LDA $1007,Y")
    void lda_absolute_y() {
      ram.write(0x0000, new int[]{0xB9, 0x07, 0x10}); // LDA $1007,Y
      ram.write(0x0008, new int[]{0x42});
      cpu.regY().setValue((byte) 0x01);

      cpu.step();

      assertThatRegister(0x42, 0x00, 0x01, 0x1003);
      assertThatFlags();
    }

    @Test
    @DisplayName("LDA ($10,X)")
    void lda_indexed_indirect_x() {
      cpu.regX().setValue((byte) 0x01);
      ram.write(0x0000, new int[]{0xA1, 0x10});
      // Fill  LSB  MSB
      // 0x01 0x08 0x10
      //        !    !
      //        +----+--- $1008 => Read value from $1008 from RAM
      zeroPage.write(0x10, new int[]{0x01, 0x08, 0x10});
      ram.write(0x0008, new int[]{0x42});
      cpu.step();

      assertThatRegister(0x42, 0x01, 0x00, 0x1002);
      assertThatFlags();
    }

    @Test
    @DisplayName("LDA ($10),Y")
    void lda_indirect_indexed_y() {
      cpu.regY().setValue((byte) 0x01);
      ram.write(0x0000, new int[]{0xB1, 0x10});
      //  LSB  MSB
      // 0x05 0x10 ..
      //   !   !
      //   +---+--- $1005
      zeroPage.write(0x10, new int[]{0x05, 0x10});
      // Added $1005+y => $1006
      ram.write(0x0006, new int[]{0x36});
      cpu.step();

      assertThatRegister(0x36, 0x00, 0x01, 0x1002);
      assertThatFlags();
    }

  }

  @Nested
  class IncrementX {
    @Test
    @DisplayName("INX with positive value")
    void inx_positive() {
      // INX
      ram.write(0x0000, new int[]{0xE8});

      cpu.regX().setValue((byte) 0x12);

      cpu.step();

      assertThatRegister(0x00, 0x13, 0x00, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("INX 0x7F to 0x80 Flags")
    void inx_of_positive_value_which_becomes_negative() {
      ram.write(0x0000, new int[]{0xE8});

      cpu.regX().setValue((byte) 0x7f);

      cpu.step();

      assertThatRegister(0x00, 0x80, 0x00, 0x1001);
      assertThatFlags(Negative);
    }
  }

  @Nested
  class IncrementY {
    @Test
    @DisplayName("INY with positive value")
    void iny_positive() {
      ram.write(0x0000, new int[]{0xC8});

      cpu.regY().setValue((byte) 0x34);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x35, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("INY 0x7F to 0x80 Flags")
    void iny_of_positive_value_which_becomes_negative() {
      ram.write(0x0000, new int[]{0xC8});

      cpu.regY().setValue((byte) 0x7f);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x80, 0x1001);
      assertThatFlags(Negative);
    }
  }

  @Nested
  class INC {
    @Test
    @DisplayName("INC $20 (zero page)")
    void inc_zero_page() {
      ram.write(0x0000, new int[]{0xE6, 0x20});

      zeroPage.write(0x0020, new int[]{0x21});
      Byte read = addressBus.read(0x0020);
      assertThat(read).isEqualTo(Integer.valueOf(33).byteValue());

      cpu.step();

      read = addressBus.read(0x0020);
      assertThat(read).isEqualTo(Integer.valueOf(34).byteValue());
      assertThatRegister(0x00, 0x00, 0x00, 0x1002);
      assertThatFlags();
    }

    @Test
    @DisplayName("INC $20,X (zero page,X)")
    void inc_zero_page_x() {
      ram.write(0x0000, new int[]{0xF6, 0x21});

      zeroPage.write(0x0021, new int[]{0x34});
      Byte read = addressBus.read(0x0021);
      assertThat(read).isEqualTo(Integer.valueOf(52).byteValue());

      cpu.step();

      read = addressBus.read(0x0021);
      assertThat(read).isEqualTo(Integer.valueOf(53).byteValue());
      assertThatRegister(0x00, 0x00, 0x00, 0x1002);
      assertThatFlags();
    }

    @Test
    @DisplayName("INC $1005")
    void inc_absolute() {
      ram.write(0x0000, new int[]{0xEE, 0x05, 0x10});

      addressBus.write(0x1005, 0x54);
      Byte read = addressBus.read(0x1005);
      assertThat(read).isEqualTo(Integer.valueOf(0x54).byteValue());

      cpu.step();

      read = addressBus.read(0x1005);
      assertThat(read).isEqualTo(Integer.valueOf(0x55).byteValue());
      assertThatRegister(0x00, 0x00, 0x00, 0x1003);
      assertThatFlags();
    }

    @Test
    @DisplayName("INC $1005,X")
    void inc_absolute_x() {
      ram.write(0x0000, new int[]{0xFE, 0x05, 0x10});

      cpu.regX().setValue(Integer.valueOf(0x03).byteValue());
      addressBus.write(0x1008, 0x60);
      Byte read = addressBus.read(0x1008);
      assertThat(read).isEqualTo(Integer.valueOf(0x60).byteValue());

      cpu.step();

      read = addressBus.read(0x1008);
      assertThat(read).isEqualTo(Integer.valueOf(0x61).byteValue());
      assertThatRegister(0x00, 0x03, 0x00, 0x1003);
      assertThatFlags();
    }
  }

  @Nested
  class SBCDecimal {
    @Test
    @DisplayName("SBC #$01 Flags:xx-xDxxC")
    void sbc_decimal_immediate_1() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry).set(Decimal);
      cpu.regA().setValue((byte) 0x10);

      cpu.step();

      assertThatRegister(0x09, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry, Decimal);
    }

    @Test
    @DisplayName("SBC #$01 Flags:xx-xDxxC")
    void sbc_decimal_immediate_2() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry).set(Decimal);
      cpu.regA().setValue((byte) 0x20);

      cpu.step();

      assertThatRegister(0x19, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry, Decimal);
    }

    @Test
    @DisplayName("SBC #$01 Flags:xx-xDxxC")
    void sbc_decimal_immediate_3() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry).set(Decimal);
      cpu.regA().setValue((byte) 0x00);

      cpu.step();

      assertThatRegister(0x99, 0x00, 0x00, 0x1002);
      assertThatFlags(Decimal, Negative);
    }

  }

  @Nested
  class SBC {

    @Test
    @DisplayName("SBC #$01 Flags:xx-xxxxC")
    void sbc_immediate() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry);
      cpu.regA().setValue((byte) 0x42);

      cpu.step();

      assertThatRegister(0x41, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SBC #$01 Flags:Nx-xxxxC")
    void sbc_immediate_1() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry);
      cpu.regA().setValue((byte) 0x81);

      cpu.step();

      assertThatRegister(0x80, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry, Negative);
    }

    @Test
    @DisplayName("SBC #$01 Flags:xx-xxxxC")
    void sbc_immediate_2() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry);
      cpu.regA().setValue((byte) 0x7F);

      cpu.step();

      assertThatRegister(0x7E, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SBC #$01 Flags:xx-xxxZC")
    void sbc_immediate_3() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry);
      cpu.regA().setValue((byte) 0x01);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry, Zero);
    }

    @Test
    @DisplayName("SBC #$01 Flags:Nx-xxxxC")
    void sbc_immediate_4() {
      ram.write(0x0000, new int[]{0xE9, 0x01});
      cpu.getPsr().set(Carry);
      cpu.regA().setValue((byte) 0x00);

      cpu.step();

      assertThatRegister(0xFF, 0x00, 0x00, 0x1002);
      assertThatFlags(Negative);
    }

    @Test
    @DisplayName("SBC #$01 Flags:xV-xxxxC")
    void sbc_immediate_5() {
      cpu.regA().setValue((byte) 0x80);
      cpu.getPsr().set(Carry);
      ram.write(0x0000, new int[]{0xE9, 0x01});

      cpu.step();

      assertThatRegister(0x7F, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry, Overflow);
    }

    @Test
    @DisplayName("SBC $50 Flags:xx-xxxxC")
    void sbc_zero_page() {
      cpu.regA().setValue((byte) 0x70);
      cpu.getPsr().set(Carry);
      ram.write(0x0000, new int[]{0xE5, 0x50});
      zeroPage.write(0x0050, new int[]{0x60});

      cpu.step();

      assertThatRegister(0x10, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SBC $20,X Flags:xx-xxxxC")
    void sbc_zero_page_x() {
      cpu.regX().setValue((byte) 0x01);
      cpu.regA().setValue((byte) 0x70);
      cpu.getPsr().set(Carry);
      ram.write(0x0000, new int[]{0xF5, 0x20});
      zeroPage.write(0x0021, new int[]{0x60});

      cpu.step();

      assertThatRegister(0x10, 0x01, 0x00, 0x1002);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SBC $1008 Flags:xx-xxxxC")
    void sbc_absolute() {
      cpu.regA().setValue((byte) 0x70);
      cpu.getPsr().set(Carry);
      ram.write(0x0000, new int[]{0xED, 0x08, 0x10});
      ram.write(0x0008, new int[]{0x60});

      cpu.step();

      assertThatRegister(0x10, 0x00, 0x00, 0x1003);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SBC $1005,X Flags:xx-xxxxC")
    void sbc_absolute_x() {
      ram.write(0x0000, new int[]{0xFE, 0x05, 0x10});

      cpu.regX().setValue(Integer.valueOf(0x03).byteValue());
      addressBus.write(0x1008, 0x60);
      Byte read = addressBus.read(0x1008);
      assertThat(read).isEqualTo(Integer.valueOf(0x60).byteValue());

      cpu.step();

      read = addressBus.read(0x1008);
      assertThat(read).isEqualTo(Integer.valueOf(0x61).byteValue());
      assertThatRegister(0x00, 0x03, 0x00, 0x1003);
      assertThatFlags();
    }

    @Test
    @DisplayName("SBC $1005,Y Flags:xx-xxxxC")
    void sbc_absolute_y() {
      cpu.regA().setValue((byte) 0x70);
      cpu.regY().setValue((byte) 0x03);
      cpu.getPsr().set(Carry);

      ram.write(0x0000, new int[]{0xF9, 0x05, 0x10});
      ram.write(0x0008, new int[]{0x60});

      cpu.step();

      assertThatRegister(0x10, 0x00, 0x03, 0x1003);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SBC ($10,X) Flags:xx-xxxxC")
    void sbc_indexed_indirect_x() {
      cpu.getPsr().set(Carry);
      cpu.regA().setValue((byte) 0x70);
      cpu.regX().setValue((byte) 0x01);
      ram.write(0x0000, new int[]{0xE1, 0x10});
      // Fill  LSB  MSB
      // 0x01 0x08 0x10
      //        !    !
      //        +----+--- $1008 => Read value from $1008 from RAM
      zeroPage.write(0x10, new int[]{0x01, 0x08, 0x10});
      ram.write(0x0008, new int[]{0x60});
      cpu.step();

      assertThatRegister(0x10, 0x01, 0x00, 0x1002);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SBC ($10),Y Flags:xx-xxxxC")
    void sbc_indirect_indexed_Y() {
      cpu.getPsr().set(Carry);
      cpu.regA().setValue((byte) 0x70);
      cpu.regY().setValue((byte) 0x01);
      ram.write(0x0000, new int[]{0xF1, 0x10});
      ram.write(0x0008, new int[]{0x60});
      zeroPage.write(0x0010, new int[]{0x07, 0x10});

      cpu.step();

      assertThatRegister(0x10, 0x00, 0x01, 0x1002);
      assertThatFlags(Carry);
    }
  }

  @Nested
  class SetFlag {

    @Test
    @DisplayName("SEC set carry.  Flags:xx-xxxxC")
    void sec() {
      ram.write(0x0000, new int[]{0x38});

      assertThat(cpu.getPsr().isNotSet(Carry)).isTrue();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SED set decimal. Flags:xx-xDxxx")
    void sed() {
      ram.write(0x0000, new int[]{0xF8});

      assertThat(cpu.getPsr().isSet(Decimal)).isFalse();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags(Decimal);
    }

    @Test
    @DisplayName("SEI set interrupt disable. Flags:xx-xxIxx")
    void sei() {
      ram.write(0x0000, new int[]{0x78});

      assertThat(cpu.getPsr().isNotSet(Interrupt)).isTrue();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags(Interrupt);
    }

  }

  @Nested
  class ClearFlag {

    @Test
    @DisplayName("CLC clear carry. Flags:xx-xxxx0")
    void clc() {
      ram.write(0x0000, new int[]{0x18});

      cpu.getPsr().set(Carry);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("CLD clear decimal. Flags:xx-x0xxx")
    void cld() {
      ram.write(0x0000, new int[]{0xD8});

      cpu.getPsr().set(Decimal);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("CLI clear interuppt disable. Flags:xx-xx0xx")
    void cli() {
      ram.write(0x0000, new int[]{0x58});

      cpu.getPsr().set(Interrupt);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("CLV clear overflow Flags:x0-xxxxx")
    void clv() {
      ram.write(0x0000, new int[]{0xB8});

      cpu.getPsr().set(Overflow);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

  }


}