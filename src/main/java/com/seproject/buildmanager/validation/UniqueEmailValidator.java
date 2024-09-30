package com.seproject.buildmanager.validation;

import org.springframework.beans.factory.annotation.Autowired;
import com.seproject.buildmanager.repository.MstUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// バリデータークラス<A,B> Aにはアノテーションクラスを指定する
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

  @Autowired
  private MstUserRepository mstUserRepository;

  @Override
  public void initialize(UniqueEmail constraintAnnotation) {}

  // isValidクラス : バリテーションのエラー判定をする 第一引数のStringはおそらく判定の対象になる文字列
  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    return email != null && !mstUserRepository.existsByEmail(email);
  }
}
