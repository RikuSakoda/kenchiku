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
public class MstOwnerManagementForm {


  private String id;// id

  private String client;// 顧客名

  private String clientId;// 顧客id

  private String individual;// 個人・法人区分

  @NotBlank(message = "オーナー名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 50, message = "最大文字数を超えました。50文字以内で入力してください")
  private String corporation;// オーナー名

  @NotBlank(message = "オーナー名カナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 50, message = "最大文字数を超えました。50文字以内で入力してください")
  private String corporationKana;// オーナー名カナ

  // groupsについて
  // groupsでグループを指定することで、実行するバリデーションをコントロールすることができる
  // 例：下のバリデーションの場合、
  // ControllerでValidationGroups.Registration.classまたは、ValidationGroups.Update.classが指定された時だけバリデーションが実行される
  @Size(max = 50, message = "部署は50文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String department;// 部署

  @NotBlank(message = "姓は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "姓は20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String lName;// 姓

  @NotBlank(message = "名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "名は20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String fName;// 名

  @NotBlank(message = "姓カナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "姓カナは20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String lNameKana;// 姓かな

  @NotBlank(message = "名カナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "名カナは20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String fNameKana;// 名かな

  @Pattern(regexp = "^\\d{3}-\\d{4}$|",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい郵便番号を表示してください")
  @NotBlank(message = "郵便番号は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String postCode;// 郵便番号

  private String prefectures;// 都道府県

  @NotBlank(message = "住所は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 100, message = "住所は100文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String address;// 住所

  private String buildingName;// 建物名


  @Pattern(regexp = "^0[1-9]0-\\d{4}-\\d{4}$|",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい電話番号を入力してください")
  @NotBlank(message = "電話番号は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String phone;// 電話番号

  @Pattern(regexp = "^0[789]0-\\d{4}-\\d{4}$|",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい携帯番号を入力してください")
  private String mobilePhone;// 携帯番号

  @NotBlank(message = "メールアドレスは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Email(message = "@を持つメールアドレスの形式で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 100, message = "メールアドレスは100文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @UniqueEmail(message = "メールアドレスは既に使用されています",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String email;// メールアドレス

  private String status;

  private String transactionToken;

  private LocalDateTime createdAt;// 表示専用登録日

  private LocalDateTime updatedAt;// 表示専用更新日

  // 検索用
  private String createdAt1;
  private String updatedAt1;
}
