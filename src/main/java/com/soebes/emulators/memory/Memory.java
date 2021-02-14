package com.soebes.emulators.memory;

public interface Memory {

  int Size();
  /**
   * @param address The address within the memory.
   * @return a single byte read from the given memory location.
   */
  Byte readByte(int address);

  /**
   * @param address The address within the memory.
   * @param value write a single byte at the given location in memory.
   */
  void writeByte(int address, Byte value);

}
