package com.jdf.state_machine_pattern;

public class OrderUnpayState extends OrderState {

  @Override
  public void pay() {
    this.context.setState(Context.UNRECEIVED_STATE);
    System.out.println("---订单已提交，等待商家接单---");
  }

  @Override
  public void receive() {
    System.out.println("---[warning]订单未提交，不可接收---");
  }
}
