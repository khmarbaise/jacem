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

  private C6502 c6502;

  @BeforeEach
  void beforeEach() {
    // 16 Byte of RAM
    this.ram = new Ram(0x0010);
    AddressBus addressBus = new AddressBus();
    addressBus.attach(ram, 0x0000);
    this.c6502 = new C6502(addressBus);
  }

  @Test
  @DisplayName("NOP")
  void nop() {
    // NOP
    ram.write(0x0000, new int[]{0xEA});

    c6502.step();

    assertThatRegister(0x00, 0x00, 0x00, 0x0001, false, false, false, false);
  }

  private void assertThatRegister(int regA, int regX, int regY, int pc, boolean nFlag, boolean zFlag, boolean cFlag, boolean oFlag) {
    assertThat(c6502.getRegisterA().value()).isEqualTo(Integer.valueOf(regA).byteValue());
    assertThat(c6502.getRegX().value()).isEqualTo(Integer.valueOf(regX).byteValue());
    assertThat(c6502.getRegY().value()).isEqualTo(Integer.valueOf(regY).byteValue());
    assertThat(c6502.getPC().value()).isEqualTo(pc);

    assertThat(c6502.getPsf().isNegativeFlag()).as("The negative flag expected to be '%s'", nFlag).isEqualTo(nFlag);
    assertThat(c6502.getPsf().isZeroFlag()).as("The zero flag expected to be '%s'", zFlag).isEqualTo(zFlag);
    assertThat(c6502.getPsf().isCarryFlag()).as("The carry flag expected to be '%s'", cFlag).isEqualTo(cFlag);
    assertThat(c6502.getPsf().isOverflowFlag()).as("The overflow flag expected to be '%s'").isEqualTo(oFlag);

  }

  @Nested
  class LoadAccumulator {

    @Test
    @DisplayName("LDA #$33")
    void lda_immediate() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x33});

      c6502.step();

      assertThatRegister(0x33, 0x00, 0x00, 0x0002, false, false, false, false);
    }

    @Test
    @DisplayName("LDA #$80")
    void lda_immediate_negative() {
      // LDA #$33
      ram.write(0x0000, new int[]{0xa9, 0x80});

      c6502.step();

      assertThatRegister(0x80, 0x00, 0x00, 0x0002, true, false, false, false);
    }

  }

  @Nested
  class IncrementX {
    @Test
    @DisplayName("INX with positive value")
    void inx_positive() {
      // INX
      ram.write(0x0000, new int[]{0xE8});

      c6502.getRegX().setValue((byte) 0x12);

      c6502.step();

      assertThatRegister(0x00, 0x13, 0x00, 0x0001, false, false, false, false);
    }

    @Test
    @DisplayName("INX 0x7F to 0x80 Flags")
    void inx_of_positive_value_which_becomes_negative() {
      ram.write(0x0000, new int[]{0xE8});

      c6502.getRegX().setValue((byte) 0x7f);

      c6502.step();

      assertThatRegister(0x00, 0x80, 0x00, 0x0001, true, false, false, false);
    }
  }
}