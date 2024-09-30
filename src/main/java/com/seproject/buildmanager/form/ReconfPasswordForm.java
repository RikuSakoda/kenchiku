package com.seproject.buildmanager.form;

import com.seproject.buildmanager.validation.UniqueLoginCd;
import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ReconfPasswordForm {
  @NotBlank(message = "ログインコードは必須項目です", groups = ValidationGroups.Registration.class)
  @Size(min = 8, max = 15, message = "ログインコードは8文字以上、15文字以内で入力してください",
      groups = ValidationGroups.Registration.class)
  @UniqueLoginCd(message = "ログインコードは既に使用されています", groups = ValidationGroups.Registration.class)
  @Pattern(regexp = "[a-zA-Z0-9]*", message = "ログインコードは半角英数字で入力してください",
      groups = ValidationGroups.Registration.class)
  private String loginCd;
}
