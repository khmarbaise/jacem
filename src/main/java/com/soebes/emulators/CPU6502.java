package com.soebes.emulators;

import com.soebes.emulators.register.AddressBus;
import com.soebes.emulators.register.Register16Bit;
import com.soebes.emulators.register.Register8Bit;

public class CPU6502 {

  private final Register8Bit registerX;

  private final Register8Bit registerY;

  /**
   * The Program Counter.
   */
  private final Register16Bit registerPC;


  private final AddressBus addressBus;

  /**
   * The accumulator.
   */
  private Register8Bit registerA;

  public CPU6502(AddressBus addressBus) {
    this.addressBus = addressBus;
    this.registerA = new Register8Bit((byte) 0);
    this.registerX = new Register8Bit((byte) 0);
    this.registerY = new Register8Bit((byte) 0);
    this.registerPC = new Register16Bit(0);
  }

  public CPU6502 step() {
    Instruction instruction = readNextInstruction();
    registerPC.incrementBy(instruction.getOpc().getInstructionSize());
    execute(instruction);
    return this;
  }

  private void execute(Instruction instruction) {
    switch (instruction.getOpc().getOpCode()) {
      case LDA:
        byte value = resolveOperand(instruction);
        registerA.setValue(value);
        break;
      default:
    }
  }

  private int memoryAddress(Instruction instruction) {
    switch (instruction.getOpc().getAddressingMode()) {
      case absolute:
        return instruction.getAddress();
      case absoluteX:
        return instruction.getAddress() + registerX.value();
      case absoluteY:
        return instruction.getAddress() + registerY.value();

      default:
        throw new IllegalStateException("Unknown adressing mode.");
    }
  }

  private byte resolveOperand(Instruction instruction) {
    switch (instruction.getOpc().getAddressingMode()) {
      case immediate:
        return instruction.getOp8();
      default:
        return addressBus.read(memoryAddress(instruction));
    }
  }

  private Instruction readNextInstruction() {
    Byte opcode = addressBus.read(registerPC.value());

    if (!InstructionSet.opcExists(Byte.toUnsignedInt(opcode))) {
      throw new IllegalStateException(String.format("Unknown operation code %02x", opcode));
    }

    OperationCode opc = InstructionSet.getOpc(Byte.toUnsignedInt(opcode));

    switch (opc.getInstructionSize()) {
      case 1:
      case 2:
        byte read8 = addressBus.read(this.registerPC.value() + 1);
        return new Instruction(opc, read8, (byte) 0, registerPC.value());
      case 3:
        int read16 = addressBus.read16(this.registerPC.value() + 1);
        return new Instruction(opc, (byte) 0, read16, registerPC.value());
      default:
        throw new IllegalStateException("Unknown instruction size");
    }
  }

  public Register8Bit getRegisterX() {
    return registerX;
  }

  public Register8Bit getRegisterY() {
    return registerY;
  }

  public Register16Bit getRegisterPC() {
    return registerPC;
  }

  public Register8Bit getRegisterA() {
    return registerA;
  }
}
