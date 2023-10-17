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

  static Stream<Arguments> mov() {
    return Stream.of(
        //                                               A    B C     D E     H L
        arguments("MOV A,B", new int[]{0b01_111_000}, 0x22, 0x2233, 0x4455, 0x6677),
        arguments("MOV A,C", new int[]{0b01_111_001}, 0x33, 0x2233, 0x4455, 0x6677),
        arguments("MOV A,D", new int[]{0b01_111_010}, 0x44, 0x2233, 0x4455, 0x6677),
        arguments("MOV A,E", new int[]{0b01_111_011}, 0x55, 0x2233, 0x4455, 0x6677),
        arguments("MOV A,H", new int[]{0b01_111_100}, 0x66, 0x2233, 0x4455, 0x6677),
        arguments("MOV A,L", new int[]{0b01_111_101}, 0x77, 0x2233, 0x4455, 0x6677),
        //                                               A    B C     D E     H L
        arguments("MOV B,A", new int[]{0b01_000_111}, 0x11, 0x1133, 0x4455, 0x6677),
        arguments("MOV B,C", new int[]{0b01_000_001}, 0x11, 0x3333, 0x4455, 0x6677),
        arguments("MOV B,D", new int[]{0b01_000_010}, 0x11, 0x4433, 0x4455, 0x6677),
        arguments("MOV B,E", new int[]{0b01_000_011}, 0x11, 0x5533, 0x4455, 0x6677),
        arguments("MOV B,H", new int[]{0b01_000_100}, 0x11, 0x6633, 0x4455, 0x6677),
        arguments("MOV B,L", new int[]{0b01_000_101}, 0x11, 0x7733, 0x4455, 0x6677),
        //                                               A    B C     D E     H L
        arguments("MOV C,A", new int[]{0b01_001_111}, 0x11, 0x2211, 0x4455, 0x6677),
        arguments("MOV C,B", new int[]{0b01_001_000}, 0x11, 0x2222, 0x4455, 0x6677),
        arguments("MOV C,D", new int[]{0b01_001_010}, 0x11, 0x2244, 0x4455, 0x6677),
        arguments("MOV C,E", new int[]{0b01_001_011}, 0x11, 0x2255, 0x4455, 0x6677),
        arguments("MOV C,H", new int[]{0b01_001_100}, 0x11, 0x2266, 0x4455, 0x6677),
        arguments("MOV C,L", new int[]{0b01_001_101}, 0x11, 0x2277, 0x4455, 0x6677),
        //                                               A    B C     D E     H L
        arguments("MOV D,A", new int[]{0b01_010_111}, 0x11, 0x2233, 0x1155, 0x6677),
        arguments("MOV D,B", new int[]{0b01_010_000}, 0x11, 0x2233, 0x2255, 0x6677),
        arguments("MOV D,C", new int[]{0b01_010_001}, 0x11, 0x2233, 0x3355, 0x6677),
        arguments("MOV D,E", new int[]{0b01_010_011}, 0x11, 0x2233, 0x5555, 0x6677),
        arguments("MOV D,H", new int[]{0b01_010_100}, 0x11, 0x2233, 0x6655, 0x6677),
        arguments("MOV D,L", new int[]{0b01_010_101}, 0x11, 0x2233, 0x7755, 0x6677),
        //                                               A    B C     D E     H L
        arguments("MOV E,A", new int[]{0b01_011_111}, 0x11, 0x2233, 0x4411, 0x6677),
        arguments("MOV E,B", new int[]{0b01_011_000}, 0x11, 0x2233, 0x4422, 0x6677),
        arguments("MOV E,C", new int[]{0b01_011_001}, 0x11, 0x2233, 0x4433, 0x6677),
        arguments("MOV E,D", new int[]{0b01_011_010}, 0x11, 0x2233, 0x4444, 0x6677),
        arguments("MOV E,H", new int[]{0b01_011_100}, 0x11, 0x2233, 0x4466, 0x6677),
        arguments("MOV E,L", new int[]{0b01_011_101}, 0x11, 0x2233, 0x4477, 0x6677),
        //                                               A    B C     D E     H L
        arguments("MOV H,A", new int[]{0b01_100_111}, 0x11, 0x2233, 0x4455, 0x1177),
        arguments("MOV H,B", new int[]{0b01_100_000}, 0x11, 0x2233, 0x4455, 0x2277),
        arguments("MOV H,C", new int[]{0b01_100_001}, 0x11, 0x2233, 0x4455, 0x3377),
        arguments("MOV H,D", new int[]{0b01_100_010}, 0x11, 0x2233, 0x4455, 0x4477),
        arguments("MOV H,E", new int[]{0b01_100_011}, 0x11, 0x2233, 0x4455, 0x5577),
        arguments("MOV H,L", new int[]{0b01_100_101}, 0x11, 0x2233, 0x4455, 0x7777),
        //                                               A    B C     D E     H L
        arguments("MOV L,A", new int[]{0b01_101_111}, 0x11, 0x2233, 0x4455, 0x6611),
        arguments("MOV L,B", new int[]{0b01_101_000}, 0x11, 0x2233, 0x4455, 0x6622),
        arguments("MOV L,C", new int[]{0b01_101_001}, 0x11, 0x2233, 0x4455, 0x6633),
        arguments("MOV L,D", new int[]{0b01_101_010}, 0x11, 0x2233, 0x4455, 0x6644),
        arguments("MOV L,E", new int[]{0b01_101_011}, 0x11, 0x2233, 0x4455, 0x6655),
        arguments("MOV L,H", new int[]{0b01_101_100}, 0x11, 0x2233, 0x4455, 0x6666)
    );
  }

  static Stream<Arguments> mov_r_m() {
    return Stream.of(
        arguments("MOV A,M", new int[]{0b01_111_110}, 0x33, 0x33, 0x0000, 0x0000, 0x1008),
        arguments("MOV B,M", new int[]{0b01_000_110}, 0x44, 0x00, 0x4400, 0x0000, 0x1008),
        arguments("MOV C,M", new int[]{0b01_001_110}, 0x55, 0x00, 0x0055, 0x0000, 0x1008),
        arguments("MOV D,M", new int[]{0b01_010_110}, 0x66, 0x00, 0x0000, 0x6600, 0x1008),
        arguments("MOV E,M", new int[]{0b01_011_110}, 0x77, 0x00, 0x0000, 0x0077, 0x1008),
        arguments("MOV H,M", new int[]{0b01_100_110}, 0x88, 0x00, 0x0000, 0x0000, 0x8808),
        arguments("MOV L,M", new int[]{0b01_101_110}, 0x99, 0x00, 0x0000, 0x0000, 0x1099)
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

    cpu.  step();

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

  @ParameterizedTest
  @MethodSource
  void mov(String operation, int[] opCodes, int expRegA, int expRegBC, int expRegDE, int expRegHL) {
    ram.write(0x0000, opCodes);
    cpu.a().setValue(0x11);
    cpu.bc().setValue(0x2233);
    cpu.de().setValue(0x4455);
    cpu.hl().setValue(0x6677);

    cpu.step();

    assertThatRegister(expRegA, expRegBC, expRegDE, expRegHL, 0x1000, 0x1001);
  }

  @ParameterizedTest
  @MethodSource
  void mov_r_m(String operation, int[] opCodes, Integer memValue, int expRegA, int expRegBC, int expRegDE, int expRegHL) {
    ram.write(0x0000, opCodes);
    cpu.hl().setValue(0x1008); // LXI H,1008H
    ram.writeByte(0x0008, memValue.byteValue());

    cpu.step();

    assertThatRegister(expRegA, expRegBC, expRegDE, expRegHL, 0x1000, 0x1001);
  }

  @Test
  void mov_m_a() {
    ram.write(0x0000, new int[]{0b01_110_111});
    cpu.a().setValue(0x21); // MVI A,$21
    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x21, 0x0000, 0x0000, 0x1008, 0x1000, 0x1001);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x21).byteValue());
  }

  @Test
  void mov_m_b() {
    ram.write(0x0000, new int[]{0b01_110_000});
    cpu.bc().setHv(0x22); // MVI B,$22
    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x2200, 0x0000, 0x1008, 0x1000, 0x1001);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x22).byteValue());
  }

  @Test
  void mov_m_c() {
    ram.write(0x0000, new int[]{0b01_110_001});
    cpu.bc().setLv(0x23); // MVI C,$23
    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x0023, 0x0000, 0x1008, 0x1000, 0x1001);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x23).byteValue());
  }

  @Test
  void mov_m_d() {
    ram.write(0x0000, new int[]{0b01_110_010});
    cpu.de().setHv(0x24); // MVI D,$24
    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x0000, 0x2400, 0x1008, 0x1000, 0x1001);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x24).byteValue());
  }

  @Test
  void mov_m_e() {
    ram.write(0x0000, new int[]{0b01_110_011});
    cpu.de().setLv(0x25); // MVI E,$25
    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x0000, 0x0025, 0x1008, 0x1000, 0x1001);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x25).byteValue());
  }

  @Test
  void mov_m_h() {
    ram.write(0x0000, new int[]{0b01_110_100});

    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x0000, 0x0000, 0x1008, 0x1000, 0x1001);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x10).byteValue());
  }

  @Test
  void mov_m_l() {
    ram.write(0x0000, new int[]{0b01_110_101});

    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x0000, 0x0000, 0x1008, 0x1000, 0x1001);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x08).byteValue());
  }

  @Test
  void mvi_m() {
    ram.write(0x0000, new int[]{0b00_110_110, 0x9f});
    cpu.hl().setValue(0x1008); // LXI H,1008H

    cpu.step();

    assertThatRegister(0x00, 0x0000, 0x0000, 0x1008, 0x1000, 0x1002);
    assertThat(ram.readByte(0x0008)).isEqualTo(Integer.valueOf(0x9f).byteValue());
  }

}