package com.seproject.buildmanager.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 
 */
@Constraint(validatedBy = UniqueEmailValidator.class) // @Constraint：バリデーションを実行するクラスを指定する
@Target({ElementType.FIELD}) // @Target ： アノテーションが付与できる場所の指定を行う ElementType.FIELDはフィールドのこと
@Retention(RetentionPolicy.RUNTIME) // @Retention：いつまでアノテーションを残すか設定する
                                    // RetentionPolicy.RUNTIMEはreturnが実行されるまで有効
public @interface UniqueEmail { // クラス名がアノテーション名になる
  String message() default "This email is already registered."; // バリテーションが働いた時のエラーメッセージ

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
