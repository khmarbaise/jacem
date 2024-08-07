package com.soebes.emulators.common.register;

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
public class Register16Bit {

  private int value;

  public Register16Bit(int value) {
    this.value = value;
  }

  public void incrementBy(int value) {
    this.value += value;
  }

  public int value() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setLv(int low) {
    this.value = (this.value & 0xff00) | (low & 0xff);
  }

  public void setHv(int high) {
    this.value = (this.value & 0x00ff) | ((high & 0xff) << 8);
  }

  public int getLv() {
    return this.value & 0xff;
  }

  public int getHv() {
    return (this.value & 0xff00) >> 8;
  }
}
