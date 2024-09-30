package com.seproject.buildmanager.form;

import com.seproject.buildmanager.validation.ValidationGroups;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordForm {

  private String loginCd;

  @NotBlank(message = "パスワードは必須項目です", groups = ValidationGroups.Registration.class)
  // @Size(min = 8, max = 20, message = "パスワードは8文字以上、20文字以内で入力してください",
  // groups = ValidationGroups.Registration.class)
  private String password;

  @NotBlank(message = "パスワードは必須項目です", groups = ValidationGroups.Registration.class)
  // @Size(min = 8, max = 20, message = "パスワードは8文字以上、20文字以内で入力してください",
  // groups = ValidationGroups.Registration.class)
  private String againPassword;

  @AssertTrue(message = "passwordの入力値が一緒ではありません", groups = ValidationGroups.Registration.class)
  public boolean isPasswordValid() {
    if (password == null || password.isEmpty()) {
      return true;
    }
    return password.equals(againPassword);
  }
}
