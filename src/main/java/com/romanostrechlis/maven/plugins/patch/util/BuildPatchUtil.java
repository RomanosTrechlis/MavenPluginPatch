package com.romanostrechlis.maven.plugins.patch.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Romanos on 1/12/2016.
 */
public class BuildPatchUtil {

  public static String DOUBLE_SLASHES = "\\";

  /**
   * JaxB converter from XML to Pojo
   */
  public static Object readIssueFile(String issueFile,
                               Class classs) throws Exception {
    File file = new File(issueFile);
    JAXBContext jaxbContext = JAXBContext.newInstance(classs);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    return jaxbUnmarshaller.unmarshal(file);
  }

  /**
   * Method creates string from exception stacktrace.
   *
   * @param e
   * @return
   */
  public static String getStackTraceString(Exception e) {
    StackTraceElement[] stackTraceElementArray = e.getStackTrace();
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement stackTraceElement : stackTraceElementArray) {
      if (sb.length() != 0) {
        sb.append("\t");
      }
      sb.append(stackTraceElement.toString())
          .append("\n");
    }
    return sb.toString();
  }
}
