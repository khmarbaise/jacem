package com.soebes.emulators.cpu8085;

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

import java.util.Map;

import static com.soebes.emulators.cpu8085.AddressingMode.immediate;
import static com.soebes.emulators.cpu8085.AddressingMode.immediate16;
import static com.soebes.emulators.cpu8085.AddressingMode.implied;
import static com.soebes.emulators.cpu8085.AddressingMode.indirect;
import static com.soebes.emulators.cpu8085.OpCode.LDAX;
import static com.soebes.emulators.cpu8085.OpCode.LXI;
import static com.soebes.emulators.cpu8085.OpCode.MOV;
import static com.soebes.emulators.cpu8085.OpCode.MVI;
import static com.soebes.emulators.cpu8085.OpCode.NOP;
import static com.soebes.emulators.cpu8085.OpCode.RIM;
import static com.soebes.emulators.cpu8085.OpCode.SIM;
import static com.soebes.emulators.cpu8085.OpCode.STAX;
import static com.soebes.emulators.cpu8085.Operation.of;
import static java.util.Map.entry;

/**
 * @author Karl Heinz Marbaise
 */
public class InstructionSet {


  //@formatter:off
  private static final Map<Integer, Operation> opcodes = Map.ofEntries(
      // NOP
      entry(0x00, of(NOP, implied, 1, 4)),

      // MOV A,R
      entry(0x7F, of( MOV, implied, 1, 4)), //A
      entry(0x78, of( MOV, implied, 1, 4)), //B
      entry(0x79, of( MOV, implied, 1, 4)), //C
      entry(0x7A, of( MOV, implied, 1, 4)), //D
      entry(0x7B, of( MOV, implied, 1, 4)), //E
      entry(0x7C, of( MOV, implied, 1, 4)), //H
      entry(0x7D, of( MOV, implied, 1, 4)), //L
      entry(0x7E, of( MOV, implied, 1, 7)), //M
      // MOV B,R
      entry(0x47, of(MOV, implied, 1, 4)), //A
      entry(0x40, of(MOV, implied, 1, 4)), //B
      entry(0x41, of(MOV, implied, 1, 4)), //C
      entry(0x42, of(MOV, implied, 1, 4)), //D
      entry(0x43, of(MOV, implied, 1, 4)), //E
      entry(0x44, of(MOV, implied, 1, 4)), //H
      entry(0x45, of(MOV, implied, 1, 4)), //L
      entry(0x46, of(MOV, implied, 1, 7)), //M
      // MOV C,R
      entry(0x4F, of(MOV, implied, 1, 4)), //A
      entry(0x48, of(MOV, implied, 1, 4)), //B
      entry(0x49, of(MOV, implied, 1, 4)), //C
      entry(0x4A, of(MOV, implied, 1, 4)), //D
      entry(0x4B, of(MOV, implied, 1, 4)), //E
      entry(0x4C, of(MOV, implied, 1, 4)), //H
      entry(0x4D, of(MOV, implied, 1, 4)), //L
      entry(0x4E, of(MOV, implied, 1, 7)), //M
      // MOV D,R
      entry(0x57, of(MOV, implied, 1, 4)), //A
      entry(0x50, of(MOV, implied, 1, 4)), //B
      entry(0x51, of(MOV, implied, 1, 4)), //C
      entry(0x52, of(MOV, implied, 1, 4)), //D
      entry(0x53, of(MOV, implied, 1, 4)), //E
      entry(0x54, of(MOV, implied, 1, 4)), //H
      entry(0x55, of(MOV, implied, 1, 4)), //L
      entry(0x56, of(MOV, implied, 1, 7)), //M
      // MOV E,R
      entry(0x5F, of(MOV, implied, 1, 4)), //A
      entry(0x58, of(MOV, implied, 1, 4)), //B
      entry(0x59, of(MOV, implied, 1, 4)), //C
      entry(0x5A, of(MOV, implied, 1, 4)), //D
      entry(0x5B, of(MOV, implied, 1, 4)), //E
      entry(0x5C, of(MOV, implied, 1, 4)), //H
      entry(0x5D, of(MOV, implied, 1, 4)), //L
      entry(0x5E, of(MOV, implied, 1, 7)), //M
      // MOV H,R
      entry(0x67, of(MOV, implied, 1, 4)), //A
      entry(0x60, of(MOV, implied, 1, 4)), //B
      entry(0x61, of(MOV, implied, 1, 4)), //C
      entry(0x62, of(MOV, implied, 1, 4)), //D
      entry(0x63, of(MOV, implied, 1, 4)), //E
      entry(0x64, of(MOV, implied, 1, 4)), //H
      entry(0x65, of(MOV, implied, 1, 4)), //L
      entry(0x66, of(MOV, implied, 1, 7)), //M
      // MOV L,R
      entry(0x6F, of(MOV, implied, 1, 4)), //A
      entry(0x68, of(MOV, implied, 1, 4)), //B
      entry(0x69, of(MOV, implied, 1, 4)), //C
      entry(0x6A, of(MOV, implied, 1, 4)), //D
      entry(0x6B, of(MOV, implied, 1, 4)), //E
      entry(0x6C, of(MOV, implied, 1, 4)), //H
      entry(0x6D, of(MOV, implied, 1, 4)), //L
      entry(0x6E, of(MOV, implied, 1, 7)), //M
      // MOV M,R
      entry(0x77, of(MOV, implied, 1, 7)), //A
      entry(0x70, of(MOV, implied, 1, 7)), //B
      entry(0x71, of(MOV, implied, 1, 7)), //C
      entry(0x72, of(MOV, implied, 1, 7)), //D
      entry(0x73, of(MOV, implied, 1, 7)), //E
      entry(0x74, of(MOV, implied, 1, 7)), //H
      entry(0x75, of(MOV, implied, 1, 7)), //L
      entry(0x76, of(MOV, implied, 1, 7)), //M

      // MVI R, #value
      // FIXME: immediate => load 8 bit value from memory.
      entry(0x3E, of(MVI, immediate, 2, 7)),  //A
      entry(0x06, of(MVI, immediate, 2, 7)),  //B
      entry(0x0E, of(MVI, immediate, 2, 7)),  //C
      entry(0x16, of(MVI, immediate, 2, 7)),  //D
      entry(0x1E, of(MVI, immediate, 2, 7)),  //E
      entry(0x26, of(MVI, immediate, 2, 7)),  //H
      entry(0x2E, of(MVI, immediate, 2, 7)),  //L
      entry(0x36, of(MVI, immediate, 2, 10)), //M

      // LXI R, #value
      //FIXME: immediate? => load 16 bit value from memory.
      entry(0x01, of(LXI, immediate16, 3, 10)), //LXI B,
      entry(0x11, of(LXI, immediate16, 3, 10)), //LXI D,
      entry(0x21, of(LXI, immediate16, 3, 10)), //LXI H,
      entry(0x31, of(LXI, immediate16, 3, 10)), //LXI SP,

      // LDAX R
      // Load value into A from memory address defined by given register.
      entry(0x0A, of(LDAX, indirect, 3, 7)), //LDAX B
      entry(0x1A, of(LDAX, indirect, 3, 7)), //LDAX D
      // STAX R
      // Store value of A into memory address defined by given register.
      entry(0x02, of(STAX, indirect, 3, 7)), //LDAX B
      entry(0x12, of(STAX, indirect, 3, 7)), //LDAX D

      // 8085 only.
      entry(0x20, of(RIM, indirect, 1, 7)),
      entry(0x30, of(SIM, indirect, 1, 7))

  );
  //@formatter:on

  public static Operation getOpc(int opcode) {
    return opcodes.get(opcode);
  }

  public static boolean opcExists(int opcode) {
    return opcodes.containsKey(opcode);
  }

}
