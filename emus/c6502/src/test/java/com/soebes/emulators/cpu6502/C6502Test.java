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

import com.soebes.emulators.cpu6502.memory.AddressBus;
import com.soebes.emulators.cpu6502.memory.Ram;
import com.soebes.emulators.cpu6502.register.StatusRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.assertj.core.api.Assertions.assertThat;

/**
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
    assertThat(cpu.PC().value()).as("The registry PC has not the expected value: '%s' instead of '%s'", pc, cpu.PC()).isEqualTo(pc);
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
  class LoadAccumulator {

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
    @DisplayName("LDA #$80")
    void lda_immediate_negative() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x80});

      cpu.step();

      assertThatRegister(0x80, 0x00, 0x00, 0x1002);
      assertThatFlags(Negative);
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
  class SBC {
    /*
            func TestSBCImmediate(t *testing.T) {
          cpu, _, _ := NewRamMachine()
          cpu.LoadProgram([]byte{0xE9, 0x01}, 0x0300)
          cpu.A = 0x42
          cpu.setCarry(true)

          cpu.Step()

          assert.EqualValues(t, 0x0302, cpu.PC)
          assert.EqualValues(t, 0x41, cpu.A)
          assert.True(t, cpu.getCarry())
        }
     */
    @Test
    @Disabled
    @DisplayName("SBC #$01 (immediate)")
    void sbc_immediate() {
      ram.write(0x0000, new int[]{0xE9, 0x01});

      cpu.regA().setValue((byte) 0x42);
      cpu.getPsr().set(Carry);

      cpu.step();

      assertThat(cpu.regA()).isEqualTo(Integer.valueOf(41).byteValue());
      assertThatRegister(0x00, 0x00, 0x00, 0x1002);
      assertThatFlags(Carry);
    }

    @Test
    @Disabled
    @DisplayName("SBC $20,X (zero page,X)")
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
    @Disabled
    @DisplayName("SBC $1005")
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
    @Disabled
    @DisplayName("SBC $1005,X")
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
  class SetFlag {

    @Test
    @DisplayName("SEC set carry.")
    void sec() {
      ram.write(0x0000, new int[]{0x38});

      assertThat(cpu.getPsr().isNotSet(Carry)).isTrue();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags(Carry);
    }

    @Test
    @DisplayName("SED set decimal.")
    void sed() {
      ram.write(0x0000, new int[]{0xF8});

      assertThat(cpu.getPsr().isSet(Decimal)).isFalse();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags(Decimal);
    }

    @Test
    @DisplayName("SEI set interrupt disable.")
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
    @DisplayName("CLC clear carry.")
    void clc() {
      ram.write(0x0000, new int[]{0x18});

      cpu.getPsr().set(Carry);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("CLD clear decimal.")
    void cld() {
      ram.write(0x0000, new int[]{0xD8});

      cpu.getPsr().set(Decimal);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("CLI clear interuppt disable.")
    void cli() {
      ram.write(0x0000, new int[]{0x58});

      cpu.getPsr().set(Interrupt);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

    @Test
    @DisplayName("CLV clear overflow")
    void clv() {
      ram.write(0x0000, new int[]{0xB8});

      cpu.getPsr().set(Overflow);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001);
      assertThatFlags();
    }

  }


}