package com.soebes.emulators.cpu6502.register;

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

import com.soebes.emulators.cpu6502.memory.Memory;

import java.util.ArrayList;
import java.util.List;

public class AddressBus {

  private final List<Addressable> addressables;

  public AddressBus() {
    this.addressables = new ArrayList<>();
  }

  /**
   * @param memoryAccess The memory which should be attached.
   */
  public void attach(Memory memoryAccess, int start) {
    this.addressables.add(new Addressable(memoryAccess, start));
  }

  public void write(int address, int value) {
    Addressable addressableStream = this.addressables
        .stream()
        .filter(s -> address >= s.getStart() && address <= s.getEnd()).findAny()
        .orElseThrow(() -> new IllegalStateException("Unknown address given"));
    int segmentAddress = address - addressableStream.getStart();
    addressableStream.getMemory().writeByte(segmentAddress, (byte)value);
  }

  public Byte read(int address) {
    Addressable addressableStream = this.addressables
        .stream()
        .filter(s -> address >= s.getStart() && address <= s.getEnd()).findAny()
        .orElseThrow(() -> new IllegalStateException("Unknown address given"));
    int segmentAddress = address - addressableStream.getStart();
    return addressableStream.getMemory().readByte(segmentAddress);
  }

  public int read16(int address) {
    Byte lo = read(address);
    Byte hi = read(address + 1);

    return (hi << 8) | lo;
  }
}