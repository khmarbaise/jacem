package com.soebes.emulators.register;

import com.soebes.emulators.memory.Ram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AddressBusTest {

  @Nested
  class SingleRamSegment {

    private AddressBus addressBus;
    private Ram ram;

    @BeforeEach
    void beforeEach() {
      addressBus = new AddressBus();
      ram = new Ram(0x0010);
      addressBus.attach(ram, 0x0000);
    }

    @Test
    void write_read_single_location() {
      addressBus.write(0x0000, 0xa9);

      assertThat(addressBus.read(0x0000)).isEqualTo((byte) 0xa9);
    }

    @Test
    void read_write_add_different_locations() {
      addressBus.write(0x0000, 0xa9);
      addressBus.write(0x0005, 0x05);
      addressBus.write(0x0008, 0xff);
      addressBus.write(0x000F, 0x21);

      assertThat(addressBus.read(0x0000)).isEqualTo((byte) 0xa9);
      assertThat(addressBus.read(0x0005)).isEqualTo((byte) 0x05);
      assertThat(addressBus.read(0x0008)).isEqualTo((byte) 0xff);
      assertThat(addressBus.read(0x0000F)).isEqualTo((byte) 0x21);
    }
  }

  @Nested
  class TwoConsecutiveRamSegment {

    private AddressBus addressBus;

    @BeforeEach
    void beforeEach() {
      addressBus = new AddressBus();
      Ram ram1 = new Ram(0x0010);
      addressBus.attach(ram1, 0x0000);
      Ram ram2 = new Ram(0x0010);
      addressBus.attach(ram2, 0x0010);
    }

    @Test
    void write_read_single_location() {
      addressBus.write(0x0000, 0xa9);

      assertThat(addressBus.read(0x0000)).isEqualTo((byte) 0xa9);
    }

    @Test
    void write_into_two_different_segments() {
      addressBus.write(0x0000, 0xa0);
      addressBus.write(0x0010, 0xa1);

      assertThat(addressBus.read(0x0000)).isEqualTo((byte) 0xa0);
      assertThat(addressBus.read(0x0010)).isEqualTo((byte) 0xa1);
    }

    @Test
    void write_first_byte_of_first_segment_and_write_last_byte_of_last_segment() {
      addressBus.write(0x0000, 0xa0);
      addressBus.write(0x001F, 0xa1);

      assertThat(addressBus.read(0x0000)).isEqualTo((byte) 0xa0);
      assertThat(addressBus.read(0x001F)).isEqualTo((byte) 0xa1);
    }

  }

  @Nested
  class ThreeNonConsecutiveRamSegment {

    private AddressBus addressBus;

    @BeforeEach
    void beforeEach() {
      addressBus = new AddressBus();
      Ram ram1 = new Ram(0x0010);
      addressBus.attach(ram1, 0x0000);
      Ram ram2 = new Ram(0x0010);
      addressBus.attach(ram2, 0x0020);
      Ram ram3 = new Ram(0x0010);
      addressBus.attach(ram3, 0x0050);
    }

    @Test
    void write_read_in_three_different_segements() {
      addressBus.write(0x0000, 0xa0);
      addressBus.write(0x0021, 0xa1);
      addressBus.write(0x0057, 0xa2);

      assertThat(addressBus.read(0x0000)).isEqualTo((byte) 0xa0);
      assertThat(addressBus.read(0x0021)).isEqualTo((byte) 0xa1);
      assertThat(addressBus.read(0x0057)).isEqualTo((byte) 0xa2);
    }

    @Test
    void write_first_byte_of_first_segment_and_write_last_byte_of_last_segment() {
      addressBus.write(0x0000, 0xa0);
      addressBus.write(0x002F, 0xa1);

      assertThat(addressBus.read(0x0000)).isEqualTo((byte) 0xa0);
      assertThat(addressBus.read(0x002F)).isEqualTo((byte) 0xa1);
    }

  }

  @Nested
  @DisplayName("Creating 64 K segments each 1 byte size.")
  class ExtremeSegments64K {

    private AddressBus addressBus;

    @BeforeEach
    void beforeEach() {
      addressBus = new AddressBus();
      for (int i = 0; i < 64 * 1024; i++) {
        Ram ram = new Ram(0x0001);
        addressBus.attach(ram, 0x0000 + i);
      }
    }

    @Test
    void write_read_in_all_different_segements() {
      for (int i = 0; i < 64 * 1024; i++) {
        addressBus.write(0x0000 + i, (byte) i);
      }

      for (int i = 0; i < 64 * 1024; i++) {
        byte read = addressBus.read(0x0000 + i);
        assertThat(read).isEqualTo((byte) i);
      }
    }
  }

  @Nested
  @DisplayName("Creating 32 K segments each 2 byte size.")
  class ExtremeSegments32K {

    private AddressBus addressBus;

    @BeforeEach
    void beforeEach() {
      addressBus = new AddressBus();
      for (int i = 0; i < 32 * 1024; i++) {
        Ram ram = new Ram(0x0002);
        addressBus.attach(ram, 0x0000 + i * 2);
      }
    }

    @Test
    void write_read_in_three_different_segements() {
      for (int i = 0; i < 32 * 1024; i++) {
        addressBus.write(0x0001 + i * 2, (byte) i);
      }

      for (int i = 0; i < 32 * 1024; i++) {
        byte read = addressBus.read(0x0001 + i * 2);
        assertThat(read).isEqualTo((byte) i);
      }
    }
  }

  @Nested
  @DisplayName("Creating 10000 segments each 2 byte size.")
  class MassOfSegements10000 {

    private AddressBus addressBus;

    @BeforeEach
    void beforeEach() {
      addressBus = new AddressBus();
      for (int i = 0; i < 10000; i++) {
        Ram ram = new Ram(0x0002);
        addressBus.attach(ram, 0x0000 + i * 2);
      }
    }

    @Test
    void write_read_in_three_different_segements() {
      for (int i = 0; i < 10000; i++) {
        addressBus.write(0x0001 + i * 2, (byte) i);
      }

      for (int i = 0; i < 10000; i++) {
        byte read = addressBus.read(0x0001 + i * 2);
        assertThat(read).isEqualTo((byte) i);
      }
    }
  }

}