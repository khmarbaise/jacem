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
class BCD {
  private int[] digits;

  public BCD(int sum1) {
    //TODO: limit checks?
    this.digits = new int[8];
    convertToDigits(sum1);
  }

  private BCD(int[] digits) {
    this.digits = digits;
  }

  /**
   * Given the following value {@code 0x245} will
   * be represented within the {@code digits} array
   * like this:
   * <pre>
   *     7   6   5   4   3   2   1   0
   *   +---+---+---+---+---+---+---+---+
   *   !   !   !   !   !   ! 2 ! 4 ! 5 !
   *   +---+---+---+---+---+---+---+---+
   * </pre>
   *
   * @param value
   */
  private void convertToDigits(int value) {
    int pos = 0;
    while (value > 0) {
      this.digits[pos++] = value & 0x0f;
      value /= 0x10;
    }
  }

  /**
   * This will add the given {@code sum} to ourself (this)
   * and returning the sum of both.
   *
   * @param sum This will be added to ourself.
   * @return The sum of this+sum (BCD).
   */
  BCD add(BCD sum) {
    int[] result = new int[8];
    int carry = 0;
    for (int i = 0; i < 8; i++) {
      int r = this.digits[i] + sum.digits[i] + carry;
      carry = 0;
      if (r > 9) {
        r += 6;
        carry++;
      }
      result[i] = r & 0x0f;
    }

    //If we have carry == 1 at that point it means we have
    //an overflow.
    //TODO: Need to reconsider if we should throw an exception?
    return new BCD(result);
  }

  int toInt() {
    int result = 0;
    for (int i = 7; i >= 0; i--) {
      result *= 16;
      result += digits[i];
    }
    return result;
  }
}
