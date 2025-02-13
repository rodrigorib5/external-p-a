package com.empresa.producta.domain;

public enum DomainStatusMessage {

  COLLECTED, WAITING, ERROR;


  public boolean isError() {
    return this.equals(ERROR);
  }

  public boolean isCollected() {
    return this.equals(COLLECTED);
  }

  public boolean isWaiting() {
    return this.equals(WAITING);
  }

}
