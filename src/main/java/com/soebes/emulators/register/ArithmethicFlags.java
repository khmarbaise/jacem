package com.soebes.emulators.register;

public class ArithmethicFlags {

  private boolean carryFlag;
  private boolean zeroFlag;
  private boolean interuptDisable;
  private boolean decimalModeFlag;
  private boolean BreakCommandFlag;
  private boolean overflowFlag;
  private boolean negativeFlag;

  public ArithmethicFlags() {
  }

  public boolean isCarryFlag() {
    return carryFlag;
  }

  public void setCarryFlag(boolean carryFlag) {
    this.carryFlag = carryFlag;
  }

  public boolean isZeroFlag() {
    return zeroFlag;
  }

  public void setZeroFlag(boolean zeroFlag) {
    this.zeroFlag = zeroFlag;
  }

  public boolean isInteruptDisable() {
    return interuptDisable;
  }

  public void setInteruptDisable(boolean interuptDisable) {
    this.interuptDisable = interuptDisable;
  }

  public boolean isDecimalModeFlag() {
    return decimalModeFlag;
  }

  public void setDecimalModeFlag(boolean decimalModeFlag) {
    this.decimalModeFlag = decimalModeFlag;
  }

  public boolean isBreakCommandFlag() {
    return BreakCommandFlag;
  }

  public void setBreakCommandFlag(boolean breakCommandFlag) {
    BreakCommandFlag = breakCommandFlag;
  }

  public boolean isOverflowFlag() {
    return overflowFlag;
  }

  public void setOverflowFlag(boolean overflowFlag) {
    this.overflowFlag = overflowFlag;
  }

  public boolean isNegativeFlag() {
    return negativeFlag;
  }

  public void setNegativeFlag(boolean negativeFlag) {
    this.negativeFlag = negativeFlag;
  }
}
