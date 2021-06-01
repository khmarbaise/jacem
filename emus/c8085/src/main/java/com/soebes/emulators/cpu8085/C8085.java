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


import com.soebes.emulators.common.memory.AddressBus;
import com.soebes.emulators.common.register.Register16Bit;
import com.soebes.emulators.common.register.Register8Bit;
import com.soebes.emulators.cpu8085.register.StatusRegister;

/**
 * @author Karl Heinz Marbaise
 */
public class C8085 {

  /**
   * The address bus for access to memory.
   */
  private final AddressBus bus;
  /**
   * The Program Counter.
   */
  private final Register16Bit PC;
  /**
   * Stackpointer
   */
  private final Register16Bit SP;

  private final Register16Bit regBC;
  private final Register16Bit regDE;
  private final Register16Bit regHL;
  /**
   * The accumulator.
   */
  private Register8Bit registerA;
  /**
   * processor status word
   * (Accu + Flags
   */
  private StatusRegister psw;

  /**
   * Create an instance of the CPU with the following components:
   *
   * @param bus
   */
  public C8085(AddressBus bus) {
    this.bus = bus;
    this.registerA = new Register8Bit(0);
    this.regBC = new Register16Bit(0x0000);
    this.regDE = new Register16Bit(0x0000);
    this.regHL = new Register16Bit(0x0000);
    //TODO: This might be wrong. Currently for the tests it works.
    // It would be better to make a Reset() method which
    // loads the correct address from reset vector.
    this.PC = new Register16Bit(0x1000);
    // Initial value?
    this.SP = new Register16Bit(0x1000);
    this.psw = new StatusRegister();
  }

  /**
   * TODO: What do we need to do here?
   * <p>
   * Current assumption is to start at 0x0000 address.
   *
   * @return Current instance.
   */
  public C8085 reset() {
    // reset operation
    return this;
  }

  public C8085 step() {
    Instruction instruction = readNextInstruction();
    PC.incrementBy(instruction.getOpc().getInstructionSize());
    execute(instruction);
    return this;
  }

  private void execute(Instruction instruction) {
    switch (instruction.getOpc().getOpCode()) {
      case NOP:
        break;
      default:
        throw new IllegalStateException("Unknown opcode: '%s'" + instruction.getOpc());
    }
  }


  private int memoryAddress(Instruction instruction) {
    switch (instruction.getOpc().getAddressingMode()) {
      case implied:
        return 0;

      case absolute:
        return instruction.getOp16();
//      case absoluteX:
//        return instruction.getOp16() + regX.value();
//      case absoluteY:
//        return instruction.getOp16() + regY.value();
//      case zeropage:
//        return instruction.getOp8();
//      case zeropageX:
//        return instruction.getOp8() + regX.value();
      default:
        throw new IllegalStateException("Unknown addressing mode.");
    }
  }

  private byte resolveOperand(Instruction instruction) {
    switch (instruction.getOpc().getAddressingMode()) {
      case immediate:
        return instruction.getOp8();
      default:
        return bus.read(memoryAddress(instruction));
    }
  }

  private Instruction readNextInstruction() {
    Byte opcode = bus.read(PC.value());

    if (!InstructionSet.opcExists(Byte.toUnsignedInt(opcode))) {
      throw new IllegalStateException(String.format("Unknown operation code %04x: %02x ", PC.value(), opcode));
    }

    OperationCode opc = InstructionSet.getOpc(Byte.toUnsignedInt(opcode));

    switch (opc.getInstructionSize()) {
      case 1:
      case 2:
        byte read8 = bus.read(this.PC.value() + 1);
        return new Instruction(opc, read8, (byte) 0, PC.value() + 1);
      case 3:
        int read16 = bus.read16(this.PC.value() + 1);
        return new Instruction(opc, (byte) 0, read16, PC.value() + 1);
      default:
        throw new IllegalStateException("Unknown instruction size");
    }
  }

}
