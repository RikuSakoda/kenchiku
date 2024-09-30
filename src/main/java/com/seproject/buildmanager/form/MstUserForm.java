package com.seproject.buildmanager.form;

import com.seproject.buildmanager.validation.UniqueEmail;
import com.seproject.buildmanager.validation.UniqueLoginCd;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * ユーザーの登録および変更時に使用するフォームクラスです。
 * 
 * <p>
 * このクラスは、ユーザーの入力データを保持し、バリデーションを適用します。 各フィールドはユーザーの属性に対応しています。
 * 
 * <p>
 * 変更履歴：
 * <ul>
 * <li>2024/10/31 - 初版作成</li>
 * </ul>
 * 
 * @since 1.0
 * @version 1.0
 */
@Data
public class MstUserForm {

  /**
   * ユーザーの識別子。
   */
  private Integer id;

  /**
   * ログインコード。 必須項目で、最大15文字。
   */
  @NotBlank(message = "ログインコードは必須項目です", groups = ValidationGroups.Registration.class)
  @Size(min = 8, max = 15, message = "ログインコードは8文字以上、15文字以内で入力してください",
      groups = ValidationGroups.Registration.class)
  @UniqueLoginCd(message = "ログインコードは既に使用されています", groups = ValidationGroups.Registration.class)
  @Pattern(regexp = "[a-zA-Z0-9]*", message = "ログインコードは半角英数字で入力してください",
      groups = ValidationGroups.Registration.class)
  private String loginCd;

  /**
   * パスワード。
   */
  @NotBlank(message = "パスワードは必須項目です", groups = ValidationGroups.Registration.class)
  @Size(min = 8, max = 20, message = "パスワードは8文字以上、20文字以内で入力してください",
      groups = ValidationGroups.Registration.class)
  private String password;

  /**
   * ユーザーの役割。
   */
  private String roles;

  /**
   * 姓。 最大20文字。
   */
  @NotBlank(message = "姓は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "姓は20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String lName;

  /**
   * 名。 最大20文字。
   */
  @NotBlank(message = "名は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "名は20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String fName;

  /**
   * 姓のカナ。 最大20文字。
   */
  @NotBlank(message = "姓のカナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "姓のカナは20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String lNameKana;

  /**
   * 名のカナ。 最大20文字。
   */
  @NotBlank(message = "名のカナは必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 20, message = "名のカナは20文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String fNameKana;

  /**
   * 電話番号。 最大14文字。
   */
  @NotBlank(message = "電話番号は必須項目です",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  @Size(max = 14, message = "電話番号は14文字以内で入力してください",
      groups = {ValidationGroups.Registration.class, ValidationGroups.Update.class})
  private String tel;

  /**
   * メールアドレス。 有効な形式で、最大100文字。
   */
  @NotBlank(message = "メールアドレスは必須項目です", groups = ValidationGroups.Registration.class)
  @Email(message = "@を持つメールアドレスの形式で入力してください", groups = ValidationGroups.Registration.class)
  @Size(max = 100, message = "メールアドレスは100文字以内で入力してください",
      groups = ValidationGroups.Registration.class)
  @UniqueEmail(message = "メールアドレスは既に使用されています", groups = ValidationGroups.Registration.class)
  private String email;

  /**
   * ユーザーのステータス。
   */
  private String status;

  /**
   * ユーザーの権限。
   */
  private Integer mstAuthId;

  /**
   * トランザクショントークン。
   */
  private String transactionToken;

  // 検索用
  private String createdAt;
  private String updatedAt;
}
