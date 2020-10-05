package com.jdf.state_machine_pattern;

public class Context {

  public final static OrderState UNPAY_STATE = new OrderUnpayState();
  public final static OrderState UNRECEIVED_STATE = new OrderUnreceivedState();
  public final static OrderState RECEIVED_STATE = new OrderReceivedState();

  {
    UNPAY_STATE.setContext(this);
    UNRECEIVED_STATE.setContext(this);
    RECEIVED_STATE.setContext(this);
    this.state = UNPAY_STATE;
  }

  private OrderState state;

  public OrderState getState() {
    return state;
  }

  public void setState(OrderState state) {
    this.state = state;
  }

  public void pay() {
    this.state.pay();
  }

  public void receive() {
    this.state.receive();
  }
}
