package com.soebes.cralinkr.cli;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "asm")
public class Assembler implements Runnable {

  @Override
  public void run() {
    System.out.println("Hello from asm command.");
  }
}
