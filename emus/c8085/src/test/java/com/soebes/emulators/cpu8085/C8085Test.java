package com.soebes.emulators.cpu8085;

import com.soebes.emulators.common.memory.AddressBus;
import com.soebes.emulators.common.memory.Ram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class C8085Test {

  private Ram ram;

  private Ram stack;

  private Ram zeroPage;

  private C8085 cpu;

  private AddressBus addressBus;

  @BeforeEach
  void beforeEach() {
    // Page 0: zero page.
    this.zeroPage = new Ram(0x0100);
    // Page 1: stack memory
    this.stack = new Ram(0x0100);
    // 16 Byte of RAM which is needed for the tests.
    this.ram = new Ram(0x0010);
    this.addressBus = new AddressBus();

    //Page 0 zero page
    addressBus.attach(zeroPage, 0x0000);
    //Page 1 with stack memory.
    addressBus.attach(stack, 0x0100);
    //Add ram at 0x1000
    addressBus.attach(ram, 0x1000);
    //TODO: consider attaching a ROM/RAM at 0xFF00 which contains reset/irq vector.
    this.cpu = new C8085(addressBus);
  }

  @Test
  void name() {

  }
}