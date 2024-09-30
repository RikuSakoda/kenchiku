package com.seproject.buildmanager.form;

import lombok.Data;

@Data
public class MstCheckItemGroupManagementForm {

  private String id;// id

  private String groupId;// グループid

  private String checkId;// チェックid

  private String customerId;// 顧客id

  private String checkItem;// チェック項目のidを保管
}
