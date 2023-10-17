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
  private final Register16Bit sp;

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
    this.sp = new Register16Bit(0x1000);
    this.psw = new StatusRegister();
  }

  public StatusRegister getPsw() {
    return psw;
  }

  public void setPsw(StatusRegister psw) {
    this.psw = psw;
  }

  public Register8Bit a() {
    return registerA;
  }

  public Register16Bit sp() {
    return sp;
  }

  public Register16Bit bc() {
    return regBC;
  }

  public Register16Bit de() {
    return regDE;
  }

  public Register16Bit hl() {
    return regHL;
  }

  public Register16Bit pc() {
    return PC;
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
    PC.incrementBy(instruction.opc().getInstructionSize());
    execute(instruction);
    return this;
  }

  private void execute(Instruction instruction) {
    switch (instruction.opc().getOpCode()) {
      case NOP:
        break;
      case MOV:
        mov(instruction);
        break;
      case MVI:
        mvi(instruction);
        break;
      case LXI:
        lxi(instruction);
        break;
      default:
        throw new IllegalStateException("Unknown opcode: '%s'" + instruction.opc());
    }
  }


  private void lxi(Instruction instruction) {
    int value = resolveOperand(instruction);
    RegLxi register = RegLxi.values()[(instruction.opCode() & 0b00110000) >> 4];
    switch (register) {
      case B:
        regBC.setValue(value);
        break;
      case D:
        regDE.setValue(value);
        break;
      case H:
        regHL.setValue(value);
        break;
      case SP:
        sp.setValue(value);
        break;
    }

  }

  private void mvi(Instruction instruction) {
    int value = resolveOperand(instruction);
    RegMVI register = RegMVI.values()[(instruction.opCode() & 0b00111000) >> 3];
    switch (register) {
      case B:
        regBC.setHv(value);
        break;
      case C:
        regBC.setLv(value);
        break;
      case D:
        regDE.setHv(value);
        break;
      case E:
        regDE.setLv(value);
        break;
      case H:
        regHL.setHv(value);
        break;
      case L:
        regHL.setLv(value);
        break;
      case M:
        bus.write(regHL.value(), value);
        break;
      case A:
        registerA.setValue(value);
        break;
    }

  }

  private void mov(Instruction instruction) {
    RegMVI dst = RegMVI.values()[(instruction.opCode() & 0b00111000) >> 3];
    RegMVI src = RegMVI.values()[instruction.opCode() & 0b00000111];

    // MOV A,A
    // MOV B,B
    // MOV C,C
    // MOV D,D
    // MOV E,E
    // MOV H,H
    // MOV L,L
    // MOV M,M
    // Results in NOP
    if (dst.equals(src)) {
      return;
    }
    int source = 0;
    switch (src) {
      case B:
        source = regBC.getHv();
        break;
      case C:
        source = regBC.getLv();
        break;
      case D:
        source = regDE.getHv();
        break;
      case E:
        source = regDE.getLv();
        break;
      case H:
        source = regHL.getHv();
        break;
      case L:
        source = regHL.getLv();
        break;
      case M:
        source = bus.read(regHL.value());
        break;
      case A:
        source = registerA.asInt();
        break;
    }
    switch (dst) {
      case B:
        regBC.setHv(source);
        break;
      case C:
        regBC.setLv(source);
        break;
      case D:
        regDE.setHv(source);
        break;
      case E:
        regDE.setLv(source);
        break;
      case H:
        regHL.setHv(source);
        break;
      case L:
        regHL.setLv(source);
        break;
      case M:
        bus.write(regHL.value(), source);
        break;
      case A:
        registerA.setValue(source);
        break;
    }

  }

  private int memoryAddress(Instruction instruction) {
    switch (instruction.opc().getAddressingMode()) {
      case implied:
        return 0;

      case immediate16:
        return instruction.op16();
      case absolute:
        return instruction.op16();
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

  private int resolveOperand(Instruction instruction) {
    switch (instruction.opc().getAddressingMode()) {
      case immediate:
        return instruction.op8();
      case immediate16:
        return instruction.op16();
      default:
        return bus.read(memoryAddress(instruction));
    }
  }

  private Instruction readNextInstruction() {
    Byte opcode = bus.read(PC.value());

    if (!InstructionSet.opcExists(Byte.toUnsignedInt(opcode))) {
      throw new IllegalStateException(String.format("Unknown operation code %04x: %02x ", PC.value(), opcode));
    }

    Operation opc = InstructionSet.getOpc(Byte.toUnsignedInt(opcode));

    switch (opc.getInstructionSize()) {
      case 1:
      case 2:
        byte read8 = bus.read(this.PC.value() + 1);
        return new Instruction(opcode, opc, read8, (byte) 0, PC.value() + 1);
      case 3:
        int read16 = bus.read16(this.PC.value() + 1);
        return new Instruction(opcode, opc, (byte) 0, read16, PC.value() + 1);
      default:
        throw new IllegalStateException("Unknown instruction size");
    }
  }

}
