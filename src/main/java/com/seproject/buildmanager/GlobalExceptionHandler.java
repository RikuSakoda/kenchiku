package com.seproject.buildmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * グローバル例外ハンドラークラス。
 * アプリケーション全体で発生する例外をキャッチして処理します。
 * 
 * <p>
 * このクラスは@ControllerAdviceアノテーションを使用して、
 * コントローラーで発生するすべての例外をキャッチします。
 * 例外が発生した場合、エラーメッセージとステータスコードを含む
 * ModelAndViewオブジェクトを返します。
 * </p>
 * 
 * <p>
 * 変更履歴：
 * <ul>
 * <li>2024/10/31 - 初版作成</li>
 * </ul>
 * </p>
 * 
 * @since 1.0
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  /**
   * グローバルな例外を処理するメソッド。
   * 
   * <p>
   * このメソッドは、アプリケーション全体で発生した例外をキャッチし、
   * エラーメッセージとステータスコードを含むModelAndViewオブジェクトを返します。
   * エラーページは "error" という名前のテンプレートを使用します。
   * </p>
   * 
   * @param e 発生した例外
   * @return エラーメッセージとステータスコードを含むModelAndViewオブジェクト
   */
  @ExceptionHandler(Exception.class)
  public ModelAndView handleException(Exception e) {
    logger.info("エラーが発生しました: " + e.getMessage());

    ModelAndView modelAndView = new ModelAndView();
    modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    modelAndView.addObject("error", HttpStatus.INTERNAL_SERVER_ERROR.name());
    modelAndView.addObject("message", e.getMessage());
    
    modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    
    modelAndView.setViewName("error");  // エラーページのテンプレート名を指定

    return modelAndView;
  }
}
