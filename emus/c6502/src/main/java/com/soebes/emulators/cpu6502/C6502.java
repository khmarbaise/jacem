package com.soebes.emulators.cpu6502;

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
import com.soebes.emulators.cpu6502.register.StatusRegister;

import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Carry;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Decimal;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Interrupt;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Negative;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Overflow;
import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Zero;

/**
 * @author Karl Heinz Marbaise
 */
public class C6502 {

  /**
   * The Program Counter.
   */
  private final Register16Bit pc;


  /**
   * The address bus for access to memory.
   */
  private final AddressBus bus;
  /**
   * The X register.
   */
  private final Register8Bit regX;
  /**
   * The Y register.
   */
  private final Register8Bit regY;
  /**
   * The accumulator.
   */
  private Register8Bit registerA;
  /**
   * Process Status Flags
   */
  private StatusRegister psr;

  public C6502(AddressBus bus) {
    this.bus = bus;
    this.registerA = new Register8Bit((byte) 0);
    this.regX = new Register8Bit((byte) 0);
    this.regY = new Register8Bit((byte) 0);
    //TODO: This might be wrong. Currently for the tests it works.
    // It would be better to make a Reset() method which
    // loads the correct address from reset vector.
    this.pc = new Register16Bit(0x1000);
    this.psr = new StatusRegister();
  }

  public C6502 step() {
    Instruction instruction = readNextInstruction();
    pc.incrementBy(instruction.getOpc().getInstructionSize());
    execute(instruction);
    return this;
  }

  private void execute(Instruction instruction) {
    // BiConsumer<C6502, Instruction> adc = C6502::adc;

    switch (instruction.getOpc().getOpCode()) {
      case NOP:
        break;
      case ADC:
        adc(instruction);
        break;
      case SBC:
        sbc(instruction);
        break;
      case SEC:
        sec();
        break;
      case SED:
        sed();
        break;
      case SEI:
        sei();
        break;
      case CLC:
        clc();
        break;
      case CLD:
        cld();
        break;
      case CLI:
        cli();
        break;
      case CLV:
        clv();
        break;

      case INX:
        inx();
        break;
      case INY:
        iny();
        break;
      case INC:
        inc(instruction);
        break;
      case DEX:
        dex();
        break;
      case DEY:
        dey();
        break;
      case DEC:
        dec(instruction);
        break;
      case LDA:
        lda(instruction);
        break;
      case LDX:
        ldx(instruction);
        break;
      case LDY:
        ldy(instruction);
        break;
      default:
        throw new IllegalStateException("Unknown opcode: '%s'" + instruction.getOpc());
    }
  }

  private void clv() {
    psr.unset(Overflow);
  }

  private void cli() {
    psr.unset(Interrupt);
  }

  private void cld() {
    psr.unset(Decimal);
  }

  private void clc() {
    psr.unset(Carry);
  }

  private void sei() {
    psr.set(Interrupt);
  }

  private void sed() {
    psr.set(Decimal);
  }

  private void sec() {
    psr.set(Carry);
  }

  private void lda(Instruction instruction) {
    byte value = resolveOperand(instruction);
    registerA.setValue(value);
    setCarryAndNegativeFlag(value);
  }

  private void ldx(Instruction instruction) {
    byte value = resolveOperand(instruction);
    regX.setValue(value);
  }

  private void ldy(Instruction instruction) {
    byte value = resolveOperand(instruction);
    regY.setValue(value);
  }

  private void sbc(Instruction in) {
    Byte operand = resolveOperand(in);
    int carryValue = getPsr().isSet(Carry) ? 0 : 1;

    Integer regA = Byte.toUnsignedInt(registerA.value());
    Integer result = regA - Byte.toUnsignedInt(operand) - carryValue;

    if (psr.isSet(Decimal)) {
      Integer rl = (regA & 0x0f) - (operand & 0x0f) - carryValue;
      Integer rh = (regA >> 4) - (operand >> 4);
      if ((rl & 0x10) > 0) {
        rl -= 6;
        rh--;
      }
      if ((rh & 0x10) > 0) {
        rh -= 6;
      }
      registerA.setValue((rh << 4) | (rl & 0x0f));
    } else {
      registerA.setValue(result.intValue() & 0xff);
    }

    int overflow = ((regA ^ result) & 0x80) & ((regA ^ operand) & 0x80);
    if (overflow > 0) {
      getPsr().set(Overflow);
    }

    if (Integer.signum(result.byteValue()) < 0) {
      getPsr().set(Negative);
    }

    if (result >= 0) {
      getPsr().set(Carry);
    } else {
      getPsr().unset(Carry);
    }

    if (result == 0) {
      getPsr().set(Zero);
    }
  }


