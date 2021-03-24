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
public final class Constants {

  /**
   * The reset vector contains the address where to jump
   * when a reset is triggered. The address is stored in:
   * <ul>
   *   <li>0xFFFC</li>
   *   <li>0xFFFD</li>
   * </ul>
   */
  public static final int RESET_VECTOR = 0xFFFC;

  /**
   * The IRQ vectors.
   * <ul>
   *   <li>0xFFFE</li>
   *   <li>0xFFFF</li>
   * </ul>
   */
  public static final int IRQ_VECTOR = 0xFFFE;

}
