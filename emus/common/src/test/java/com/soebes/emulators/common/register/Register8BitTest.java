package com.soebes.emulators.common.register;

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
class Register8BitTest {

  @Test
  void check_for_initial_value() {
    Register8Bit register8Bit = new Register8Bit((byte) 0);

    assertThat(register8Bit.value()).isZero();
  }

  @Test
  void check_increment() {
    Register8Bit register16Bit = new Register8Bit((byte) 0);
    register16Bit.incr();

    assertThat(register16Bit.value()).isEqualTo((byte)1);
  }
}