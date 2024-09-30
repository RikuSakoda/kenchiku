package com.seproject.buildmanager.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * メール送信ロジックを持つService
 * 
 * 変更履歴： 2024/10/31 - 初版作成
 * 
 * @since 1.0
 * @version 1.0
 * 
 */
@Service
public class EmailService {
  private final JavaMailSender mailSender;

  private final MailTemplateService mailTemplateService;

  private EmailService(JavaMailSender mailSender, MailTemplateService mailTemplateService) {
    this.mailSender = mailSender;
    this.mailTemplateService = mailTemplateService;
  }

  /**
   * text形式のメールを送信する処理
   * 
   * @param toEmail
   * @param subject
   * @param body
   */
  public void sendSimpleEmail(String toEmail, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(subject);
    message.setText(body);
    message.setFrom("your_email@example.com");

    mailSender.send(message);
  }

  /**
   * htmlメールを送信する処理
   * 
   * @param toEmail
   * @param subject
   * @param htmlBody
   * @param attachments
   * @throws MessagingException
   */
  public void sendHtmlEmail(String toEmail, String subject, String htmlBody, List<File> attachments) // MessagingException
      // 送信失敗時の例外
      throws MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // 第一引数 MimeMessage 第二引数
                                                                         // ファイル送付を許可するかどうか
    // このほかにも、CCやBCCも設定可能
    helper.setTo(toEmail); // 送信先のメールアドレス
    helper.setSubject(subject); // タイトル
    helper.setText(htmlBody, true); // 本文(第一引数
    // 普通の文(第二引数がtrueの場合htmlも), 第二引数
    // htmlを適応するか、または、htmlのテキスト)
    helper.setFrom("your_email@example.com"); // 送信元のメールアドレス
    if (attachments != null) {
      for (File attachment : attachments) {
        // addAttachment : 送付ファイルの設定 第一引数 ファイル名 第二引数 ファイルリソース
        // 送付ファイルの数だけaddAttachmentを実行する
        helper.addAttachment(attachment.getName(), attachment);
      }
    }
    mailSender.send(mimeMessage); // 送信
  }

  /**
   * htmlメールを送信する処理(オーバーロード)
   * 
   * @param toEmail
   * @param subject
   * @param variables
   * @param attachments
   * @throws MessagingException
   */
  public void sendHtmlEmail(String toEmail, String subject, Map<String, Object> variables,
      String filePath, List<File> attachments) throws MessagingException {

    MimeMessage mimeMessage = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

    // メール送信先、件名、テンプレート本文の設定
    helper.setTo(toEmail);
    helper.setSubject(subject);
    helper.setText(mailTemplateService.CreateTemplate(filePath, variables), true);
    helper.setFrom("your_email@example.com");

    // 添付ファイルがある場合、添付処理
    if (attachments != null) {
      for (File attachment : attachments) {
        helper.addAttachment(attachment.getName(), attachment);
      }
    }

    // メール送信
    mailSender.send(mimeMessage);
  }

}
