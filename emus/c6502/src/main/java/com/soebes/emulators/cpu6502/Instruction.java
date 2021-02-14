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
public class Instruction {

  private OperationCode opc;

  private byte Op8;
  private int Op16;
  private int Address;

  public Instruction(OperationCode opc, byte op8, int op16, int address) {
    this.opc = opc;
    Op8 = op8;
    Op16 = op16;
    Address = address;
  }

  public OperationCode getOpc() {
    return opc;
  }

  public byte getOp8() {
    return Op8;
  }

  public int getOp16() {
    return Op16;
  }

  public int getAddress() {
    return Address;
  }
}
