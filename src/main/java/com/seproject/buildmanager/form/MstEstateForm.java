package com.seproject.buildmanager.form;


import java.util.List;
import lombok.Data;

@Data
public class MstEstateForm {

  private Integer id;

  private Integer proId;

  private String proName;

  private Integer layId;

  private String layName;

  private List<Integer> checkId;

  private List<String> checkNames;


}
