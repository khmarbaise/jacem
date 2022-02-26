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

import com.soebes.emulators.common.memory.AddressBus;
import com.soebes.emulators.common.memory.Ram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

/**
 * @author Karl Heinz Marbaise
 */
class C6502PerformanceIT {

  private Ram ram;

  private Ram stack;

  private Ram zeroPage;

  private C6502 cpu;

  private AddressBus addressBus;

  @BeforeEach
  void beforeEach() {
    this.addressBus = new AddressBus();

    // Page 0: zero page.
    this.zeroPage = new Ram(0x0100);
    // Page 1: stack memory
    this.stack = new Ram(0x0100);
    // 0xF000 bytes of RAM which is needed for the tests.
    this.ram = new Ram(0xF000);

    //Page 0 zero page
    addressBus.attach(zeroPage, 0x0000);
    //Page 1 with stack memory.
    addressBus.attach(stack, 0x0100);
    //Add ram at 0x0200
    addressBus.attach(ram, 0x1000);
    //TODO: consider attaching a ROM/RAM at 0xFF00 which contains reset/irq vector.
    this.cpu = new C6502(addressBus);

    for (int i = 0; i < this.ram.Size() - 1; i++) {
      ram.write(i, new int[]{0xEA}); // NOP
    }
  }

  @DisplayName("NOP")
  @RepeatedTest(2000)
  void running() {
    for (int i = 0; i < this.ram.Size() - 1; i++) {
      cpu.step();
    }
  }


}