package com.soebes.emulators.cpu6502;

public class NotImplementedYetException extends RuntimeException {
  public NotImplementedYetException() {
    super();
  }

  public NotImplementedYetException(String message) {
    super(message);
  }

  public NotImplementedYetException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotImplementedYetException(Throwable cause) {
    super(cause);
  }

  public NotImplementedYetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
