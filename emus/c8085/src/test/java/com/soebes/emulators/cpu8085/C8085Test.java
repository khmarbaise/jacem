package com.soebes.emulators.cpu8085;

import com.soebes.emulators.common.memory.AddressBus;
import com.soebes.emulators.common.memory.Ram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class C8085Test {

  private Ram ram;

  private Ram stack;

  private C8085 cpu;

  private AddressBus addressBus;

  @BeforeEach
  void beforeEach() {
    // stack memory (256 byte)
    this.stack = new Ram(0x0100);
    // usual RAM (16 Byte of RAM which is needed for the tests).
    this.ram = new Ram(0x0010);
    this.addressBus = new AddressBus();

    //page with stack memory.
    addressBus.attach(stack, 0x0100);
    //Add ram at 0x1000
    addressBus.attach(ram, 0x1000);
    this.cpu = new C8085(addressBus);
  }

  private void assertThatRegister(int regA, int regBC, int regDE, int regHL, int regSP, int regPC) {
    assertThat(cpu.a().asInt()).as("The register A has not the expected value: '%s' instead of '%s'", regA, cpu.a().asInt()).isEqualTo(regA);
    assertThat(cpu.bc().value()).as("The register BC has not the expected value: '%s' instead of '%s'", regBC, cpu.bc().value()).isEqualTo(regBC);
    assertThat(cpu.de().value()).as("The register DE has not the expected value: '%s' instead of '%s'", regBC, cpu.de().value()).isEqualTo(regDE);
    assertThat(cpu.hl().value()).as("The register HL has not the expected value: '%s' instead of '%s'", regBC, cpu.hl().value()).isEqualTo(regHL);
    assertThat(cpu.sp().value()).as("The register SP has not the expected value: '%s' instead of '%s'", regSP, cpu.sp().value()).isEqualTo(regSP);
  }

//  private void assertThatFlags(StatusRegister.Status... states) {
//    EnumSet<StatusRegister.Status> all = EnumSet.allOf(StatusRegister.Status.class);
//    List<StatusRegister.Status> statusList = Arrays.stream(states).collect(Collectors.toList());
//    statusList.forEach(state -> {
//          assertThat(cpu.getPsr().isSet(state)).as("The " + state.name() + " flag expected to be set.").isEqualTo(true);
//        }
//    );
//    all.removeAll(statusList);
//    all.forEach(state -> {
//          assertThat(cpu.getPsr().isNotSet(state)).as("The " + state.name() + " flag expected not being set.").isTrue();
//        }
//    );
//  }

  @Nested
  class LXI {

    @Test
    @DisplayName("LXI B,$213F")
    void bc() {
      ram.write(0x0000, new int[]{0x01, 0x3F, 0x21});

      cpu.step();

      assertThatRegister(0x00, 0x213F, 0x0000, 0x0000, 0x1000, 0x1003);
//    assertThatFlags();

    }

    @Test
    @DisplayName("LXI D,$218F")
    void de() {
      ram.write(0x0000, new int[]{0x11, 0x8F, 0x21});

      cpu.step();

      assertThatRegister(0x00, 0x0000, 0x218F, 0x0000, 0x1000, 0x1003);
//    assertThatFlags();

    }

    @Test
    @DisplayName("LXI H,$7C32")
    void hl() {
      ram.write(0x0000, new int[]{0x21, 0x32, 0x7C});

      cpu.step();

      assertThatRegister(0x00, 0x0000, 0x0000, 0x7c32, 0x1000, 0x1003);
//    assertThatFlags();

    }

    @Test
    @DisplayName("LXI SP,$20FF")
    void sp() {
      ram.write(0x0000, new int[]{0x31, 0xff, 0x20});

      cpu.step();

      assertThatRegister(0x00, 0x00, 0x0000, 0x0000, 0x20ff, 0x1003);
//    assertThatFlags();

    }

  }
}