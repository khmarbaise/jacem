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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Karl Heinz Marbaise
 */
class BCDTest {

  @Test
  void convert_to_digits() {
    BCD bcd = new BCD(0x12345678);
    assertThat(bcd.toInt()).isEqualTo(0x12345678);
  }

  @Test
  void multiply_by_2() {
    BCD bcd1 = new BCD(0x12345678);
    BCD bcd2 = new BCD(0x12345678);
    assertThat(bcd1.add(bcd2).toInt()).isEqualTo(0x24691356);
  }

  @Test
  void add_9_and_1_to_get_ten() {
    BCD bcd1 = new BCD(0x9);
    BCD bcd2 = new BCD(0x1);
    assertThat(bcd1.add(bcd2).toInt()).isEqualTo(0x10);
  }

  @Test
  void carry_subsequent_positions() {
    BCD bcd1 = new BCD(0x450);
    BCD bcd2 = new BCD(0x450);
    assertThat(bcd1.add(bcd2).toInt()).isEqualTo(0x900);
  }

  @Test
  void carry_on_more_subsequent_positions() {
    BCD bcd1 = new BCD(0x459);
    BCD bcd2 = new BCD(0x459);
    assertThat(bcd1.add(bcd2).toInt()).isEqualTo(0x918);
  }

  @Test
  void chaining_add_calls() {
    BCD bcd1 = new BCD(0x12);
    BCD bcd2 = new BCD(0x18);
    BCD bcd3 = new BCD(0x19);
    assertThat(bcd1.add(bcd2).add(bcd3).toInt()).isEqualTo(0x49);
  }

}
