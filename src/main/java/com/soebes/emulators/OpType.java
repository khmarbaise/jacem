package com.soebes.emulators;

import static com.soebes.emulators.AddressingMode.absolute;
import static com.soebes.emulators.AddressingMode.absoluteX;
import static com.soebes.emulators.AddressingMode.absoluteY;
import static com.soebes.emulators.AddressingMode.immediate;
import static com.soebes.emulators.AddressingMode.implied;
import static com.soebes.emulators.AddressingMode.indirectX;
import static com.soebes.emulators.AddressingMode.indirectY;
import static com.soebes.emulators.AddressingMode.zeropage;
import static com.soebes.emulators.AddressingMode.zeropageX;
import static com.soebes.emulators.OpCode.ADC;
import static com.soebes.emulators.OpCode.CLC;
import static com.soebes.emulators.OpCode.CLD;
import static com.soebes.emulators.OpCode.CLI;
import static com.soebes.emulators.OpCode.CLV;
import static com.soebes.emulators.OpCode.NOP;
import static com.soebes.emulators.OpCode.SEC;
import static com.soebes.emulators.OpCode.SED;
import static com.soebes.emulators.OpCode.SEI;
import static com.soebes.emulators.OperationCode.of;
import static java.util.Map.entry;

import java.util.Map;

public class OpType {


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
      entry(0x71, of(ADC, indirectY, 2, 5))

  );


}
