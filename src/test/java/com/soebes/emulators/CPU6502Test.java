package com.soebes.emulators;

import static com.soebes.emulators.memory.Ram.RAM_64K;

import com.soebes.emulators.memory.Ram;
import com.soebes.emulators.register.AddressBus;
import org.junit.jupiter.api.Test;

class CPU6502Test {

  @Test
  void first_test() {
    // 16 Byte of RAM
    Ram ram = new Ram(0x0010);
    AddressBus addressBus = new AddressBus();
    addressBus.attach(ram, 0x0000);

    // LDA #$33
    ram.write(0x0000, new int[]{0xa9, 0x33});

    CPU6502 cpu6502 = new CPU6502(addressBus);
    cpu6502.step();

  }
}