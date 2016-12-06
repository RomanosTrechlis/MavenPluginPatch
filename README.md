# MavenPluginPatch

I use this maven plugin to create partial patches. 
Its main utility is that can copy, given an xml file, classes under the same structure as a web application.

It requires the existance of a *build-patch.xml* file in the root of the project.
```xml
<issuelist>
  <issue name="ISSUE-4334">
    <sourcefile filepath="src\main\java\com\romanostrechlis\maven\plugin\patch\BuildPatchClassMojo.java" />
  </issue>
</issuelist>
```
