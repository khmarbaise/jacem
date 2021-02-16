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
import java.util.Collections;
import java.util.List;

/**
 * @author Karl Heinz Marbaise
 */
public class AddressBus {

  private final List<Addressable> addressables;

  private final List<Integer> segmentAddresses;

  public AddressBus() {
    this.addressables = new ArrayList<>();
    this.segmentAddresses = new ArrayList<>();
  }

  /**
   * @param memoryAccess The memory which should be attached.
   * @implNote The only assumption we make here is to have consecutive memory segments. No overlaying segments.
   * Currently not supported is to have memory parts which are mirrored. For example memory from 0x1000-0x1FFF which is
   * mirrored at 0xC000 up to 0xCFFF.
   */
  public void attach(Memory memoryAccess, int start) {
    Addressable adr = new Addressable(memoryAccess, start);
    this.segmentAddresses.add(adr.getStart());
    this.segmentAddresses.add(adr.getEnd());
    // TODO: Make a check for an existing start address in addressable and fail.
    this.addressables.add(adr);
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
    int foundPosition = Collections.binarySearch(this.segmentAddresses, address);
    if (foundPosition >= 0) {
      return this.addressables.get(foundPosition / 2);
    }
    int insertPosition = -foundPosition - 1;
    int pos = insertPosition / 2;
    return this.addressables.get(pos);
  }

  public int read16(int address) {
    Byte lo = read(address);
    Byte hi = read(address + 1);

    return (hi << 8) | lo;
  }
}
