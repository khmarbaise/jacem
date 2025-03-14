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

import java.util.Map;

import static com.soebes.emulators.cpu6502.AddressingMode.absolute;
import static com.soebes.emulators.cpu6502.AddressingMode.absoluteX;
import static com.soebes.emulators.cpu6502.AddressingMode.absoluteY;
import static com.soebes.emulators.cpu6502.AddressingMode.accumulator;
import static com.soebes.emulators.cpu6502.AddressingMode.immediate;
import static com.soebes.emulators.cpu6502.AddressingMode.implied;
import static com.soebes.emulators.cpu6502.AddressingMode.indexedIndirectX;
import static com.soebes.emulators.cpu6502.AddressingMode.indirect;
import static com.soebes.emulators.cpu6502.AddressingMode.indirectIndexedY;
import static com.soebes.emulators.cpu6502.AddressingMode.relative;
import static com.soebes.emulators.cpu6502.AddressingMode.zeropage;
import static com.soebes.emulators.cpu6502.AddressingMode.zeropageX;
import static com.soebes.emulators.cpu6502.AddressingMode.zeropageY;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.ADC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.AND;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.ASL;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BCC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BCS;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BEQ;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BIT;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BMI;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BNE;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BPL;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BRK;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BVC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.BVS;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.CLC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.CLD;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.CLI;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.CLV;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.CMP;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.CPX;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.CPY;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.DEC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.DEX;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.DEY;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.EOR;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.INC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.INX;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.INY;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.JMP;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.JSR;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.LDA;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.LDX;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.LDY;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.LSR;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.NOP;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.ORA;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.PHA;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.PHP;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.PLA;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.PLP;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.ROL;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.ROR;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.RTI;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.RTS;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.SBC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.SEC;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.SED;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.SEI;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.STA;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.STX;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.STY;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.TAX;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.TAY;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.TSX;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.TXA;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.TXS;
import static com.soebes.emulators.cpu6502.OpCodeMnemonic.TYA;
import static com.soebes.emulators.cpu6502.OperationCode.of;
import static java.util.Map.entry;

/**
 * @author Karl Heinz Marbaise
 */
public final class InstructionSet {
  private InstructionSet() {
    // intentional empty.
  }

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
      entry(0x69, of(ADC, immediate, 2, 2)),
      entry(0x65, of(ADC, zeropage, 2, 3)),
      entry(0x75, of(ADC, zeropageX, 2, 4)),
      entry(0x6D, of(ADC, absolute, 3, 4)),
      entry(0x7D, of(ADC, absoluteX, 3, 4)),
      entry(0x79, of(ADC, absoluteY, 3, 4)),
      entry(0x61, of(ADC, indexedIndirectX, 2, 6)),
      entry(0x71, of(ADC, indirectIndexedY, 2, 5)),

      // SBC
      entry(0xE9, of(SBC, immediate, 2, 2)),
      entry(0xE5, of(SBC, zeropage, 2, 3)),
      entry(0xF5, of(SBC, zeropageX, 2, 4)),
      entry(0xED, of(SBC, absolute, 3, 4)),
      entry(0xFD, of(SBC, absoluteX, 3, 4)),
      entry(0xF9, of(SBC, absoluteY, 3, 4)),
      entry(0xE1, of(SBC, indexedIndirectX, 2, 6)),
      entry(0xF1, of(SBC, indirectIndexedY, 2, 5)),

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
      entry(0xA1, of(LDA, indexedIndirectX, 2, 6)),
      entry(0xB1, of(LDA, indirectIndexedY, 2, 5)),

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
      entry(0x01, of(ORA, indexedIndirectX, 2, 6)),
      entry(0x11, of(ORA, indirectIndexedY, 2, 5)),

      // AND
      entry(0x29, of(AND, immediate, 2, 2)),
      entry(0x25, of(AND, zeropage, 2, 3)),
      entry(0x35, of(AND, zeropageX, 2, 4)),
      entry(0x2D, of(AND, absolute, 3, 4)),
      entry(0x3D, of(AND, absoluteX, 3, 4)),
      entry(0x39, of(AND, absoluteY, 3, 4)),
      entry(0x21, of(AND, indexedIndirectX, 2, 6)),
      entry(0x31, of(AND, indirectIndexedY, 2, 5)),

      // EOR
      entry(0x49, of(EOR, immediate, 2, 2)),
      entry(0x45, of(EOR, zeropage, 2, 3)),
      entry(0x55, of(EOR, zeropageX, 2, 4)),
      entry(0x4D, of(EOR, absolute, 3, 4)),
      entry(0x5D, of(EOR, absoluteX, 3, 4)),
      entry(0x59, of(EOR, absoluteY, 3, 4)),
      entry(0x41, of(EOR, indexedIndirectX, 2, 6)),
      entry(0x51, of(EOR, indirectIndexedY, 2, 5)),

      // STA
      entry(0x85, of(STA, zeropage, 2, 3)),
      entry(0x95, of(STA, zeropageX, 2, 4)),
      entry(0x8D, of(STA, absolute, 3, 4)),
      entry(0x9D, of(STA, absoluteX, 3, 5)),
      entry(0x99, of(STA, absoluteY, 3, 5)),
      entry(0x81, of(STA, indexedIndirectX, 2, 6)),
      entry(0x91, of(STA, indirectIndexedY, 2, 6)),

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
      entry(0xC1, of(CMP, indexedIndirectX, 2, 6)),
      entry(0xD1, of(CMP, indirectIndexedY, 2, 5)),

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
    return opcodes.containsKey(opcode);
  }

}
