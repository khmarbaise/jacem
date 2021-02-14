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
public class OperationCode {

  private final OpCode opCode;
  private final AddressingMode addressingMode;
  private final int instructionSize;
  private final int cycles;

  private OperationCode(OpCode opCode, AddressingMode addressingMode, int instructionSize, int cycles) {
    this.opCode = opCode;
    this.addressingMode = addressingMode;
    this.instructionSize = instructionSize;
    this.cycles = cycles;
  }

  public static final OperationCode of(OpCode opCode, AddressingMode addressingMode, int instructionSize, int cycles) {
    return new OperationCode(opCode, addressingMode, instructionSize, cycles);
  }

  public OpCode getOpCode() {
    return opCode;
  }

  public AddressingMode getAddressingMode() {
    return addressingMode;
  }

  public int getInstructionSize() {
    return instructionSize;
  }

  public int getCycles() {
    return cycles;
  }
}
