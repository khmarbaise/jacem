package com.soebes.cralinkr.cli;

import picocli.CommandLine.Command;

@Command(name = "relocator")
public class Relocator implements Runnable {

  @Override
  public void run() {
    System.out.println("Hello from relocator command.");
  }
}
