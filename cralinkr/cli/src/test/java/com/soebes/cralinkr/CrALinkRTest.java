package com.soebes.cralinkr;

import com.soebes.cralinkr.cli.CrALinkR;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class CrALinkRTest {

  private PrintStream saveStdErr;
  private PrintStream saveStdOut;
  private PrintStream stdErr;
  private PrintStream stdOut;

  @BeforeEach
  void beforeEach() {
    saveStdErr = System.err;
    saveStdOut = System.out;

    ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
    stdErr = new PrintStream(baosErr);

    ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
    stdOut = new PrintStream(baosOut);

    System.setErr(stdErr);
    System.setOut(stdOut);
  }

  @AfterEach
  void afterEach() {
    System.setErr(saveStdErr);
    System.setOut(saveStdOut);
  }

  @Test
  void name() {
    CrALinkR.main(new String[]{});
    String current = stdOut.toString();
    assertThat(current).isEqualTo("XXXX");
  }
}
