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
public class MstCustomerForm {

  private Integer id;

  private String cust_kind;

  @NotBlank(message = "法人名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String corpName;

  @NotBlank(message = "法人名カナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Pattern(regexp = "^[ァ-ヶー]+$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "カナ文字で入力してください")
  private String corpKana;

  private String department;

  @NotBlank(message = "姓は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 10, message = "姓は10文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String lName;

  @NotBlank(message = "名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 10, message = "名は10文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String fName;

  @NotBlank(message = "姓カナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 10, message = "姓カナは10文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Pattern(regexp = "^[ァ-ヶー]+$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "カナ文字で入力してください")
  private String lNameKana;

  @NotBlank(message = "名カナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 10, message = "名カナは10文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Pattern(regexp = "^[ァ-ヶー]+$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "カナ文字で入力してください")
  private String fNameKana;

  @Pattern(regexp = "^\\d{3}-\\d{4}$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい郵便番号を表示してください")
  private String zip;

  private String prefectures;// 都道府県

  @NotBlank(message = "住所は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String address1;

  private String address2;

  @Pattern(regexp = "^[0-9-]+$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい電話番号を入力してください")

  private String tel;

  @Pattern(regexp = "^[0-9-]*$",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class},
      message = "正しい電話番号を入力してください")
  private String mobile;


  @NotBlank(message = "メールアドレスは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Email(message = "@を持つメールアドレスの形式で入力してください", groups = ValidationGroups.Registration.class)
  @Size(max = 100, message = "メールアドレスは100文字以内で入力してください",
      groups = ValidationGroups.Registration.class)
  @UniqueEmail(message = "メールアドレスは既に使用されています", groups = ValidationGroups.Registration.class)
  private String mail;

  private boolean mailFlg;

  private String status;

  private String transactionToken;

  private LocalDateTime createdAt; // 表示用

  private LocalDateTime updatedAt; // 表示用

  private String createdAt1;
  private String updatedAt1;



}
