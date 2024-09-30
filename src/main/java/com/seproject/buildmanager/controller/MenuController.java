package com.seproject.buildmanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * メニュー画面の表示を担当するコントローラクラスです。
 * 
 * <p>
 * このクラスは、メニュー画面へのリクエストを処理し、対応するビューを返します。
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
@Controller
public class MenuController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  /**
   * メニュー画面を表示します。
   * 
   * <p>
   * このメソッドは、"/menu" パスへのGETリクエストを処理し、メニュー画面のテンプレート名を返します。
   * 
   * @return メニュー画面のテンプレート名 ("menu")
   */
  @GetMapping({"/menu"})
  public String menu() {
    logger.info("--- MenuController.menu START ---");
    logger.info("--- MenuController.menu END ---");
    return "menu";
  }
}
