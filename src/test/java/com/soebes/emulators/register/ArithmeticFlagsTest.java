package com.soebes.emulators.register;

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
class ArithmeticFlagsTest {

  @Test
  void positive_value() {
    ArithmethicFlags arithmethicFlags = new ArithmethicFlags();
    arithmethicFlags.setValue(Integer.valueOf(0x7f).byteValue());
    assertThat(arithmethicFlags.isZeroFlag()).isFalse();
    assertThat(arithmethicFlags.isNegativeFlag()).isFalse();
  }

  @Test
  void negative_value() {
    ArithmethicFlags arithmethicFlags = new ArithmethicFlags();
    arithmethicFlags.setValue(0x80);
    assertThat(arithmethicFlags.isZeroFlag()).isFalse();
    assertThat(arithmethicFlags.isNegativeFlag()).isTrue();
    assertThat(arithmethicFlags.isCarryFlag()).isFalse();
  }
}