package com.seproject.buildmanager.form;

import java.time.LocalDateTime;
import com.seproject.buildmanager.validation.UniqueEmail;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstSupplierManagementForm {

  private Integer id;// id

  @NotBlank(message = "種別は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String classification; // 種別

  @NotBlank(message = "業者・仕入先名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 50, message = "業者・仕入先名は50文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String venderName; // 業者・仕入先名

  @NotBlank(message = "郵便番号は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String postCode; // 郵便番号

  @NotBlank(message = "都道府県は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String prefectures; // 都道府県

  @NotBlank(message = "住所は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 100, message = "住所は100文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String address; // 住所

  private String buildingName; // 建物名

  @Pattern(regexp = "[0-9-]+", message = "電話番号が正しくありません",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @NotBlank(message = "電話番号は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String phone; // 電話番号

  @Pattern(regexp = "[0-9-]+|",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String faxNum1; // FAX番号1

  @NotBlank(message = "メールアドレスは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Email(message = "@を持つメールアドレスの形式で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 100, message = "メールアドレスは100文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @UniqueEmail(message = "メールアドレスは既に使用されています",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String email; // メールアドレス

  @NotBlank(message = "担当部署は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String department; // 担当部署

  @NotBlank(message = "担当者は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String staffName; // 担当者名

  private LocalDateTime createdAt; // 登録日

  private LocalDateTime updatedAt; // 変更日

  private String updatedMstUserId; // ログインユーザーID

  private String status;

  private String transactionToken;

  // 検索用
  private String creatdat;

  private String updatedAt1;



}
