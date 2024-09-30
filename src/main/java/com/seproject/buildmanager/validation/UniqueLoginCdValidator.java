package com.seproject.buildmanager.validation;

import org.springframework.beans.factory.annotation.Autowired;
import com.seproject.buildmanager.repository.MstUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 
 */
public class UniqueLoginCdValidator implements ConstraintValidator<UniqueLoginCd, String> {

  @Autowired
  private MstUserRepository mstUserRepository;

  @Override
  public void initialize(UniqueLoginCd constraintAnnotation) {}

  @Override
  public boolean isValid(String loginCd, ConstraintValidatorContext context) {
    return loginCd != null && !mstUserRepository.existsByLoginCd(loginCd);
  }
}
