package com.soebes.emulators;

import org.junit.jupiter.api.Test;

class AppClassTest {

  @Test
  void first_test() {
//    SequenceLayout intArrayLayout = MemoryLayout
//        .ofSequence(64*1024, MemoryLayout.ofValueBits(8, ByteOrder.nativeOrder()));
//
//    System.out.println(" = " + intArrayLayout.byteSize());

    /*
    SequenceLayout byteArrayLayout
        = MemoryLayout.ofSequence(64*1024,
        MemoryLayout.ofValueBits(8,
            ByteOrder.nativeOrder()));

    VarHandle indexedElementHandle
        = byteArrayLayout.varHandle(byte.class,
        PathElement.sequenceElement());

    try (MemorySegment segment = MemorySegment.allocateNative(intArrayLayout)) {
      for (int i = 0; i < intArrayLayout.elementCount().getAsLong(); i++) {
        indexedElementHandle.set(segment, (long) i, i);
      }
    }
    /*
     */
    //ValueLayout valueLayout = ValueLayout.(ByteOrder.LITTLE_ENDIAN, 64 * 1024).withBitAlignment(1L);

  }
}
