package com.soebes.emulators.register;

import com.soebes.emulators.memory.Memory;

public class Addressable {

  private Memory memory;
  private int start;
  private int end;

  public Addressable(Memory memory, int start) {
    this.memory = memory;
    this.start = start;
    this.end = start + memory.Size() - 1;
  }

  public Memory getMemory() {
    return memory;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
