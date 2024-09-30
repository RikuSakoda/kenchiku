package com.seproject.buildmanager.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.entity.MstCode;
import com.seproject.buildmanager.repository.MstCodeRepository;

@Service
public class MstCodeService {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  private final MstCodeRepository mstCodeRepesitory;

  private MstCodeService(MstCodeRepository mstCodeRepesitory) {
    this.mstCodeRepesitory = mstCodeRepesitory;
  }

  public List<MstCode> getAllCode() {
    logger.info("--- MstCodeService.getAllCode START ---");

    List<MstCode> codes = mstCodeRepesitory.findAll();

    logger.info("--- MstCodeService.getAllCode END ---");

    return codes;
  }

  public List<MstCode> getCodeByKind(Integer codeKind) {
    try {
      return mstCodeRepesitory.findByCodeKind(codeKind);
    } catch (NullPointerException e) {
      return null;
    }
  }

  public MstCode getCodeByKindAndName(String codeName, Integer codeKind) {
    try {
      return mstCodeRepesitory.findByCodeKindAndName(codeKind, codeName).orElse(new MstCode());
    } catch (NullPointerException e) {
      return null;
    }
  }

  public MstCode getCodeByKindAndBranch(Integer code_kind, Integer code_branch_num) {
    try {
      return mstCodeRepesitory.findByCodeKindAndBranchNum(code_kind, code_branch_num)
          .orElse(new MstCode());
    } catch (NullPointerException e) {
      return null;
    }
  }

  public List<MstCode> getCodeByCodebitAndBranch(Integer codeKind, Integer codeBit) {
    try {
      return mstCodeRepesitory.findCodeBitByBranchNum(codeKind, codeBit);
    } catch (NullPointerException e) {
      return null;
    }
  }

  public List<MstCode> getCodeByKindStatus(Integer codeKind) {
    try {
      return mstCodeRepesitory.findByCodeKindStatus(codeKind);
    } catch (NullPointerException e) {
      return null;
    }
  }

}
