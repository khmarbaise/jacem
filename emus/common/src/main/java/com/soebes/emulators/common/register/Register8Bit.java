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
public class Register8Bit {

  private Byte value;

  public Register8Bit(Integer value) {
    this.value = value.byteValue();
  }

  public Byte value() {
    return this.value;
  }

  public Integer asInt() {
    return Integer.valueOf(this.value);
  }

  public void setValue(Byte value) {
    this.value = value;
  }

  public void setValue(Integer value) {
    this.value = value.byteValue();
  }

  public void incr() {
    Integer result = Integer.valueOf(this.value) + 1;
    this.value = result.byteValue();
  }

  public void decr() {
    Integer result = Integer.valueOf(this.value) - 1;
    this.value = result.byteValue();
  }
}
