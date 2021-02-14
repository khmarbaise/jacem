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


/**
 *
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
 * @author Karl Heinz Marbaise
 */
public class ArithmeticFlags {

  private boolean carryFlag;
  private boolean zeroFlag;
  private boolean interuptDisable;
  private boolean decimalModeFlag;
  private boolean BreakCommandFlag;
  private boolean overflowFlag;
  private boolean negativeFlag;

  public ArithmeticFlags() {
  }

  public boolean isCarryFlag() {
    return carryFlag;
  }

  public void setCarryFlag(boolean carryFlag) {
    this.carryFlag = carryFlag;
  }

  public boolean isZeroFlag() {
    return zeroFlag;
  }

  public void setZeroFlag(boolean zeroFlag) {
    this.zeroFlag = zeroFlag;
  }

  public boolean isInteruptDisable() {
    return interuptDisable;
  }

  public void setInteruptDisable(boolean interuptDisable) {
    this.interuptDisable = interuptDisable;
  }

  public boolean isDecimalModeFlag() {
    return decimalModeFlag;
  }

  public void setDecimalModeFlag(boolean decimalModeFlag) {
    this.decimalModeFlag = decimalModeFlag;
  }

  public boolean isBreakCommandFlag() {
    return BreakCommandFlag;
  }

  public void setBreakCommandFlag(boolean breakCommandFlag) {
    BreakCommandFlag = breakCommandFlag;
  }

  public boolean isOverflowFlag() {
    return overflowFlag;
  }

  public void setOverflowFlag(boolean overflowFlag) {
    this.overflowFlag = overflowFlag;
  }

  public boolean isNegativeFlag() {
    return negativeFlag;
  }

  public void setNegativeFlag(boolean negativeFlag) {
    this.negativeFlag = negativeFlag;
  }

  public void setValue(int value) {
    setNegativeFlag((value >> 7) == 1);
    setZeroFlag(value == 0);
  }

}
