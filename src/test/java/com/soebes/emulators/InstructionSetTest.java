package com.soebes.emulators;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;

class InstructionSetTest {

  private static final Map<Integer, String> RESULT = Map.ofEntries(
      entry(0x1, "A"),
      entry(0x2, "B"),
      entry(0x3, "C"),
      entry(0x5, "C")
  );

  @Test
  void serach() {
    assertThat(RESULT.containsKey(5)).isTrue();
  }

  @Test
  void name() {
    int readByte = 0xa9;
    System.out.printf("readByte = %02x", readByte);
    assertThat(InstructionSet.opcExists(readByte)).isTrue();
  }

  @Test
  void second_test() {
    OperationCode opc = InstructionSet.getOpc(0xA9);
    assertThat(opc.getOpCode()).isEqualTo(OpCode.LDA);
  }

}