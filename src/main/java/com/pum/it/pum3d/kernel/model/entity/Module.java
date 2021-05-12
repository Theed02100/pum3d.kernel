package com.pum.it.pum3d.kernel.model.entity;

import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "module")
public class Module {

  @Id
  private String id;
  private String libelle;
  private String url;
  private Boolean binded;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLibelle() {
    return libelle;
  }

  public void setLibelle(String libelle) {
    this.libelle = libelle;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Boolean getBinded() {
    return binded;
  }

  public void setBinded(Boolean binded) {
    this.binded = binded;
  }

  @Override
  public String toString() {
    return "Module{"
        + "id=" + id
        + ", libelle='" + libelle + '\''
        + ", url='" + url + '\''
        + ", binded=" + binded
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    } else if (getClass() != o.getClass()) {
      return false;
    }
    Module module = (Module) o;
    return Objects.equals(libelle, module.libelle)
        && Objects.equals(url, module.url)
        && Objects.equals(binded, module.binded);
  }
}
