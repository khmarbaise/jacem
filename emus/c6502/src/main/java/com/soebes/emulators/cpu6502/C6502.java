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
  private final Register8Bit registerA;
  /**
   * Process Status Flags
   */
  private final StatusRegister psr;

  public C6502(AddressBus bus) {
    this.bus = bus;
    this.registerA = new Register8Bit(0);
    this.regX = new Register8Bit(0);
    this.regY = new Register8Bit(0);
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
      case NOP -> {}
      case ADC -> adc(instruction);
      case AND -> and(instruction);
      case SBC -> sbc(instruction);
      case SEC -> sec();
      case SED -> sed();
      case SEI -> sei();
      case CLC -> clc();
      case CLD -> cld();
      case CLI -> cli();
      case CLV -> clv();
      case INX -> inx();
      case INY -> iny();
      case INC -> inc(instruction);
      case DEX -> dex();
      case DEY -> dey();
      case DEC -> dec(instruction);
      case LDA -> lda(instruction);
      case LDX -> ldx(instruction);
      case LDY -> ldy(instruction);
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

  private void and(Instruction in) {
    int operand = Byte.toUnsignedInt(resolveOperand(in));
    Integer regA = Byte.toUnsignedInt(registerA.value());

    Integer result = regA & operand;

    registerA.setValue(result & 0xff);

    if (Integer.signum(result.byteValue()) < 0) {
      getPsr().set(Negative);
    }

    if (registerA.value() == 0) {
      getPsr().set(Zero);
    }

  }

  private void adc(Instruction in) {
    int operand = Byte.toUnsignedInt(resolveOperand(in));
    int carryValue = getPsr().isSet(Carry) ? 1 : 0;
    int regA = Byte.toUnsignedInt(registerA.value());

    Integer result;
    if (psr.isDecimal()) {
      BCD bcdRegA = new BCD(regA);
      BCD bcdOperand = new BCD(operand);
      BCD bcdCarry = new BCD(carryValue);
      result = bcdRegA.add(bcdOperand).add(bcdCarry).toInt();
    } else {
      result = regA + operand + carryValue;
    }
    registerA.setValue(result & 0xff);

    boolean partI = ((regA ^ operand) & 0x80) == 0;
    boolean partII = ((regA ^ result) & 0x80) != 0;
    boolean xov = partI && partII;
    if (xov) {
      getPsr().set(Overflow);
    }

    if (Integer.signum(result.byteValue()) < 0) {
      getPsr().set(Negative);
    }

    if (result > 0xff) {
      getPsr().set(Carry);
    } else {
      getPsr().unset(Carry);
    }

    if (registerA.value() == 0) {
      getPsr().set(Zero);
    }

  }

  private void sbc(Instruction in) {
    Byte operand = resolveOperand(in);
    int carryValue = getPsr().isSet(Carry) ? 0 : 1;

    Integer regA = Byte.toUnsignedInt(registerA.value());

    Integer result;
    if (psr.isSet(Decimal)) {
      BCD bcdRegA = new BCD(regA);
      BCD bcdOperand = new BCD(operand);
      BCD bcdCarry = new BCD(carryValue);
      result = bcdRegA.sub(bcdOperand).sub(bcdCarry).toInt();
    } else {
      result = regA - Byte.toUnsignedInt(operand) - carryValue;
    }

    registerA.setValue(result.intValue() & 0xff);

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

  private int memoryAddress(Instruction instruction) {
    return switch (instruction.getOpc().getAddressingMode()) {
      case absolute -> instruction.getOp16();
      case absoluteX -> instruction.getOp16() + regX.value();
      case absoluteY -> instruction.getOp16() + regY.value();
      case zeropage, indirectIndexedY -> Byte.toUnsignedInt(instruction.getOp8());
      case zeropageX, indexedIndirectX -> Byte.toUnsignedInt(instruction.getOp8()) + regX.value();
      default -> throw new IllegalStateException("Unknown addressing mode.");
    };
  }

  private byte resolveOperand(Instruction instruction) {
    return switch (instruction.getOpc().getAddressingMode()) {
      case immediate -> instruction.getOp8();
      case indexedIndirectX -> bus.read(bus.read16(memoryAddress(instruction)));
      case indirectIndexedY -> bus.read(bus.read16(memoryAddress(instruction)) + regY.value());
      default -> bus.read(memoryAddress(instruction));
    };
  }

  private Instruction readNextInstruction() {
    Byte opcode = bus.read(pc.value());

    if (!InstructionSet.opcExists(Byte.toUnsignedInt(opcode))) {
      throw new IllegalStateException(String.format("Unknown operation code %04x: %02x ", pc.value(), opcode));
    }

    OperationCode opc = InstructionSet.getOpc(Byte.toUnsignedInt(opcode));

    return switch (opc.getInstructionSize()) {
      case 1, 2 -> new Instruction(opc, bus.read(this.pc.value() + 1), (byte) 0, pc.value() + 1);
      case 3 -> new Instruction(opc, (byte) 0, bus.read16(this.pc.value() + 1), pc.value() + 1);
      default -> throw new IllegalStateException("Unknown instruction size");
    };
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
