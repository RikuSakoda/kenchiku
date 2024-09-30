package com.seproject.buildmanager.form;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MstYoshinoForm {

    @NotEmpty(message = "顧客idを入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private Integer custkind; // 顧客id

    @NotEmpty(message = "顧客名を入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private String custcorpname; // 顧客名

    @NotEmpty(message = "顧客名（かな）を入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private String custcorpnamekana; // 顧客名かな

    @NotEmpty(message = "部署名を入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private String custdepartmentname; // 部署

    @NotEmpty(message = "姓を入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private String custlname; // 姓

    @NotEmpty(message = "名を入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private String custfname; // 名

    @NotEmpty(message = "姓（かな）を入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private String custlnamekana; // 姓かな

    @NotEmpty(message = "名（かな）を入力してください")
    @Size(max = 20, message = "20文字以内で入力してください")
    private String custfnamekana; // 名かな

    @NotEmpty(message = "電話番号を入力してください")
    @Size(max = 14, message = "14文字以内で入力してください")
    private String custphone; // 電話番号

    @NotEmpty(message = "携帯電話番号を入力してください")
    @Size(max = 14, message = "14文字以内で入力してください")
    private String custmobilephone; // 携帯電話番号
    
    private String transactionToken;
    
    private Integer id;
    
 
}
