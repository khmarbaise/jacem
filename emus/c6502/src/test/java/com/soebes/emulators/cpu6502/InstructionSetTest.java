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

import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Karl Heinz Marbaise
 */
class InstructionSetTest {

  private static final Map<Integer, String> RESULT = Map.ofEntries(
      entry(0x1, "A"),
      entry(0x2, "B"),
      entry(0x3, "C"),
      entry(0x5, "C")
  );

  @Test
  void serach() {
    assertThat(RESULT.containsKey(5)).isTrue();
  }

  @Test
  void name() {
    int readByte = 0xa9;
    assertThat(InstructionSet.opcExists(readByte)).isTrue();
  }

  @Test
  void second_test() {
    OperationCode opc = InstructionSet.getOpc(0xA9);
    assertThat(opc.getOpCode()).isEqualTo(OpCode.LDA);
  }

}