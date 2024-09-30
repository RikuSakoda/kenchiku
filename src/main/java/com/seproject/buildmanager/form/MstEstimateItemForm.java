package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import lombok.Data;

@Data

public class MstEstimateItemForm {

  private Integer id;// 見積項目ID

  private MstEstimateManagementForm estimateId;// 見積

  private Integer matterId; // 案件ID

  private String constoructionId;// 工事区分分類ID

  private String constructionName;// 工事区分分類名

  private String constoructionClassificationId;// 工事工事区分ID

  private String constructionClassificationName;// 工事区分名

  private String unit;// 単位

  private String volume;// 数量

  private String estimateUnitPrice;// 見積用単価

  private String estimateAmount;// 見積用金額

  private String approvalUnitPrice;// 原状回復工事費用承諾書単価

  private String tenantBurdenRatio;// 入居者負担割合

  private String tenantBurdenAmount;// 入居者負担額

  private String tenantAmount;// 入居者金額

  private String note;// 備考

  private LocalDateTime creatregistrationDatetimeedAt;// 登録日時

  private LocalDateTime latestUpdateDatetime;// 最終更新日時

  private boolean flag;// 見積があるかどうか



}
