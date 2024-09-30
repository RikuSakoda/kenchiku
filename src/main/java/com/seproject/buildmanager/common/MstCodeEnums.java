package com.seproject.buildmanager.common;

// テーブルのMstCodeのcode_kindカラムに対応したEnumです
public enum MstCodeEnums {
  INDIVIDUAL_CORPORATE(1),
  INDIVIDUAL_ACTIVATE(2),
  PREFECTURES(3),
  SITUATION_STATUS(4),
  UNIT(5),
  TASK_SUBSTANCE(6),
  SUPPLIER(7),
  INDIVIDUAL_COMPLETE(8),
  DO_EMAIL(9),
  SELECT_SUPPLIER(10),
  ACCEPTING_ORDERS_STATUS(11),
  ORDER_STATUS(12);
  
  private int value;
  
  private MstCodeEnums(int value) {
    this.value = value;
  }
  
  public int getValue()
  {
    return value;
  }
}
