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

import com.soebes.emulators.cpu6502.register.AddressBus;
import com.soebes.emulators.cpu6502.register.ArithmeticFlags;
import com.soebes.emulators.cpu6502.register.Register16Bit;
import com.soebes.emulators.cpu6502.register.Register8Bit;

/**
 * @author Karl Heinz Marbaise
 */
public class C6502 {

  /**
   * The Program Counter.
   */
  private final Register16Bit PC;


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
  private ArithmeticFlags psf;

  public C6502(AddressBus bus) {
    this.bus = bus;
    this.registerA = new Register8Bit((byte) 0);
    this.regX = new Register8Bit((byte) 0);
    this.regY = new Register8Bit((byte) 0);
    this.PC = new Register16Bit(0);
    this.psf = new ArithmeticFlags();
  }

  public C6502 step() {
    Instruction instruction = readNextInstruction();
    PC.incrementBy(instruction.getOpc().getInstructionSize());
    execute(instruction);
    return this;
  }

  private void execute(Instruction instruction) {
    switch (instruction.getOpc().getOpCode()) {
      case ADC:
        adc(instruction);
        break;
      case NOP:
        break;
      case LDA:
        lda(instruction);
        break;
      case INX:
        inx(instruction);
        break;
      case SEC:
        psf.setCarryFlag(true);
        break;
      case SED:
        psf.setDecimalModeFlag(true);
        break;
      case SEI:
        psf.setInteruptDisable(true);
        break;
      case CLC:
        psf.setCarryFlag(false);
        break;
      case CLD:
        psf.setDecimalModeFlag(false);
        break;
      case CLI:
        psf.setInteruptDisable(false);
        break;
      case CLV:
        psf.setOverflowFlag(false);
        break;
      default:
    }
  }

  private void lda(Instruction instruction) {
    byte value = resolveOperand(instruction);
    registerA.setValue(value);
    psf.setValue(Byte.toUnsignedInt(value));
  }

  private void inx(Instruction in) {
    regX.incr();
    psf.setValue(Byte.toUnsignedInt(regX.value()));
  }

  private void adc(Instruction in) {
    byte operand = resolveOperand(in);
    boolean carryFlag = getPsf().isCarryFlag();
    if (getPsf().isDecimalModeFlag()) {
      adcDecimal(registerA, operand, carryFlag);
    } else {
      adcNormal(registerA, operand, carryFlag);
    }
  }

  private void adcDecimal(Register8Bit a, byte operand, boolean carryFlag) {

  }

  private void adcNormal(Register8Bit a, byte operand, boolean carryFlag) {

  }

  private int memoryAddress(Instruction instruction) {
    switch (instruction.getOpc().getAddressingMode()) {
      case absolute:
        return instruction.getAddress();
      case absoluteX:
        return instruction.getAddress() + regX.value();
      case absoluteY:
        return instruction.getAddress() + regY.value();

      default:
        throw new IllegalStateException("Unknown adressing mode.");
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
        return new Instruction(opc, read8, (byte) 0, PC.value());
      case 3:
        int read16 = bus.read16(this.PC.value() + 1);
        return new Instruction(opc, (byte) 0, read16, PC.value());
      default:
        throw new IllegalStateException("Unknown instruction size");
    }
  }

  public Register8Bit getRegX() {
    return regX;
  }

  public Register8Bit getRegY() {
    return regY;
  }

  public Register16Bit getPC() {
    return PC;
  }

  public Register8Bit getRegisterA() {
    return registerA;
  }

  public ArithmeticFlags getPsf() {
    return psf;
  }
}
