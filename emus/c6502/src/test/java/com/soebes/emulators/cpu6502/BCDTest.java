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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author Karl Heinz Marbaise
 */
class BCDTest {

  static Stream<Arguments> subtract() {
    return Stream.of(
        arguments(new BCD(0x1010101), new BCD(0x900), new Result(0x1009201)),
        arguments(new BCD(0x10000000), new BCD(0x1), new Result(0x9999999)),
        arguments(new BCD(0x1000000), new BCD(0x1), new Result(0x999999)),
        arguments(new BCD(0x100000), new BCD(0x1), new Result(0x99999)),
        arguments(new BCD(0x10000), new BCD(0x1), new Result(0x9999)),
        arguments(new BCD(0x1000), new BCD(0x1), new Result(0x999)),
        arguments(new BCD(0x100), new BCD(0x1), new Result(0x99)),
        arguments(new BCD(0x10), new BCD(0x1), new Result(0x9))
    );
  }

  static Stream<Arguments> add() {
    return Stream.of(
        arguments(new BCD(0x12345678), new BCD(0x12345678), new Result(0x24691356)),
        arguments(new BCD(0x459), new BCD(0x459), new Result(0x918)),
        arguments(new BCD(0x450), new BCD(0x450), new Result(0x900)),
        arguments(new BCD(0x9), new BCD(0x1), new Result(0x10))
    );
  }

  @ParameterizedTest(name = "[{index}] {0} - {1} = {2}")
  @MethodSource
  void subtract(BCD minuend, BCD subtrahend, Result result) {
    BCD summand = minuend.sub(subtrahend);
    assertThat(summand.toInt()).isEqualTo(result.value());
  }

  @ParameterizedTest(name = "[{index}] {0} + {1} = {2}")
  @MethodSource
  void add(BCD summand1, BCD summand2, Result summ) {
    BCD summand = summand1.add(summand2);
    assertThat(summand.toInt()).isEqualTo(summ.value());
  }

  @Test
  void convert_to_digits() {
    BCD bcd = new BCD(0x12345678);
    assertThat(bcd.toInt()).isEqualTo(0x12345678);
  }

  @Test
  void chaining_add_calls() {
    BCD bcd1 = new BCD(0x12);
    BCD bcd2 = new BCD(0x18);
    BCD bcd3 = new BCD(0x19);
    assertThat(bcd1.add(bcd2).add(bcd3).toInt()).isEqualTo(0x49);
  }


}
