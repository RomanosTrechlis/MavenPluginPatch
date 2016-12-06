# MavenPluginPatch

I use this plugin to create partial patches.
Its main utility is that can copy, given an xml file, classes under the same structure as a web application.

It requires the existence of a *build-patch.xml* file in the root path of project.
```xml
<issuelist>
  <issue name="ISSUE-4334">
    <sourcefile filepath="src\main\java\com\romanostrechlis\maven\plugin\patch\BuildPatchClassMojo.java" />
  </issue>
</issuelist>
```
