package com.soebes.emulators.cpu6502.register;

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

import java.util.EnumSet;

import static com.soebes.emulators.cpu6502.register.StatusRegister.Status.Decimal;

/**
 * <pre>
 * NV-BDIZC
 * 00110000
 * </pre>
 * <pre>
 * SR Flags (bit 7 to bit 0):
 *
 * N	....	Negative
 * V	....	Overflow
 * -	....	ignored
 * B	....	Break
 * D	....	Decimal (use BCD for arithmetics)
 * I	....	Interrupt (IRQ disable)
 * Z	....	Zero
 * C	....	Carry
 * </pre>
 *
 * @author Karl Heinz Marbaise
 */
public class StatusRegister {

  private EnumSet<Status> statuses;

  public StatusRegister(EnumSet<Status> statuses) {
    this.statuses = statuses;
  }

  public StatusRegister() {
    this.statuses = EnumSet.noneOf(Status.class);
  }

  public boolean isDecimal() {
    return isSet(Decimal);
  }

  public boolean isSet(Status status) {
    return this.statuses.contains(status);
  }

  public boolean isNotSet(Status status) {
    return !this.statuses.contains(status);
  }

  public byte value(Status status) {
    if (this.statuses.contains(status)) {
      return 1;
    }
    return 0;
  }

  public StatusRegister unset(Status status) {
    this.statuses.remove(status);
    return this;
  }

  public StatusRegister set(Status status) {
    this.statuses.add(status);
    return this;
  }

  /*
   * <pre>
   * NV-BDIZC
   * 00110000
   * </pre>
   * <pre>
   * SR Flags (bit 7 to bit 0):
   *
   * N	....	Negative
   * V	....	Overflow
   * -	....	ignored
   * B	....	Break
   * D	....	Decimal (use BCD for arithmetics)
   * I	....	Interrupt (IRQ disable)
   * Z	....	Zero
   * C	....	Carry
   * </pre>
   */
  public enum Status {
    Negative(7),
    Overflow(6),
    Ignored(5),
    Break(4),
    Decimal(3),
    Interrupt(2),
    Zero(1),
    Carry(0);
    private int bit;

    Status(int bit) {
      this.bit = bit;
    }
  }
}
