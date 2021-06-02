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

/**
 * immediate, register, direct, indirect, and implied addressing modes.
 *
 * @author Karl Heinz Marbaise
 */
public enum AddressingMode {
  absolute("absolute"),
  implied("implied"),
  immediate("immediate"),
  immediate16("immediate16"),
  indirect("indirect");

  private String info;

  AddressingMode(String info) {
    this.info = info;
  }

  public String getInfo() {
    return info;
  }
}
