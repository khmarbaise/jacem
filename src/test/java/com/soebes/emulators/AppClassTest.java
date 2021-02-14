package com.soebes.emulators;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.junit.jupiter.api.Test;

/**
 * @author Karl Heinz Marbaise
 */
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
