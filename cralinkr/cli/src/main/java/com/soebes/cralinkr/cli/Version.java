package com.soebes.cralinkr.cli;

import java.util.concurrent.Callable;
import picocli.CommandLine.Command;

@Command(name = "version")
public class Version implements Callable<Integer> {

  @Override
  public Integer call() {
    System.out.println("*** (Cr)oss (A)ssembler (Link)er (R)elocator (CrALinkR) V ***");
    return 0;
  }
}
