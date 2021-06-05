package com.soebes.emulators.cpu8085;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class C8085Test {

  private Ram ram;

  private Ram stack;

  private C8085 cpu;

  private AddressBus addressBus;

  static Stream<Arguments> lxi() {
    return Stream.of(
        arguments("LXI B,$213F", new int[]{0x01, 0x3F, 0x21}, 0x00, 0x213F, 0x0000, 0x0000, 0x1000, 0x1003),
        arguments("LXI D,$218F", new int[]{0x11, 0x8F, 0x21}, 0x00, 0x0000, 0x218F, 0x0000, 0x1000, 0x1003),
        arguments("LXI H,$7C32", new int[]{0x21, 0x32, 0x7C}, 0x00, 0x0000, 0x0000, 0x7c32, 0x1000, 0x1003),
        arguments("LXI SP,$20FF", new int[]{0x31, 0xff, 0x20}, 0x00, 0x00, 0x0000, 0x0000, 0x20ff, 0x1003)
    );
  }

  static Stream<Arguments> mvi() {
    return Stream.of(
        arguments("MVI B,$22", new int[]{0b00_000_110, 0x22}, 0x00, 0x2200, 0x0000, 0x0000, 0x1000, 0x1002),
        arguments("MVI C,$23", new int[]{0b00_001_110, 0x23}, 0x00, 0x0023, 0x0000, 0x0000, 0x1000, 0x1002),
        arguments("MVI D,$24", new int[]{0b00_010_110, 0x24}, 0x00, 0x0000, 0x2400, 0x0000, 0x1000, 0x1002),
        arguments("MVI E,$25", new int[]{0b00_011_110, 0x25}, 0x00, 0x0000, 0x0025, 0x0000, 0x1000, 0x1002),
        arguments("MVI H,$26", new int[]{0b00_100_110, 0x26}, 0x00, 0x0000, 0x0000, 0x2600, 0x1000, 0x1002),
        arguments("MVI L,$27", new int[]{0b00_101_110, 0x27}, 0x00, 0x0000, 0x0000, 0x0027, 0x1000, 0x1002),
        arguments("MVI A,$21", new int[]{0b00_111_110, 0x21}, 0x21, 0x0000, 0x0000, 0x0000, 0x1000, 0x1002)
    );
  }

  @BeforeEach
  void beforeEach() {
    // stack memory (256 byte)
    this.stack = new Ram(0x0100);
    // usual RAM (16 Byte of RAM which is needed for the tests).
    this.ram = new Ram(0x0010);
    this.addressBus = new AddressBus();

    //page with stack memory.
    addressBus.attach(stack, 0x0100);
    //Add ram at 0x1000
    addressBus.attach(ram, 0x1000);
    this.cpu = new C8085(addressBus);
  }

  @ParameterizedTest
  @MethodSource
  void lxi(String operation, int[] opCodes, int regA, int regBC, int regDE, int regHL, int regSP, int regPC) {
    ram.write(0x0000, opCodes);

    cpu.step();

    assertThatRegister(regA, regBC, regDE, regHL, regSP, regPC);
  }

  private void assertThatRegister(int regA, int regBC, int regDE, int regHL, int regSP, int regPC) {
    assertThat(cpu.a().asInt()).as("The register A has not the expected value: '%s' instead of '%s'", regA, cpu.a().asInt()).isEqualTo(regA);
    assertThat(cpu.bc().value()).as("The register BC has not the expected value: '%s' instead of '%s'", regBC, cpu.bc().value()).isEqualTo(regBC);
    assertThat(cpu.de().value()).as("The register DE has not the expected value: '%s' instead of '%s'", regBC, cpu.de().value()).isEqualTo(regDE);
    assertThat(cpu.hl().value()).as("The register HL has not the expected value: '%s' instead of '%s'", regBC, cpu.hl().value()).isEqualTo(regHL);
    assertThat(cpu.sp().value()).as("The register SP has not the expected value: '%s' instead of '%s'", regSP, cpu.sp().value()).isEqualTo(regSP);
    assertThat(cpu.pc().value()).as("The register PC has not the expected value: '%s' instead of '%s'", regPC, cpu.pc().value()).isEqualTo(regPC);
  }

  @ParameterizedTest
  @MethodSource
  void mvi(String operation, int[] opCodes, int regA, int regBC, int regDE, int regHL, int regSP, int regPC) {
    ram.write(0x0000, opCodes);

    cpu.step();

    assertThatRegister(regA, regBC, regDE, regHL, regSP, regPC);
  }

  @Test
  void mvi_m() {
    ram.write(0x0000, new int[]{0b00_110_110, 0x7F});
    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x0000, 0x0000, 0x1008, 0x1000, 0x1002);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x7f).byteValue());
  }

}