  private void setCarryAndNegativeFlag(byte value) {
    if ((value & 0x80) > 0) {
      psr.set(Negative);
    } else {
      psr.unset(Negative);
    }

    if (value == 0) {
      psr.set(Zero);
    } else {
      psr.unset(Zero);
    }

  }

  private void inx() {
    regX.incr();
    setCarryAndNegativeFlag(regX.value());
  }

  private void dex() {
    regX.decr();
    setCarryAndNegativeFlag(regX.value());
  }

  private void dey() {
    regY.decr();
    setCarryAndNegativeFlag(regY.value());
  }

  private void iny() {
    regY.incr();
    setCarryAndNegativeFlag(regY.value());
  }

  private void inc(Instruction in) {
    int address = memoryAddress(in);
    int value = bus.read(address) + 1;
    bus.write(address, value);
    setCarryAndNegativeFlag(Integer.valueOf(value).byteValue());
  }

  private void dec(Instruction in) {
    int address = memoryAddress(in);
    int value = bus.read(address) - 1;
    bus.write(address, value);
    setCarryAndNegativeFlag(Integer.valueOf(value).byteValue());
  }

  void adc(Instruction in) {
    byte operand = resolveOperand(in);

    if (psr.isDecimal()) {
      adcDecimal(operand);
    } else {
      adcNormal(operand);
    }
  }

  private void adcDecimal(byte operand) {
    this.psr.isSet(Carry);

  }

  private void adcNormal(byte operand) {
    registerA.value();
  }

  private int memoryAddress(Instruction instruction) {
    switch (instruction.getOpc().getAddressingMode()) {
      case absolute:
        return instruction.getOp16();
      case absoluteX:
        return instruction.getOp16() + regX.value();
      case absoluteY:
        return instruction.getOp16() + regY.value();
      case zeropage:
      case indirectIndexedY:
        return Byte.toUnsignedInt(instruction.getOp8());
      case zeropageX:
      case indexedIndirectX:
        return Byte.toUnsignedInt(instruction.getOp8()) + regX.value();
      default:
        throw new IllegalStateException("Unknown addressing mode.");
    }
  }

  private byte resolveOperand(Instruction instruction) {
    switch (instruction.getOpc().getAddressingMode()) {
      case immediate:
        return instruction.getOp8();
      case indexedIndirectX:
        int indirectX = bus.read16(memoryAddress(instruction));
        return bus.read(indirectX);
      case indirectIndexedY:
        int indirectY = bus.read16(memoryAddress(instruction)) + regY.value();
        return bus.read(indirectY);
      default:
        int address = memoryAddress(instruction);
        return bus.read(address);
    }
  }

  private Instruction readNextInstruction() {
    Byte opcode = bus.read(pc.value());

    if (!InstructionSet.opcExists(Byte.toUnsignedInt(opcode))) {
      throw new IllegalStateException(String.format("Unknown operation code %04x: %02x ", pc.value(), opcode));
    }

    OperationCode opc = InstructionSet.getOpc(Byte.toUnsignedInt(opcode));

    switch (opc.getInstructionSize()) {
      case 1:
      case 2:
        byte read8 = bus.read(this.pc.value() + 1);
        return new Instruction(opc, read8, (byte) 0, pc.value() + 1);
      case 3:
        int read16 = bus.read16(this.pc.value() + 1);
        return new Instruction(opc, (byte) 0, read16, pc.value() + 1);
      default:
        throw new IllegalStateException("Unknown instruction size");
    }
  }

  public Register8Bit regX() {
    return regX;
  }

  public Register8Bit regY() {
    return regY;
  }

  public Register16Bit pc() {
    return pc;
  }

  public Register8Bit regA() {
    return registerA;
  }

  public StatusRegister getPsr() {
    return psr;
  }
}
