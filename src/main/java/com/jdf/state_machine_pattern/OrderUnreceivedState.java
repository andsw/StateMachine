package com.jdf.state_machine_pattern;

public class OrderUnreceivedState extends OrderState {

  @Override
  public void pay() {
    System.out.println("---[warning]订单已提交，不可重复提交---");
  }

  @Override
  public void receive() {
    this.context.setState(Context.RECEIVED_STATE);
    System.out.println("---商家已接收订单---");
  }
}
