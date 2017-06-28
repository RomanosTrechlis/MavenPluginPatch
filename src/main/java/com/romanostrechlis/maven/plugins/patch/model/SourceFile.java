package com.romanostrechlis.maven.plugins.patch.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by Romanos on 1/12/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SourceFile {

  @XmlAttribute
  private String filepath;

  public SourceFile() {
  }

  public String getFilepath() {
    return filepath;
  }

  public void setFilepath(String filepath) {
    this.filepath = filepath;
  }
}
