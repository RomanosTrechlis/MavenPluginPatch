# MavenPluginPatch

I use this plugin to create partial patches.
Its main utility is that it can copy, given an xml file, classes under the same structure as a web application.

It requires the existence of a *build-patch.xml* file in a given path of project.
```xml
<issuelist>
  <issue name="ISSUE-4334">
    <sourcefile filepath="src\main\java\com\romanostrechlis\maven\plugins\patch\BuildPatchClassMojo.java" />
  </issue>
</issuelist>
```

In *pom.xml* it should be added the following:
```xml
<builds>
  <plugins>
    <plugin>
      <groupId>com.romanostrechlis.maven.plugins</groupId>
      <artifactId>patch</artifactId>
      <version>2.3</version>
      <configuration>
        <projectBaseDir>${project.basedir}</projectBaseDir>
        <patchDir>${patch.path}\patch</patchDir>
        <issueFile>${project.basedir}\build-patch.xml</issueFile>
        <classReplaceFolder>webapp\WEB-INF\classes</classReplaceFolder>
        <contextName>${web.app.context}</contextName>
        <configPath>${config.path}</configPath>
      </configuration>
    </plugin>
    ...
  </plugins>
</builds>
```

There configuration tags are:

1. **projectBaseDir**
2. **patchDir**: where the patch folder should be created.
3. **issueFile**: what is the file containing info about the patch (see example above).
4. **classReplaceFolder**: the content of this tag will be replaced by the *contextName*.
5. **contextName**: replaces the content of *classReplaceFolder* in the patch path.
6. **configPath**: the path of configuration files.

##Contact Info
*Romanos Trechlis* @r_trechlis

[www.romanostrechlis.com](http://www.romanostrechlis.com)