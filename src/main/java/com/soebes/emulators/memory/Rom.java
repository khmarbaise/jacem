package com.soebes.emulators.memory;

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

/**
 * @author Karl Heinz Marbaise
 */
public class Rom implements MemoryAccess {

  public static final int RAM_64K = 64 * 1024;

  private Byte[] memory;

  public Rom(int sizeOfMemory) {
    this.memory = new Byte[sizeOfMemory];
  }

  public byte[] readWord(int address) {
    return new byte[] {this.memory[address], this.memory[address + 1]};
  }

  @Override
  public void writeByte(int address, Byte value) {
    this.memory[address] = value;
  }

  public void writeWord(int address, Byte value, Byte value1) {
    this.memory[address] = value;
    this.memory[address + 1] = value1;
  }

  @Override
  public int Size() {
    return this.memory.length;
  }

  @Override
  public Byte readByte(int address) {
    return this.memory[address];
  }

  // Only via constructor.
  void write(int address, int[] ints) {
    for (int i = 0; i < ints.length; i++) {
      this.memory[address] = (byte)ints[i];
    }
  }
}
