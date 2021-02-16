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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Karl Heinz Marbaise
 * @implNote Current implementation is lacking performance for a larger amount of Addressable elements see the unit
 * tests. For example 32 K segments with two bytes each.
 */
public class AddressBus {

  private final List<Addressable> addressables;

  public AddressBus() {
    this.addressables = new ArrayList<>();
  }

  private final Predicate<Addressable> contains(int address) {
    return s -> address >= s.getStart() && address <= s.getEnd();
  }

  /**
   * @param memoryAccess The memory which should be attached.
   */
  public void attach(Memory memoryAccess, int start) {
    // TODO: Make a check for an existing start address in addressable and fail.
    this.addressables.add(new Addressable(memoryAccess, start));
  }

  public void write(int address, int value) {
    Addressable addressableStream = findAdressable(address);
    int segmentAddress = address - addressableStream.getStart();
    addressableStream.getMemory().writeByte(segmentAddress, (byte) value);
  }

  public Byte read(int address) {
    Addressable addressableStream = findAdressable(address);
    int segmentAddress = address - addressableStream.getStart();
    return addressableStream.getMemory().readByte(segmentAddress);
  }

  private Addressable findAdressable(int address) {
    return this.addressables.stream()
        .filter(contains(address))
        .findAny()
        .orElseThrow(() -> new IllegalStateException("Unknown address given"));
  }

  public int read16(int address) {
    Byte lo = read(address);
    Byte hi = read(address + 1);

    return (hi << 8) | lo;
  }
}
