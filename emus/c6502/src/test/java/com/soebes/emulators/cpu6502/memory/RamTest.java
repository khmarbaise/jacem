package com.soebes.emulators.cpu6502.memory;

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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Karl Heinz Marbaise
 */
class RamTest {

  @Test
  void write_amount_of_memory_and_check_if_the_same_is_read() {
    Ram ram = new Ram(0x0100);

    int[] givenContent = {0x01, 0x02, 0x03, 0x10, 0x20, 0x50};
    ram.write(0x0000, givenContent);

    int[] realMemoryContent = new int[givenContent.length];
    for (int address = 0; address < givenContent.length; address++) {
      Byte aByte = ram.readByte(address);
      realMemoryContent[address] = aByte;
    }

    assertThat(givenContent).isEqualTo(realMemoryContent);

  }
}