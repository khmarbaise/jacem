package com.soebes.emulators.cpu6502.memory;

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
public interface Memory {

  /**
   * @return The size of the memory.
   */
  int Size();
  /**
   * @param address The address within the memory.
   * @return a single byte read from the given memory location.
   */
  Byte readByte(int address);

  /**
   * @param address The address within the memory.
   * @param value write a single byte at the given location in memory.
   */
  void writeByte(int address, Byte value);

}
