package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import java.util.List;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.validation.PropertyNameCheck;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstMatterForm {

  private Integer id; // ID PK

  private String situationStatus;// 状況ステータス

  private String status;// 有効無効ステータス

  private Integer registrationUserId;// 登録ユーザーID FK

  private Integer updateUserId;// 更新ユーザーID FK

  private LocalDateTime registrationDatetime;// 登録日時

  private LocalDateTime updateDatetime;// 更新日時

  @NotNull(message = "顧客名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private Integer customerId;// 顧客ID FK

  private String customerName;// 顧客名


  private Integer propertyId;// 物件ID FK
  @NotBlank(message = "物件名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @PropertyNameCheck(message = "一致する物件名がありませんでした",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String propertyName;// 物件名

  private String propertyAddress;// 物件住所

  private String propertyBuildingName;// 物件建物名

  private String taskSubstance;// 種別

  @NotBlank(message = "案件名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 25, message = "案件名は25文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String matterName;// 案件名

  private LocalDateTime scheduledVisitDatetime;// 訪問予定日時


  private Integer visitId;// 訪問者ID FK


  private String visitName;// 訪問者名

  private String securityDeposit;// 敷金

  private LocalDateTime rentalContractDate;// 賃貸契約日

  private LocalDateTime rentalContractEndDate;// 賃貸契約終了日

  private String facility;// 設備一覧

  private Integer estimatefinalversion;// 見積確定バージョン



  @Valid
  private MstTenantForm mstTenantForm;

  private String transactionToken;

  private String leavingStatus = "退去立ち合い"; // 種別が退去

  private String vist = "未入力";

  private List<MstCode> situationStatusSelectList; // 状況ステータスに応じた選択肢

  @AssertTrue(message = "種別が退去立ち合いの場合は必須入力です", groups = ValidationGroups.Registration.class)
  public boolean isLeavingValid() {
    if (taskSubstance == null || taskSubstance.isEmpty()) {
      return true;
    }
    if (taskSubstance.equals(leavingStatus)) {
      if (scheduledVisitDatetime == null) {
        return false;
      }
    }
    return true;
  }

  @AssertTrue(message = "種別が退去立ち合いの場合は必須入力です", groups = ValidationGroups.Registration.class)
  public boolean isVisitValid() {
    if (taskSubstance == null || taskSubstance.isEmpty()) {
      return true;
    }
    if (taskSubstance.equals(leavingStatus)) {
      if (visitId == null) {
        return false;
      }
    }
    return true;
  }



  // 検索用
  private String visit;
  private String createdAt1;
  private String updatedAt1;

  private Integer selectViewMatterForm;


  private LocalDateTime conStartDate;
  private LocalDateTime conEndDate;


}
