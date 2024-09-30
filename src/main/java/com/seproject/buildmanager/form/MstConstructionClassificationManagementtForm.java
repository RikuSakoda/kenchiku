package com.seproject.buildmanager.form;


import java.time.LocalDateTime;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstConstructionClassificationManagementtForm {

  private Integer id;

  private String costGroupId;

  private String groupName;

  private String custId;

  private String custName;

  @Pattern(regexp = "\\d+S", message = "数字のみを入力してください")
  @NotNull(message = "表示順は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private Integer viewDetail; // 表示順

  private String costUnitId; // 単位

  @NotBlank(message = "作業内容は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 200, message = "作業内容は200文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String costContents; // 作業内容

  @NotBlank(message = "作業内容カナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 200, message = "作業内容カナは200文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Pattern(regexp = "^[ァ-ンヴー]*$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "カナ文字で入力してください")
  private String contentsKana; // 作業内容カナ

  @Pattern(regexp = "\\d+$", message = "数字のみを入力してください")
  @NotNull(message = "単価は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private Integer costPrice; // 原状回復工事費用承諾書用単価

  @Pattern(regexp = "\\d+$", message = "数字のみを入力してください")
  @NotNull(message = "単価は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private Integer costPrice2; // 見積用単価

  private String status; // ステータス

  private String transactionToken;

  private LocalDateTime createAt; // 登録日 表示用

  private LocalDateTime updatedAt; // 更新日 表示用

  private Integer updateUserId;

  // 検索用
  private String createAt1;

  private String updatedAt1;

  private String costPrice1;

  private String costPrice3;


}
