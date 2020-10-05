package com.jdf.state_machine_pattern;

public class OrderReceivedState extends OrderState {

  @Override
  public void pay() {
    System.out.println("---[warning]订单已被接收，不可再提交---");
  }

  @Override
  public void receive() {
    System.out.println("---[warning]不可重复接受订单---");
  }
}
