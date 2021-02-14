package com.soebes.emulators.register;

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

import com.soebes.emulators.memory.MemoryAccess;

/**
 * @author Karl Heinz Marbaise
 */
public class Addressable {

  private final MemoryAccess memoryAccess;
  private final int start;
  private final int end;

  public Addressable(MemoryAccess memoryAccess, int start) {
    this.memoryAccess = memoryAccess;
    this.start = start;
    this.end = start + memoryAccess.Size() - 1;
  }

  public MemoryAccess getMemory() {
    return memoryAccess;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}
