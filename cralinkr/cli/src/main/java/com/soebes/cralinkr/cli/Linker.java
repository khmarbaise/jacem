package com.soebes.cralinkr.cli;

import picocli.CommandLine.Command;

@Command(name = "link")
public class Linker implements Runnable {

  @Override
  public void run() {
    System.out.println("Hello from linker command.");
  }
}
