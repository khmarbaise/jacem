package com.soebes.emulators;

import com.soebes.emulators.memory.Ram;
import com.soebes.emulators.register.Register16Bit;
import com.soebes.emulators.register.Register8Bit;

public class CPU6502 {

  private final Register8Bit registerX;

  private final Register8Bit registerY;

  /**
   * The Program Counter.
   */
  private final Register16Bit registerPC;

  private final Ram ram;

  /**
   * The accumulator.
   */
  private Register8Bit registerA;

  public CPU6502(Ram ram) {
    this.ram = ram;
    this.registerA = new Register8Bit((byte) 0);
    this.registerX = new Register8Bit((byte) 0);
    this.registerY = new Register8Bit((byte) 0);
    this.registerPC = new Register16Bit((short) 0);
  }

  public CPU6502 step() {

//    instruction = readNextInstruction();
//    registerPC.incrementBy(instuction.size());

    //opTypes[opcode]

    return this;
  }


}
