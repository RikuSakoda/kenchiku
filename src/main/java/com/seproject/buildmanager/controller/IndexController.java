package com.seproject.buildmanager.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import com.seproject.buildmanager.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Indexページおよび関連機能を提供するコントローラクラスです。
 * 
 * <p>
 * このクラスは、ファイルのアップロード・ダウンロード機能やメール送信機能を提供します。
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
@RequestMapping(value = "/")
public class IndexController {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootApplication.class);

  @Autowired
  private EmailService emailService;

  @Value("${file.upload-dir}")
  private String uploadDir;

  /**
   * インデックスページを表示します。
   * 
   * @param name クエリパラメータ "name" の値。デフォルトは "World"。
   * @return インデックスページのテンプレート名
   */
  @GetMapping({"/"})
  public String index(@RequestParam(value = "name", defaultValue = "World") String name,
      HttpServletResponse response) {
    logger.info("--- IndexController.index START ---");

    logger.info("--- IndexController.index END ---");
    return "index";
  }

  /**
   * HTML形式のメールを送信します。
   * 
   * @return インデックスページのテンプレート名
   */
  @GetMapping("/sendHtmlEmail")
  public String sendHtmlEmail() {
    try {
      emailService.sendHtmlEmail("takano@se-project.co.jp", "BuildManagerタイトルsendHtmlEmail",
          "BODYsendHtmlEmail", null);
    } catch (MessagingException e) {
      e.printStackTrace();
      logger.info(e.getMessage());
    }
    return "index";
  }

  /**
   * シンプルなメールを送信します。
   * 
   * @return インデックスページのテンプレート名
   */
  @GetMapping("/sendEmail")
  public String sendEmail() {
    emailService.sendSimpleEmail("takano@se-project.co.jp", "BuildManagerタイトルsendEmail",
        "BODYsendEmail");
    return "index";
  }

  /**
   * ファイルをアップロードします。
   * 
   * @param file アップロードするファイル
   * @param redirectAttributes リダイレクト時の属性
   * @return インデックスページへのリダイレクトURL
   */
  @PostMapping("/fileupload")
  public String fileupload(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) {
    if (file.isEmpty()) {
      redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
      return "redirect:/";
    }

    try {
      // ファイルを取得してアップロードディレクトリに保存
      byte[] bytes = file.getBytes();
      Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
      Files.write(path, bytes);

      redirectAttributes.addFlashAttribute("message",
          "You successfully uploaded '" + file.getOriginalFilename() + "'");
    } catch (IOException e) {
      e.printStackTrace();
    }

    return "redirect:/";
  }

  /**
   * ファイルをダウンロードします。
   * 
   * @param response HTTPレスポンスオブジェクト
   * @return インデックスページへのリダイレクトURL
   */
  @GetMapping("/download")
  public String downloadFile(HttpServletResponse response) {
    String originFilePath = "/app/uploads/favicon.png";
    String outputFileName = "favicon.png";

    String CONTENT_DISPOSITION_FORMAT = "attachment; filename=\"%s\"; filename*=UTF-8''%s";
    outputFileName = String.format(CONTENT_DISPOSITION_FORMAT, outputFileName,
        UriUtils.encode(outputFileName, StandardCharsets.UTF_8.name()));
    try (OutputStream os = response.getOutputStream()) {
      Path filePath = Path.of(originFilePath);
      byte[] fb = Files.readAllBytes(filePath);
      response.setContentType("application/octet-stream");
      response.setHeader(HttpHeaders.CONTENT_DISPOSITION, outputFileName);
      response.setContentLength(fb.length);
      os.write(fb);
      os.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return "redirect:/";
  }
}
