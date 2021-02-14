package com.soebes.emulators.cpu8085;

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

import static java.util.Map.entry;

/**
 * @author Karl Heinz Marbaise
 */
public class InstructionSet {


  //@formatter:off
  private static final Map<Integer, OperationCode> opcodes = Map.ofEntries(
      // LDA 2400H
      entry(0xEA, OperationCode.of(OpCode.LDA, AddressingMode.absolute, 1, 2))
  );
  //@formatter:on

  public static OperationCode getOpc(int opcode) {
    return opcodes.get(opcode);
  }

  public static boolean opcExists(int opcode) {
    return opcodes.containsKey(opcode);
  }

}
