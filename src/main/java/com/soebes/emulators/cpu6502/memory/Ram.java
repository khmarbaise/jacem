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

/**
 * @author Karl Heinz Marbaise
 */
public class Ram implements Memory {

  private Byte[] memory;

  public Ram(int sizeOfMemory) {
    this.memory = new Byte[sizeOfMemory];
    for (int i = 0; i < memory.length; i++) {
      this.memory[i] = Byte.valueOf((byte) 0);
    }
  }

  public byte[] readWord(int address) {
    return new byte[] {this.memory[address], this.memory[address + 1]};
  }


  @Override
  public void writeByte(int address, Byte value) {
    this.memory[address] = value;
  }

  public void writeWord(int address, byte value, byte value1) {
    this.memory[address] = value;
    this.memory[address + 1] = value1;
  }

  @Override
  public int Size() {
    return this.memory.length;
  }

  @Override
  public Byte readByte(int address) {
    return Byte.valueOf(this.memory[address]);
  }

  public void write(int address, int[] ints) {
    for (int i = 0; i < ints.length; i++) {
      this.memory[address+i] = (byte)ints[i];
    }
  }
}
