package com.seproject.buildmanager.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * docker上のファイルを読み込み・書き込みする
 */
public class FileService {
  /**
   * docker上のappサーバーのパス
   */
  public String path;
  /**
   * 書き込み用ストリーム
   */
  protected FileOutputStream output = null;

  /**
   * 読み込み用ストリーム
   */
  protected FileInputStream input = null;
  /**
   * ファイル名
   */
  protected String fileName;

  /**
   * コンストラクタ ipアドレス + ファイル名でファイルオブジェクトを生成する
   * 
   * @param fileName ファイル名
   */
  public FileService(String path) {
    this.path = path;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }


  // @Override
  // protected void finalize() throws Throwable {
  // releaseFile(); // オブジェクト解放時にリソースを開放
  // }

  /**
   * ファイルオブジェクト作成
   * 
   * @return ファイル準備の成否
   * @throws IOException ファイル操作に失敗した場合にスローされる例外
   */
  public File preparationFile() throws IOException {
    URL url = new URL(path);
    url.openConnection();
    File file = new File(url.getFile() + "/file/" + fileName); // ファイルオブジェクトを作成
    if (!file.exists()) {
      file.createNewFile();
    }
    return file;
  }

  /**
   * 書き込み用ストリームを生成 ストリームを生成する際、元ファイルの中身がなくなるので、書き込む直前に生成する
   * 
   * 
   */
  protected FileOutputStream importOutputStream() {
    try {
      File file = preparationFile();
      if (!file.createNewFile()) {
        file.createNewFile();
      }

      // ファイルが存在しない場合、新規作成
      this.output = new FileOutputStream(file); // 書き込み用ストリームを生成
      return this.output;
    } catch (IOException e) {
      this.releaseFile();
      throw new RuntimeException("outputStreamの作成に失敗しました。", e);
    } catch (Exception e) {
      this.releaseFile();
    }
    return null;
  }

  /**
   * 読み込み用ストリームを生成
   * 
   * @throws IOException ストリーム生成に失敗した場合にスローされる例外
   */
  protected FileInputStream importInputStream() throws IOException {
    try {
      File file = preparationFile();
      if (file.exists()) {
        this.input = new FileInputStream(file); // 読み込み用ストリームを生成
        return this.input;
      }
    } catch (IOException e) {
      this.releaseFile();
      throw new RuntimeException("ファイルの生成に失敗しました。", e);
    } catch (Exception e) {
      this.releaseFile();
    }
    return null;
  }

  /**
   * 各ストリームのcloseを呼び出す
   * 
   * @throws IOException ストリームのクローズに失敗した場合にスローされる例外
   */
  public void releaseFile() {
    try {
      if (output != null) {
        output.close(); // 書き込み用ストリームをクローズ
      }
      if (input != null) {
        input.close(); // 読み込み用ストリームをクローズ
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
