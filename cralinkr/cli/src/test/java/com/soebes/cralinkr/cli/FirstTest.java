package com.soebes.cralinkr.cli;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

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

class FirstTest {

  private Path a65 = Path.of("target", "test-classes", "first.a65");

  @Test
  void readalllines_with_supplemental() throws IOException {
    List<String> lines = Files.readAllLines(a65);

    List<SourceLine> collect = IntStream.range(1, lines.size())
        .mapToObj(i -> new SourceLine(i, lines.get(i)))
        .toList();
    collect.forEach(s -> System.out.printf("%6d %s\n", s.getLineNumber(), s.getLine()));
  }


}
