package com.jdf.statemachine_demo.state;

public enum OrderStates {
  UNPAID, // 待支付
  WAITING_FOR_RECEIVE, // 待收货
  DONE // 结束
}
