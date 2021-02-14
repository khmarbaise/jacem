package com.soebes.emulators;

import java.util.Map;

import static com.soebes.emulators.AddressingMode.absolute;
import static com.soebes.emulators.AddressingMode.absoluteX;
import static com.soebes.emulators.AddressingMode.absoluteY;
import static com.soebes.emulators.AddressingMode.accumulator;
import static com.soebes.emulators.AddressingMode.immediate;
import static com.soebes.emulators.AddressingMode.implied;
import static com.soebes.emulators.AddressingMode.indirect;
import static com.soebes.emulators.AddressingMode.indirectX;
import static com.soebes.emulators.AddressingMode.indirectY;
import static com.soebes.emulators.AddressingMode.relative;
import static com.soebes.emulators.AddressingMode.zeropage;
import static com.soebes.emulators.AddressingMode.zeropageX;
import static com.soebes.emulators.AddressingMode.zeropageY;
import static com.soebes.emulators.OpCode.ADC;
import static com.soebes.emulators.OpCode.AND;
import static com.soebes.emulators.OpCode.ASL;
import static com.soebes.emulators.OpCode.BCC;
import static com.soebes.emulators.OpCode.BCS;
import static com.soebes.emulators.OpCode.BEQ;
import static com.soebes.emulators.OpCode.BIT;
import static com.soebes.emulators.OpCode.BMI;
import static com.soebes.emulators.OpCode.BNE;
import static com.soebes.emulators.OpCode.BPL;
import static com.soebes.emulators.OpCode.BRK;
import static com.soebes.emulators.OpCode.BVC;
import static com.soebes.emulators.OpCode.BVS;
import static com.soebes.emulators.OpCode.CLC;
import static com.soebes.emulators.OpCode.CLD;
import static com.soebes.emulators.OpCode.CLI;
import static com.soebes.emulators.OpCode.CLV;
import static com.soebes.emulators.OpCode.CMP;
import static com.soebes.emulators.OpCode.CPX;
import static com.soebes.emulators.OpCode.CPY;
import static com.soebes.emulators.OpCode.DEC;
import static com.soebes.emulators.OpCode.DEX;
import static com.soebes.emulators.OpCode.DEY;
import static com.soebes.emulators.OpCode.EOR;
import static com.soebes.emulators.OpCode.INC;
import static com.soebes.emulators.OpCode.INX;
import static com.soebes.emulators.OpCode.INY;
import static com.soebes.emulators.OpCode.JMP;
import static com.soebes.emulators.OpCode.JSR;
import static com.soebes.emulators.OpCode.LDA;
import static com.soebes.emulators.OpCode.LDX;
import static com.soebes.emulators.OpCode.LDY;
import static com.soebes.emulators.OpCode.LSR;
import static com.soebes.emulators.OpCode.NOP;
import static com.soebes.emulators.OpCode.ORA;
import static com.soebes.emulators.OpCode.PHA;
import static com.soebes.emulators.OpCode.PHP;
import static com.soebes.emulators.OpCode.PLA;
import static com.soebes.emulators.OpCode.PLP;
import static com.soebes.emulators.OpCode.ROL;
import static com.soebes.emulators.OpCode.ROR;
import static com.soebes.emulators.OpCode.RTI;
import static com.soebes.emulators.OpCode.RTS;
import static com.soebes.emulators.OpCode.SBC;
import static com.soebes.emulators.OpCode.SEC;
import static com.soebes.emulators.OpCode.SED;
import static com.soebes.emulators.OpCode.SEI;
import static com.soebes.emulators.OpCode.STA;
import static com.soebes.emulators.OpCode.STX;
import static com.soebes.emulators.OpCode.STY;
import static com.soebes.emulators.OpCode.TAX;
import static com.soebes.emulators.OpCode.TAY;
import static com.soebes.emulators.OpCode.TSX;
import static com.soebes.emulators.OpCode.TXA;
import static com.soebes.emulators.OpCode.TXS;
import static com.soebes.emulators.OpCode.TYA;
import static com.soebes.emulators.OperationCode.of;
import static java.util.Map.entry;

public class InstructionSet {


