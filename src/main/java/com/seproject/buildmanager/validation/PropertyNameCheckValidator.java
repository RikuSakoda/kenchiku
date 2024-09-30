package com.seproject.buildmanager.validation;

import org.springframework.beans.factory.annotation.Autowired;
import com.seproject.buildmanager.repository.MstFloorManagementRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PropertyNameCheckValidator implements ConstraintValidator<PropertyNameCheck, String> {


  @Autowired
  public MstFloorManagementRepository mstFloorManagementRepository;

  @Override
  public void initialize(PropertyNameCheck constraintAnnotation) {}

  // isValidクラス : バリテーションのエラー判定をする 第一引数のStringはおそらく判定の対象になる文字列
  @Override
  public boolean isValid(String floor, ConstraintValidatorContext context) {
    return floor != null && mstFloorManagementRepository.existsByFloorName(floor);
  }
}
