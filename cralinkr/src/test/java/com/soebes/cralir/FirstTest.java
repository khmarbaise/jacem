package com.soebes.cralir;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

class FirstTest {

  private Path a65 = Path.of("target", "test-classes", "first.a65");

  @Test
  void readalllines_with_supplemental() throws IOException {
    List<String> lines = Files.readAllLines(a65);

    List<SourceLine> collect = IntStream.range(1, lines.size())
        .mapToObj(i -> new SourceLine(i, lines.get(i)))
        .collect(toList());
    collect.forEach(s -> System.out.printf("%6d %s\n", s.getLineNumber(), s.getLine()));
  }


}
