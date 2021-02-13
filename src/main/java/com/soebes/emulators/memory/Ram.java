package com.soebes.emulators.memory;

public class Ram implements Memory {

  public static final int RAM_64K = 64 * 1024;

  private byte[] memory;

  public Ram(int sizeOfMemory) {
    this.memory = new byte[sizeOfMemory];
  }

  public byte[] readWord(int address) {
    return new byte[] {this.memory[address], this.memory[address + 1]};
  }


  @Override
  public void writeByte(int address, byte value) {
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
  public byte readByte(int address) {
    return this.memory[address];
  }

  public void write(int address, int[] ints) {
    for (int i = 0; i < ints.length; i++) {
      this.memory[address] = (byte)ints[i];
    }
  }
}
