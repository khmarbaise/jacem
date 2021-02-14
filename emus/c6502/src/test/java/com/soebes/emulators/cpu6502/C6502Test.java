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

import com.soebes.emulators.cpu6502.memory.Ram;
import com.soebes.emulators.cpu6502.register.AddressBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Karl Heinz Marbaise
 */
class C6502Test {

  private Ram ram;

  private Ram stack;

  private Ram zeroPage;

  private C6502 cpu;

  @BeforeEach
  void beforeEach() {
    // Page 0: zero page.
    this.zeroPage = new Ram(0x0100);
    // Page 1: stack memory
    this.stack = new Ram(0x0100);
    // 16 Byte of RAM which is needed for the tests.
    this.ram = new Ram(0x0010);
    AddressBus addressBus = new AddressBus();

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

    assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, false, false, false, false);
  }

  private void assertThatRegister(int regA, int regX, int regY, int pc, boolean nFlag, boolean zFlag, boolean cFlag, boolean oFlag, boolean dFlag, boolean iFlag) {
    assertThat(cpu.getRegisterA().value()).isEqualTo(Integer.valueOf(regA).byteValue());
    assertThat(cpu.getRegX().value()).isEqualTo(Integer.valueOf(regX).byteValue());
    assertThat(cpu.getRegY().value()).isEqualTo(Integer.valueOf(regY).byteValue());
    assertThat(cpu.getPC().value()).isEqualTo(pc);

    assertThat(cpu.getPsf().isNegativeFlag()).as("The negative flag expected to be '%s'", nFlag).isEqualTo(nFlag);
    assertThat(cpu.getPsf().isZeroFlag()).as("The zero flag expected to be '%s'", zFlag).isEqualTo(zFlag);
    assertThat(cpu.getPsf().isCarryFlag()).as("The carry flag expected to be '%s'", cFlag).isEqualTo(cFlag);
    assertThat(cpu.getPsf().isOverflowFlag()).as("The overflow flag expected to be '%s'", oFlag).isEqualTo(oFlag);
    assertThat(cpu.getPsf().isDecimalModeFlag()).as("The decimal flag expected to be '%s'", dFlag).isEqualTo(dFlag);
    assertThat(cpu.getPsf().isInteruptDisable()).as("The interrupt disable flag expected to be '%s'", iFlag).isEqualTo(iFlag);

  }

  @Nested
  class LoadAccumulator {

    @Test
    @DisplayName("LDA #$33")
    void lda_immediate() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x33});

      cpu.step();

      assertThatRegister(0x33, 0x00, 0x00, 0x1002, false, false, false, false, false, false);
    }

    @Test
    @DisplayName("LDA #$80")
    void lda_immediate_negative() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x80});

      cpu.step();

      assertThatRegister(0x80, 0x00, 0x00, 0x1002, true, false, false, false, false, false);
    }

  }

  @Nested
  class IncrementX {
    @Test
    @DisplayName("INX with positive value")
    void inx_positive() {
      // INX
      ram.write(0x0000, new int[]{0xE8});

      cpu.getRegX().setValue((byte) 0x12);

      cpu.step();

      assertThatRegister(0x00, 0x13, 0x00, 0x1001, false, false, false, false, false, false);
    }

    @Test
    @DisplayName("INX 0x7F to 0x80 Flags")
    void inx_of_positive_value_which_becomes_negative() {
      ram.write(0x0000, new int[]{0xE8});

      cpu.getRegX().setValue((byte) 0x7f);

      cpu.step();

      assertThatRegister(0x00, 0x80, 0x00, 0x1001, true, false, false, false, false, false);
    }
  }

  @Nested
  class SetFlag {

    @Test
    @DisplayName("SEC set carry.")
    void sec() {
      ram.write(0x0000, new int[]{0x38});

      assertThat(cpu.getPsf().isCarryFlag()).isFalse();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, true, false, false, false);
    }

    @Test
    @DisplayName("SED set decimal.")
    void sed() {
      ram.write(0x0000, new int[]{0xF8});

      assertThat(cpu.getPsf().isDecimalModeFlag()).isFalse();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, false, false, true, false);
    }

    @Test
    @DisplayName("SEI set interrupt disable.")
    void sei() {
      ram.write(0x0000, new int[]{0x78});

      assertThat(cpu.getPsf().isInteruptDisable()).isFalse();

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, false, false, false, true);
    }

  }

  @Nested
  class ClearFlag {

    @Test
    @DisplayName("CLC clear carry.")
    void clc() {
      ram.write(0x0000, new int[]{0x18});

      cpu.getPsf().setCarryFlag(true);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, false, false, false, false);
    }

    @Test
    @DisplayName("CLD clear decimal.")
    void cld() {
      ram.write(0x0000, new int[]{0xD8});

      cpu.getPsf().setDecimalModeFlag(true);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, false, false, false, false);
    }

    @Test
    @DisplayName("CLI clear interuppt disable.")
    void cli() {
      ram.write(0x0000, new int[]{0x58});

      cpu.getPsf().setInteruptDisable(true);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, false, false, false, false);
    }

    @Test
    @DisplayName("CLV clear overflow")
    void clv() {
      ram.write(0x0000, new int[]{0xB8});

      cpu.getPsf().setOverflowFlag(true);

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x00, 0x1001, false, false, false, false, false, false);
    }

  }

}