package com.seproject.buildmanager.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.common.Constants;
import com.seproject.buildmanager.config.LoginUserDetails;

/**
 * 画面を問わないビジネスロジックを持つService
 * 
 * 変更履歴： 2024/10/31 - 初版作成
 * 
 * @since 1.0
 * @version 1.0
 */
@Service
public class CommonService {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  /**
   * 現在ログインしているユーザのIDを所得
   * 
   * @return 現在ログインしているユーザのID
   */
  public Integer getLoginUserId() {

    Integer tmp = 0;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof LoginUserDetails) {
      LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
      tmp = userDetails.getUserId();
    }

    logger.info("CommonService.getLoginUserId userId : {}", tmp.toString());

    return tmp;

  }

  public String getLoginUserName() {

    String tmp = "";

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof LoginUserDetails) {
      LoginUserDetails userDetails = (LoginUserDetails) authentication.getPrincipal();
      tmp = userDetails.getName();
    }

    logger.info("CommonService.getLoginUserId userId : {}", tmp.toString());

    return tmp;

  }


  /**
   * @param date
   * @return
   */
  public String getDatetimeLocalFormatString(LocalDateTime date) {

    // 出力フォーマットを定義
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATETIME_LOCAL_FORMAT);

    String formattedDate = date.format(formatter);

    logger.info("CommonService.getDatetimeLocalFormatString formattedDate : {}", formattedDate);

    return formattedDate;

  }
}
