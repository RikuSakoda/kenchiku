package com.seproject.buildmanager.common;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

// PDF出力時のクラス
@Component
@Scope("prototype") // 毎回インスタンスを生成し、他クラスと共有しない
public class PDFFileService extends FileService {

  public PDFFileService(@Value("${file.server.address}") String path) {
    super(path);
    // TODO 自動生成されたコンストラクター・スタブ
  }

  public void initializePDF(String fileName) {
    this.setFileName(fileName);
  }

  // PDF出力 現状は白紙のPDFをファイルサーバー上に出力する
  public void writerPDF() {

    try {
      // 空のドキュメントオブジェクトを作成します
      PDDocument document = new PDDocument();

      // 新しいページのオブジェクトを作成します
      PDPage page = new PDPage();
      document.addPage(page);

      // ドキュメントを保存します
      document.save(this.preparationFile());
      document.close();
    } catch (IOException e) {
      // TODO 自動生成された catch ブロック
      e.printStackTrace();
    }
  }
}
