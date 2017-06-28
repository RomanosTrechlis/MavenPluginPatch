package com.romanostrechlis.maven.plugins.patch;

import com.romanostrechlis.maven.plugins.patch.model.Issue;
import com.romanostrechlis.maven.plugins.patch.model.IssueList;
import com.romanostrechlis.maven.plugins.patch.model.SourceFile;
import com.romanostrechlis.maven.plugins.patch.util.BuildPatchUtil;
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
@Mojo(name = "sources")
public class BuildPatchSourceMojo extends AbstractMojo {

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
  @Parameter(property = "patch.contextName")
  private String contextName;
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
      patchSources(fileList, issue.getName());
      System.out.println("################################### " + issue.getName()
          + " [END] ###################################");
      System.out.println("\n");
    }
  }

  /**
   * Method copies java to corresponding patch folder.
   */
  private void patchSources(List<SourceFile> fileList,
                            String subFolderName) throws Exception {
    for (SourceFile file : fileList) {
      String relativePath = file.getFilepath();

      try {
        System.out.println(">>> Copying file " + projectBaseDir + relativePath);
        FileUtils.copyFile(new File(projectBaseDir + relativePath),
            new File(patchDir + subFolderName + BuildPatchUtil.DOUBLE_SLASH + relativePath),
            true);
      } catch (Exception e) {
        System.out.println(">>> Failure!!!");
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
    if (!projectBaseDir.endsWith(BuildPatchUtil.DOUBLE_SLASH)) {
      System.out.println(">>> Fixing param: " + projectBaseDir + " to: " + projectBaseDir + BuildPatchUtil.DOUBLE_SLASH);
      this.projectBaseDir = projectBaseDir + BuildPatchUtil.DOUBLE_SLASH;
    }

    if (!patchDir.endsWith(BuildPatchUtil.DOUBLE_SLASH)) {
      System.out.println(">>> Fixing param: " + patchDir + " to: " + patchDir + BuildPatchUtil.DOUBLE_SLASH);
      this.patchDir = patchDir + BuildPatchUtil.DOUBLE_SLASH;
    }

    if (StringUtils.isNotEmpty(classReplaceFolder) && classReplaceFolder.endsWith(BuildPatchUtil.DOUBLE_SLASH)) {
      System.out.println(">>> Fixing param: " + classReplaceFolder + " to: " + classReplaceFolder
          .substring(0, classReplaceFolder.length() - 1) + BuildPatchUtil.DOUBLE_SLASH);
      this.classReplaceFolder = classReplaceFolder.substring(0, classReplaceFolder.length() - 1);
    }
    if (StringUtils.isNotEmpty(classReplaceFolder) && classReplaceFolder.startsWith(BuildPatchUtil.DOUBLE_SLASH)) {
      System.out.println(">>> Fixing param: " + classReplaceFolder + " to: " + classReplaceFolder
          .substring(1, classReplaceFolder.length()) + BuildPatchUtil.DOUBLE_SLASH);
      this.classReplaceFolder = classReplaceFolder.substring(1, classReplaceFolder.length());
    }

    if (StringUtils.isNotEmpty(configPath) && configPath.endsWith(BuildPatchUtil.DOUBLE_SLASH)) {
      System.out.println(">>> Fixing param: " + configPath + " to: " + configPath
          .substring(0, configPath.length() - 1) + BuildPatchUtil.DOUBLE_SLASH);
      this.configPath = configPath.substring(0, configPath.length() - 1);
    }
    if (StringUtils.isNotEmpty(configPath) && configPath.startsWith(BuildPatchUtil.DOUBLE_SLASH)) {
      System.out.println(">>> Fixing param: " + configPath + " to: " + configPath
          .substring(1, configPath.length()) + BuildPatchUtil.DOUBLE_SLASH);
      this.configPath = configPath.substring(1, configPath.length());
    }
    System.out.println("\n");
  }

}
