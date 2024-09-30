package com.seproject.buildmanager.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * メールの本文をテキストファイルから読み込む
 * 
 * @author suzuki
 * 
 */
@Service
public class MailTemplateService {


  public String CreateTemplate(Map<String, Object> variables) {
    return importMail("/sample.txt", variables);
  }

  public String CreateTemplate(String mailFilePath, Map<String, Object> variables) {
    return importMail(mailFilePath, variables);
  }

  /**
   * テキストファイルから読み込み
   * 
   * @param variables
   * @return text
   */
  private String importMail(String mailFilePath, Map<String, Object> variables) {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setTemplateMode(TemplateMode.TEXT); // テンプレートファイルの形式
    templateResolver.setCharacterEncoding("UTF-8"); // エンコード形式

    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setTemplateResolver(templateResolver);

    Context context = new Context();

    context.setVariables(variables);

    // process pathのファイルをStringに変換
    String text = engine.process(mailFilePath, context);
    return text;

  }
}
