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

/**
 * @author Karl Heinz Marbaise
 */
public enum OpCodeMnemonic {
  /**
   * Add with carry
   */
  ADC,
  /**
   * And with Accumulator
   */
  AND,
  /**
   * Arithmetic shift left (shifts in a zero bit on the right)
   */
  ASL,
  /**
   * Branch on carry clear
   */
  BCC,
  /**
   * Branch on carry set.
   */
  BCS,
  /**
   * branch on equal (zero set).
   */
  BEQ,
  /**
   * bit test (accumulator & memory).
   */
  BIT,
  /**
   * Branch on minus (negative set)
   */
  BMI,
  /**
   * Branch not equal (zero clear)
   */
  BNE,
  /**
   * Branch on plus (negative clear)
   */
  BPL,
  /**
   * Break
   */
  BRK,
  /**
   * Branch on overflow clear
   */
  BVC,
  /**
   * Branch on overflow set.
   */
  BVS,
  /**
   * Clear Carry.
   */
  CLC,
  CLD,
  CLI,
  CLV,
  CMP,
  CPX,
  CPY,
  DEC,
  DEX,
  DEY,
  EOR,
  INC,
  INX,
  INY,
  JMP,
  JSR,
  LDA,
  LDX,
  LDY,
  LSR,
  NOP,
  ORA,
  PHA,
  PHP,
  PLA,
  PLP,
  ROL,
  ROR,
  RTI,
  RTS,
  SBC,
  SEC,
  SED,
  SEI,
  STA,
  STX,
  STY,
  TAX,
  TAY,
  TSX,
  TXA,
  TXS,
  TYA,
}
