package com.soebes.emulators;

public class Ram implements IRAM {

  public static final int RAM_64K = 64 * 1024;

  private byte[] memory;

  public Ram(int sizeOfMemory) {
    this.memory = new byte[sizeOfMemory];
  }

  public byte[] readWord(int address) {
    return new byte[] {this.memory[address], this.memory[address + 1]};
  }

  public void writeByte(int address, byte value) {
    this.memory[address] = value;
  }

  public void writeWord(int address, byte value, byte value1) {
    this.memory[address] = value;
    this.memory[address + 1] = value1;
  }

  @Override
  public byte readByte(int address) {
    return this.memory[address];
  }
}
