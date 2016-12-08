package com.romanostrechlis.maven.plugins.patch.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by Romanos on 1/12/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Issue {

  @XmlAttribute
  private String name;

  @XmlElement(name="srcfile")
  private List<SourceFile> srcFiles;

  public Issue() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<SourceFile> getSrcFiles() {
    return srcFiles;
  }

  public void setSrcFiles(List<SourceFile> srcFiles) {
    this.srcFiles = srcFiles;
  }
}
