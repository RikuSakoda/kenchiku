package com.seproject.buildmanager.service;

import java.util.List;

public interface MstSearchService<T, E> {
  public List<E> search(T t);

  public default String nullCheck(String field) {
    String key = "";
    if (!(field == null || field.equals(""))) {
      key += "%" + field + "%";
    }
    return key;
  };
}
