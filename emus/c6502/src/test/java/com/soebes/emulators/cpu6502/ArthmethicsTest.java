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

import com.soebes.emulators.common.register.Register8Bit;
import com.soebes.emulators.cpu6502.register.ArithmeticFlags;
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


    assertThat(result.intput().value()).isEqualTo((byte) 0x41);
    assertThat(result.flags().isCarryFlag()).isTrue();
//    assert.EqualValues(t, 0x0302, cpu.PC)
//    assert.EqualValues(t, 0x41, cpu.A)
//    assert.True(t, cpu.getCarry())

  }
}