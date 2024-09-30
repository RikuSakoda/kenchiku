package com.seproject.buildmanager.form;

import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MstTenantForm {
  private Integer id;// 入居者ID FK

  private String lastName;// 姓

  private String firstName;// 名

  @Pattern(regexp = "^[ァ-タダ-ヶー]*$|", message = "カタカナのみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String lastNameKana;// 姓カナ

  @Pattern(regexp = "^[ァ-タダ-ヶー]*$|", message = "カタカナのみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String firstNameKana;// 名カナ

  @Pattern(regexp = "^\\d{4}$|", message = "４桁の半角数字のみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String areaCode;// 市外局番

  @Pattern(regexp = "^\\d{4}$|", message = "４桁の半角数字のみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String cityCode;// 市内局番

  @Pattern(regexp = "^\\d{1,12}$|", message = "半角数字のみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String telephoneNumber;// 電話番号

  @Pattern(regexp = "^\\d{7}$|", message = "7桁の半角数字のみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String postCode;// 郵便番号


  private String prefectures;// 都道府県

  private String address;// 住所

  private String buildingName;// 建物名

  private String bankName;// 銀行名

  private String bankBranchName;// 銀行支店名

  @Pattern(regexp = "^[a-zA-Z0-9]*$|", message = "半角数字のみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String bankAccountNumber;// 銀行口座番号

  private String bankAccountName;// 銀行口座名義

  @Pattern(regexp = "^[ァ-タダ-ヶー\\s]*$|", message = "カタカナのみで入力してください",
      groups = ValidationGroups.Registration.class)
  private String bankAccountNameKana;// 銀行口座名義カナ

  private String cylinderNumber;// シリンダー番号

  private String note;// 備考

}
