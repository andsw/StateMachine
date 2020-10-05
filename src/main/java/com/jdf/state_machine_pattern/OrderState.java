package com.jdf.state_machine_pattern;

public abstract class OrderState {

  protected Context context;

  public void setContext(Context context) {
    this.context = context;
  }

  public abstract void pay();

  public abstract void receive();
}
