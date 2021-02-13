package com.soebes.emulators;

import static com.soebes.emulators.memory.Ram.RAM_64K;

import com.soebes.emulators.memory.Ram;
import org.junit.jupiter.api.Test;

class CPU6502Test {

  @Test
  void first_test() {
    Ram ram = new Ram(RAM_64K);

    ram.write(0x1000, new int[]{0xa9, 0x33});
//    ram.writeByte(0x0, 0xA9);
//    ram.writeByte(0x0, 0xA9);
    CPU6502 cpu6502 = new CPU6502(ram);
    cpu6502.step();
  }
}