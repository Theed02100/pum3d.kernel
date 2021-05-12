package com.pum.it.pum3d.kernel.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "increment")
public class Increment {
  @Id
  private String id;
  private Integer count;

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
