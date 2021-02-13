package com.soebes.emulators.register;

import com.soebes.emulators.memory.Memory;

import java.util.ArrayList;
import java.util.List;

public class AddressBus {

  private List<Addressable> addressables;

  public AddressBus() {
    this.addressables = new ArrayList<>();
  }

  /**
   * @param memory The memory which should be attached.
   */
  public void attach(Memory memory, int start) {
    this.addressables.add(new Addressable(memory, start));
  }

  public void write(int address, int value) {
    Addressable addressableStream = this.addressables
        .stream()
        .filter(s -> address >= s.getStart() && address <= s.getEnd()).findAny()
        .orElseThrow(() -> new IllegalStateException("Unknown address given"));
    int segmentAddress = address - addressableStream.getStart();
    addressableStream.getMemory().writeByte(segmentAddress, (byte)value);
  }

  public byte read(int address) {
    Addressable addressableStream = this.addressables
        .stream()
        .filter(s -> address >= s.getStart() && address <= s.getEnd()).findAny()
        .orElseThrow(() -> new IllegalStateException("Unknown address given"));
    int segmentAddress = address - addressableStream.getStart();
    return addressableStream.getMemory().readByte(segmentAddress);
  }
}
