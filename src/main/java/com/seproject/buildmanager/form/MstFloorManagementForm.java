package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MstFloorManagementForm {

  private Integer id;

  private String customerId;

  private String customerName;

  private String ownerId;

  private String ownerName;

  private String floorName;

  @Pattern(regexp = "^[0-9a-zA-Z]+$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "数字とアルファベットで入力してください")
  private String arrangement;

  @Pattern(regexp = "\\d+S", message = "数字のみを入力してください")
  private String rent;

  @NotBlank(message = "敷金は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Pattern(regexp = "\\d+S", message = "数字のみを入力してください")
  private String securityDeposit;

  @Pattern(regexp = "^\\d{3}-\\d{4}$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい郵便番号を表示してください")
  @NotBlank(message = "郵便番号は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String postCode;

  private String prefectures;

  private String address;

  private String buildingName;

  @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{4}$|",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい電話番号を入力してください")
  @NotBlank(message = "電話番号は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String phone;

  @Pattern(regexp = "^0[789]0-\\d{4}-\\d{4}$|",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい携帯電話を入力してください")
  @NotBlank(message = "携帯電話は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String mobilePhone;

  private String area;

  private String status;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String updatedMstUserId;

  private String transactionToken;

  // 検索用
  private String createdAt1;

  private String updatedAt1;

}