  //@formatter:off
  private static final Map<Integer, OperationCode> opcodes = Map.ofEntries(
      // NOP
      entry(0xEA, of(NOP, implied, 1, 2)),

      // Set instruction
      entry(0x38, of(SEC, implied, 1, 2)),
      entry(0xF8, of(SED, implied, 1, 2)),
      entry(0x78, of(SEI, immediate, 1, 2)),

      // Clear instructions
      entry(0x18, of(CLC, implied, 1, 2)),
      entry(0xD8, of(CLD, implied, 1, 2)),
      entry(0x58, of(CLI, implied, 1, 2)),
      entry(0xB8, of(CLV, implied, 1, 2)),

      // ADC
      entry(0x65, of(ADC, zeropage, 2, 3)),
      entry(0x75, of(ADC, zeropageX, 2, 4)),
      entry(0x6D, of(ADC, absolute, 3, 4)),
      entry(0x7D, of(ADC, absoluteX, 3, 4)),
      entry(0x79, of(ADC, absoluteY, 3, 4)),
      entry(0x61, of(ADC, indirectX, 2, 6)),
      entry(0x71, of(ADC, indirectY, 2, 5)),

      // SBC
      entry(0xE9, of(SBC, immediate, 2, 2)),
      entry(0xE5, of(SBC, zeropage, 2, 3)),
      entry(0xF5, of(SBC, zeropageX, 2, 4)),
      entry(0xED, of(SBC, absolute, 3, 4)),
      entry(0xFD, of(SBC, absoluteX, 3, 4)),
      entry(0xF9, of(SBC, absoluteY, 3, 4)),
      entry(0xE1, of(SBC, indirectX, 2, 6)),
      entry(0xF1, of(SBC, indirectY, 2, 5)),

      // Increments
      entry(0xE8, of(INX, implied, 1, 2)),
      entry(0xC8, of(INY, implied, 1, 2)),

      entry(0xE6, of(INC, zeropage, 2, 5)),
      entry(0xF6, of(INC, zeropageX, 2, 6)),
      entry(0xEE, of(INC, absolute, 3, 6)),
      entry(0xFE, of(INC, absoluteX, 3, 7)),

      // Decrements
      entry(0xCA, of(DEX, implied, 1, 2)),
      entry(0x88, of(DEY, implied, 1, 2)),
      entry(0xC6, of(DEC, zeropage, 2, 5)),
      entry(0xD6, of(DEC, zeropageX, 2, 6)),
      entry(0xCE, of(DEC, absolute, 3, 6)),
      entry(0xDE, of(DEC, absoluteX, 3, 7)),

      // LDA
      entry(0xA9, of(LDA, immediate, 2, 2)),
      entry(0xA5, of(LDA, zeropage, 2, 3)),
      entry(0xB5, of(LDA, zeropageX, 2, 4)),
      entry(0xAD, of(LDA, absolute, 3, 4)),
      entry(0xBD, of(LDA, absoluteX, 3, 4)),
      entry(0xB9, of(LDA, absoluteY, 3, 4)),
      entry(0xA1, of(LDA, indirectX, 2, 6)),
      entry(0xB1, of(LDA, indirectY, 2, 5)),

      // LDX
      entry(0xA2, of(LDX, immediate, 2, 2)),
      entry(0xA6, of(LDX, zeropage, 2, 3)),
      entry(0xB6, of(LDX, zeropageY, 2, 4)),
      entry(0xAE, of(LDX, absolute, 3, 4)),
      entry(0xBE, of(LDX, absoluteY, 3, 4)),

      // LDY
      entry(0xA0, of(LDY, immediate, 2, 2)),
      entry(0xA4, of(LDY, zeropage, 2, 3)),
      entry(0xB4, of(LDY, zeropageX, 2, 4)),
      entry(0xAC, of(LDY, absolute, 3, 4)),
      entry(0xBC, of(LDY, absoluteX, 3, 4)),

      // ORA
      entry(0x09, of(ORA, immediate, 2, 2)),
      entry(0x05, of(ORA, zeropage, 2, 3)),
      entry(0x15, of(ORA, zeropageX, 2, 4)),
      entry(0x0D, of(ORA, absolute, 3, 4)),
      entry(0x1D, of(ORA, absoluteX, 3, 4)),
      entry(0x19, of(ORA, absoluteY, 3, 4)),
      entry(0x01, of(ORA, indirectX, 2, 6)),
      entry(0x11, of(ORA, indirectY, 2, 5)),

      // AND
      entry(0x29, of(AND, immediate, 2, 2)),
      entry(0x25, of(AND, zeropage, 2, 3)),
      entry(0x35, of(AND, zeropageX, 2, 4)),
      entry(0x2D, of(AND, absolute, 3, 4)),
      entry(0x3D, of(AND, absoluteX, 3, 4)),
      entry(0x39, of(AND, absoluteY, 3, 4)),
      entry(0x21, of(AND, indirectX, 2, 6)),
      entry(0x31, of(AND, indirectY, 2, 5)),

      // EOR
      entry(0x49, of(EOR, immediate, 2, 2)),
      entry(0x45, of(EOR, zeropage, 2, 3)),
      entry(0x55, of(EOR, zeropageX, 2, 4)),
      entry(0x4D, of(EOR, absolute, 3, 4)),
      entry(0x5D, of(EOR, absoluteX, 3, 4)),
      entry(0x59, of(EOR, absoluteY, 3, 4)),
      entry(0x41, of(EOR, indirectX, 2, 6)),
      entry(0x51, of(EOR, indirectY, 2, 5)),

      // STA
      entry(0x85, of(STA, zeropage, 2, 3)),
      entry(0x95, of(STA, zeropageX, 2, 4)),
      entry(0x8D, of(STA, absolute, 3, 4)),
      entry(0x9D, of(STA, absoluteX, 3, 5)),
      entry(0x99, of(STA, absoluteY, 3, 5)),
      entry(0x81, of(STA, indirectX, 2, 6)),
      entry(0x91, of(STA, indirectY, 2, 6)),

      // STX
      entry(0x86, of(STX, zeropage, 2, 3)),
      entry(0x96, of(STX, zeropageY, 2, 4)),
      entry(0x8E, of(STX, absolute, 3, 4)),

      // STY
      entry(0x84, of(STY, zeropage, 2, 3)),
      entry(0x94, of(STY, zeropageX, 2, 4)),
      entry(0x8C, of(STY, absolute, 3, 4)),

      // TAX
      entry(0xAA, of(TAX, implied, 1, 2)),

      // TAY
      entry(0xA8, of(TAY, implied, 1, 2)),

      // TXA
      entry(0x8A, of(TXA, implied, 1, 2)),

      // TYA
      entry(0x98, of(TYA, implied, 1, 2)),

      // TSX
      entry(0xBA, of(TSX, implied, 1, 2)),

      // TXS
      entry(0x9A, of(TXS, implied, 1, 2)),

      // ASL
      entry(0x0A, of(ASL, accumulator, 1, 2)),
      entry(0x06, of(ASL, zeropage, 2, 5)),
      entry(0x16, of(ASL, zeropageX, 2, 6)),
      entry(0x0E, of(ASL, absolute, 3, 6)),
      entry(0x1E, of(ASL, absoluteX, 3, 7)),

      // LSR
      entry(0x4A, of(LSR, accumulator, 1, 2)),
      entry(0x46, of(LSR, zeropage, 2, 5)),
      entry(0x56, of(LSR, zeropageX, 2, 6)),
      entry(0x4E, of(LSR, absolute, 3, 6)),
      entry(0x5E, of(LSR, absoluteX, 3, 7)),

      // ROL
      entry(0x2A, of(ROL, accumulator, 1, 2)),
      entry(0x26, of(ROL, zeropage, 2, 5)),
      entry(0x36, of(ROL, zeropageX, 2, 6)),
      entry(0x2E, of(ROL, absolute, 3, 6)),
      entry(0x3E, of(ROL, absoluteX, 3, 7)),

      // ROR
      entry(0x6A, of(ROR, accumulator, 1, 2)),
      entry(0x66, of(ROR, zeropage, 2, 5)),
      entry(0x76, of(ROR, zeropageX, 2, 6)),
      entry(0x6E, of(ROR, absolute, 3, 6)),
      entry(0x7E, of(ROR, absoluteX, 3, 7)),

      // CMP
      entry(0xC9, of(CMP, immediate, 2, 2)),
      entry(0xC5, of(CMP, zeropage, 2, 3)),
      entry(0xD5, of(CMP, zeropageX, 2, 4)),
      entry(0xCD, of(CMP, absolute, 3, 4)),
      entry(0xDD, of(CMP, absoluteX, 3, 4)),
      entry(0xD9, of(CMP, absoluteY, 3, 4)),
      entry(0xC1, of(CMP, indirectX, 2, 6)),
      entry(0xD1, of(CMP, indirectY, 2, 5)),

      // CPX
      entry(0xE0, of(CPX, immediate, 2, 2)),
      entry(0xE4, of(CPX, zeropage, 2, 3)),
      entry(0xEC, of(CPX, absolute, 3, 4)),

      // CPY
      entry(0xC0, of(CPY, immediate, 2, 2)),
      entry(0xC4, of(CPY, zeropage, 2, 3)),
      entry(0xCC, of(CPY, absolute, 3, 4)),

      // BRK
      entry(0x00, of(BRK, implied, 1, 7)),

      // BCC/BCS
      entry(0x90, of(BCC, relative, 2, 2)),
      entry(0xB0, of(BCS, relative, 2, 2)),

      // BNE/BEQ
      entry(0xD0, of(BNE, relative, 2, 2)),
      entry(0xF0, of(BEQ, relative, 2, 2)),

      // BPL/BMI
      entry(0x10, of(BPL, relative, 2, 2)),
      entry(0x30, of(BMI, relative, 2, 2)),

      // BVC/BVS
      entry(0x50, of(BVC, relative, 2, 2)),
      entry(0x70, of(BVS, relative, 2, 2)),

      // BIT
      entry(0x24, of(BIT, zeropage, 2, 3)),
      entry(0x2C, of(BIT, absolute, 3, 4)),

      // PHP
      entry(0x08, of(PHP, implied, 1, 3)),

      // PLP
      entry(0x28, of(PLP, implied, 1, 4)),

      // PHA
      entry(0x48, of(PHA, implied, 1, 3)),

      // PLA
      entry(0x68, of(PLA, implied, 1, 4)),

      // JMP
      entry(0x4C, of(JMP, absolute, 3, 3)),
      entry(0x6C, of(JMP, indirect, 3, 5)),

      // JSR
      entry(0x20, of(JSR, absolute, 3, 6)),

      // RTS
      entry(0x60, of(RTS, implied, 1, 6)),

      // RTI
      entry(0x40, of(RTI, implied, 1, 6)));
  //@formatter:on

  public static OperationCode getOpc(int opcode) {
    return opcodes.get(opcode);
  }

  public static boolean opcExists(int opcode) {
    byte b = Integer.valueOf(opcode).byteValue();
    return opcodes.containsKey(opcode);
  }

}
