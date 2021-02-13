package com.soebes.emulators;

import static com.soebes.emulators.Ram.RAM_64K;

import org.junit.jupiter.api.Test;

class CPU6502Test {

  @Test
  void first_test() {
    Ram ram = new Ram(RAM_64K);
    CPU6502 cpu6502 = new CPU6502(ram);
    cpu6502.step();
  }
}