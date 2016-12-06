package com.romanostrechlis.maven.plugin.patch;

import com.romanostrechlis.maven.plugin.patch.model.Issue;
import com.romanostrechlis.maven.plugin.patch.model.IssueList;
import com.romanostrechlis.maven.plugin.patch.model.SourceFile;
import com.romanostrechlis.maven.plugin.patch.util.BuildPatchUtil;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Romanos on 1/12/2016.
 */
@Mojo(name = "classes")
public class BuildPatchClassMojo extends AbstractMojo {

  /**
   * Parameters are set inside the configuration tag in pom.xml build plugins patch section
   */
  @Parameter(property = "patch.projectBaseDir")
  private String projectBaseDir;
  @Parameter(property = "patch.patchDir")
  private String patchDir;
  @Parameter(property = "patch.issueFile")
  private String issueFile;
  @Parameter(property = "patch.classReplaceFolder")
  private String classReplaceFolder;
  @Parameter(property = "patch.applicationName")
  private String applicationName;
  @Parameter(property = "patch.configPath")
  private String configPath;

  public void execute() throws MojoExecutionException, MojoFailureException {
    fixBackSlashes(projectBaseDir, patchDir, classReplaceFolder, configPath);

    IssueList issueList;
    try {
      issueList = (IssueList) BuildPatchUtil.readIssueFile(issueFile, IssueList.class);
      if (issueList == null || issueList.getIssueList().size() < 1) {
        System.out.println(">>> There where no issues inside " + issueFile + "!!!");
        return;
      }
    } catch (Exception e) {
      System.out.println(BuildPatchUtil.getStackTraceString(e));
      throw new MojoExecutionException(e.getMessage());
    }

    try {
      createPatch(issueList);
    } catch (Exception e) {
      System.out.println(BuildPatchUtil.getStackTraceString(e));
      throw new MojoExecutionException(e.getMessage());
    }
  }

  private void createPatch(IssueList issueList) throws Exception {
    for (Issue issue : issueList.getIssueList()) {
      System.out.println("################################### " + issue.getName()
          + " [START] ###################################");
      List<SourceFile> fileList = issue.getSrcFiles();
      FileUtils.deleteDirectory(new File(patchDir + issue.getName()));
      patchClasses(fileList, issue.getName());
      System.out.println("################################### " + issue.getName()
          + " [END] ###################################");
      System.out.println("\n");
    }
  }

  /**
   * Method copies class files and resources to corresponding
   * patch folder including files with "$".
   */
  private void patchClasses(List<SourceFile> fileList,
                            String subFolderName) throws Exception {
    for (SourceFile file : fileList) {
      file.setFilepath(file.getFilepath().replace(".java", ".class"));
      String relativePath = file.getFilepath();
      String folderPath = relativePath.substring(0, relativePath.lastIndexOf("\\") + 1);
      String
          fileName =
          relativePath.substring(relativePath.lastIndexOf("\\") + 1, relativePath.lastIndexOf("."));
      folderPath = folderPath.replace("java", classReplaceFolder);
      File pathFile = new File(projectBaseDir + folderPath);
      File[] files = pathFile.listFiles();
      if (files == null) {
        System.out.println(">>> No files found in path: " + projectBaseDir + folderPath);
        continue;
      }
      for (File f : files) {
        if (f.isDirectory()) {
          continue;
        }
        if (f.getName().contains(fileName)) {
          String destPath = new String(folderPath);
          if (destPath.contains("src\\main\\webapp") && StringUtils
              .isNotEmpty(applicationName)) { // this isn't applicable for other applications
            destPath = destPath.replace("src\\main\\webapp", applicationName);
          } else if (destPath.contains("src\\main\\resources") && StringUtils
              .isNotEmpty(configPath)) { // this isn't applicable for other applications
            destPath = destPath.replace("src\\main\\resources", configPath);
          }
          String finalPath = projectBaseDir + folderPath;
          try {
            System.out.println(">>> Copying file " + finalPath + f.getName());
            FileUtils.copyFile(new File(finalPath + f.getName()),
                new File(patchDir + subFolderName + "\\" + destPath + f.getName()), true);
          } catch (Exception e) {
            System.out.println(">>> Failure!!!");
          }
        }
      }
    }
  }

  /**
   * Some parameters require ending backslash and some not.
   */
  private void fixBackSlashes(String projectBaseDir,
                              String patchDir,
                              String classReplaceFolder,
                              String configPath) {
    if (!projectBaseDir.endsWith("\\")) {
      System.out.println(">>> Fixing param: " + projectBaseDir + " to: " + projectBaseDir + "\\");
      this.projectBaseDir = projectBaseDir + "\\";
    }

    if (!patchDir.endsWith("\\")) {
      System.out.println(">>> Fixing param: " + patchDir + " to: " + patchDir + "\\");
      this.patchDir = patchDir + "\\";
    }

    if (StringUtils.isNotEmpty(classReplaceFolder) && classReplaceFolder.endsWith("\\")) {
      System.out.println(">>> Fixing param: " + classReplaceFolder + " to: " + classReplaceFolder
          .substring(0, classReplaceFolder.length() - 1) + "\\");
      this.classReplaceFolder = classReplaceFolder.substring(0, classReplaceFolder.length() - 1);
    }
    if (StringUtils.isNotEmpty(classReplaceFolder) && classReplaceFolder.startsWith("\\")) {
      System.out.println(">>> Fixing param: " + classReplaceFolder + " to: " + classReplaceFolder
          .substring(1, classReplaceFolder.length()) + "\\");
      this.classReplaceFolder = classReplaceFolder.substring(1, classReplaceFolder.length());
    }

    if (StringUtils.isNotEmpty(configPath) && configPath.endsWith("\\")) {
      System.out.println(">>> Fixing param: " + configPath + " to: " + configPath
          .substring(0, configPath.length() - 1) + "\\");
      this.configPath = configPath.substring(0, configPath.length() - 1);
    }
    if (StringUtils.isNotEmpty(configPath) && configPath.startsWith("\\")) {
      System.out.println(">>> Fixing param: " + configPath + " to: " + configPath
          .substring(1, configPath.length()) + "\\");
      this.configPath = configPath.substring(1, configPath.length());
    }
    System.out.println("\n");
  }

}