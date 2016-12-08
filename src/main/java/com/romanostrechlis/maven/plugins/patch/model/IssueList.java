package com.romanostrechlis.maven.plugins.patch.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Romanos on 1/12/2016.
 */
@XmlRootElement(name="issuelist")
@XmlAccessorType(XmlAccessType.FIELD)
public class IssueList {

  @XmlElement(name="issue")
  private List<Issue> issueList;

  public IssueList() {
  }

  public List<Issue> getIssueList() {
    return issueList;
  }

  public void setIssueList(List<Issue> issueList) {
    this.issueList = issueList;
  }
}
