package com.seproject.buildmanager.common;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Excelファイルの読み書きを行うサービスクラス
 * 
 * @param <T> 操作対象のデータ型
 */
@Component
@Scope("prototype") // 毎回インスタンスを生成し、他クラスと共有しない
public class ExcelFileService<T> extends FileService {

  // 入力用と出力用のWorkbook
  Workbook inputWorkbook = new XSSFWorkbook();
  Workbook outputWorkbook = new XSSFWorkbook();
  Sheet unitSheet = null;

  /**
   * コンストラクタ
   * 
   * @param fileName 操作するExcelファイルのファイル名
   */
  public ExcelFileService(@Value("${file.server.address}") String path) {
    super(path);
  }

  public void initializeExcel(String fileName) {
    this.setFileName(fileName);
  }

  @Override
  protected void finalize() throws Throwable {
    release(); // オブジェクト解放時にリソースを開放
  }

  /**
   * データをExcelファイルにエクスポートするメソッド
   * 
   * @param tList エクスポートするデータのリスト
   * @param sheetName エクスポート先のシート名
   * @return エクスポートに成功した場合はtrue
   * @throws IOException ファイル操作に失敗した場合にスローされる例外
   */
  public boolean exportDataTypeExcel(List<T> tList, String sheetName) throws IOException {
    if (tList.size() != 0) {
      this.importOutputStream();
      outputWorkbook = new XSSFWorkbook();
      Sheet sheet = this.outputWorkbook.getSheet(sheetName);
      if (sheet == null) {
        sheet = this.outputWorkbook.createSheet(sheetName);
      }
      try {
        this.createHeaderRow(tList.get(0), sheet); // ヘッダ行を作成
        int rowNum = 1;
        for (T t : tList) {
          Row row = sheet.createRow(rowNum++); // 新しい行を作成
          try {
            row = this.writeOwnerDataToRow(t, row); // データを行に書き込む
          } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
          }
        }
        this.StyleSetting(sheet);
        this.outputWorkbook.write(output); // ワークブックを出力ストリームに書き込む
      } catch (IOException e) {
        throw new RuntimeException("エクセルファイルの生成に失敗しました。", e);
      } finally {
        this.release();
      }
      return true;
    }
    return false;
  }

  /**
   * Excelファイルから1行分のデータを読み込むメソッド
   * 
   * @param t 読み込むデータを格納するオブジェクト
   * @param sheetName シート名
   * @param index 行番号
   * @return 読み込まれたデータが格納されたオブジェクト
   */
  public T importDataToRowTypeExcel(T t, String sheetName, int index) {
    try {
      this.importInputStream();
      inputWorkbook = WorkbookFactory.create(input); // 入力ストリームからWorkbookを作成

      unitSheet = inputWorkbook.getSheet(sheetName);
      Row row = unitSheet.getRow(index);
      List<Object> object = new ArrayList<>();
      if (row != null) {
        Iterator<Cell> cells = row.cellIterator();
        while (cells.hasNext()) {
          Cell cell = cells.next();
          switch (cell.getCellType()) {
            case STRING:
              object.add(cell.getStringCellValue()); // 文字列セルの処理
              break;
            case NUMERIC:
              if (DateUtil.isCellDateFormatted(cell)) {
                Instant instant = cell.getDateCellValue().toInstant();
                LocalDateTime localTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                object.add(localTime); // 日付セルの処理
              } else {
                object.add(cell.getNumericCellValue()); // 数値セルの処理
              }
              break;
            default:
              object.add(null); // その他のセルの処理
              break;
          }
        }
        t = this.OneLineToData(t, object); // 読み込んだデータをオブジェクトに変換
      }
    } catch (IOException e) {
      throw new RuntimeException("エクセルファイルの生成に失敗しました。", e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("エクセルファイルの生成に失敗しました。", e);
    } finally {
      this.release();
    }
    return t;
  }

  /**
   * Excelファイルの最後の行を取得するメソッド
   * 
   * @return 最後の行番号
   */
  public int getRowNum() {
    if (unitSheet != null) {
      return unitSheet.getLastRowNum();
    } else {
      return -1;
    }
  }

  public int getRowNum(String sheetName) {
    try {
      this.importInputStream();
      inputWorkbook = WorkbookFactory.create(input); // 入力ストリームからWorkbookを作成
      this.unitSheet = this.inputWorkbook.getSheet(sheetName);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return unitSheet.getLastRowNum();
  }

  /**
   * シート名を設定するメソッド
   * 
   * @param sheetName 設定するシート名
   */
  public void setSheetName(String sheetName) {
    try {
      this.importInputStream();
      inputWorkbook = WorkbookFactory.create(input); // 入力ストリームからWorkbookを作成
      this.unitSheet = this.inputWorkbook.getSheet(sheetName);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * フィールドをセルに格納するメソッド
   * 
   * @param t データを格納するオブジェクト
   * @param row データを格納する行
   * @return データが格納された行
   * @throws NumberFormatException フィールドの値が数値に変換できない場合にスローされる例外
   * @throws IllegalArgumentException フィールドに不正な引数が渡された場合にスローされる例外
   * @throws IllegalAccessException フィールドにアクセスできない場合にスローされる例外
   */
  private Row writeOwnerDataToRow(T t, Row row)
      throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
    int i = 0;
    for (Field field : t.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      if (field.get(t) != null) {
        switch (field.getType().getName()) {
          case "java.lang.Integer":
            row.createCell(i).setCellValue(Integer.parseInt(String.valueOf(field.get(t)))); // 整数データの処理
            break;
          case "java.lang.String":
            row.createCell(i).setCellValue(String.valueOf(field.get(t))); // 文字列データの処理
            break;
          case "java.time.LocalDateTime":
            row.createCell(i).setCellValue(field.get(t).toString()); // 日付データの処理
            break;
        }
      } else {
        row.createCell(i).setCellValue("NULL"); // null値の処理
      }
      i++;
    }
    return row;
  }

  /**
   * ヘッダ行を作成するメソッド
   * 
   * @param t ヘッダの元となるオブジェクト
   * @param sheet ヘッダ行を作成するシート
   */
  private void createHeaderRow(T t, Sheet sheet) {
    Row headerRow = sheet.createRow(0);
    int i = 0;
    for (Field field : t.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      headerRow.createCell(i).setCellValue(field.getName()); // フィールド名をヘッダに設定
      i++;
    }
  }

  /**
   * オブジェクトのフィールドにデータをセットするメソッド
   * 
   * @param t データを格納するオブジェクト
   * @param object 読み込んだデータのリスト
   * @return データがセットされたオブジェクト
   * @throws NumberFormatException フィールドの値が数値に変換できない場合にスローされる例外
   * @throws IllegalArgumentException フィールドに不正な引数が渡された場合にスローされる例外
   * @throws IllegalAccessException フィールドにアクセスできない場合にスローされる例外
   */
  private T OneLineToData(T t, List<Object> object)
      throws NumberFormatException, IllegalArgumentException, IllegalAccessException {
    int i = 0;
    T tw = t;

    for (Field field : tw.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      if (object.size() > i && object.get(i) != null && !object.get(i).equals("NULL")) {
        switch (field.getType().getName()) {
          case "java.lang.Integer":
            field.set(tw, (int) Double.parseDouble(String.valueOf(object.get(i)))); // 整数データのセット
            break;
          case "java.lang.String":
            field.set(tw, String.valueOf(object.get(i))); // 文字列データのセット
            break;
          case "java.time.LocalDateTime":
            String localtime = String.valueOf(object.get(i));
            localtime = localtime.replace("T", " ");
            LocalDateTime localDateTime = LocalDateTime.parse(localtime,
                DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss][yyyy-MM-dd HH:mm]")); // 日付データのセット
            field.set(tw, localDateTime);
            break;
        }
      } else {
        field.set(tw, null); // null値のセット
      }
      i++;
    }
    return tw;
  }

  /**
   * リソースを開放するメソッド
   * 
   * @throws IOException リソースの開放に失敗した場合にスローされる例外
   */
  public void release() {
    try {
      if (this.inputWorkbook != null) {
        this.inputWorkbook.close();
      }
      if (this.outputWorkbook != null) {
        this.outputWorkbook.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      super.releaseFile();
    }
  }

  /**
   * セルのスタイル設定を行うメソッド
   */
  public void StyleSetting(Sheet sheet) {
    // 幅の自動調整を行う (ここでは例としてB列とする)
    CellReference cellReference = new CellReference("E");
    sheet.autoSizeColumn(cellReference.getCol());
  }
}
