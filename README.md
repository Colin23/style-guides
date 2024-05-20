# Java style guides
This repository contains style guides for Gradle Java applications. 
The mentioned style guides consist of multiple modules. 
Depending on the use case of the Gradle project, it makes sense to either integrate all modules or pick specific ones.
The mentioned modules are:
- [Checkstyles](checkstyle.xml)
- [Forbidden APIs](forbidden-apis.txt)
- [Architecture tests](ArchitectureTests.java)
- [Git attributes](.gitattributes)
- [Editor config](.editorconfig)
- [Gradle config](build.gradle.kts)


## Setup
Before setting up your Gradle project, you should read the following sections carefully!

> [!NOTE]  
> As I currently do not have an automated way of integrating all the different modules into a new project, they have to be copied manually.

> [!IMPORTANT]  
> 1. Some things in the [build.gradle.kts](build.gradle.kts) file are vital for setting up new Gradle projects and others are not. 
> Make sure you only delete what you definitely won't need and keep the rest.
> 2. As written in the first line of the [ArchitectureTests.java](ArchitectureTests.java), you have to adjust its location. 
> Usually this file sits on the root level of the `test` directory.

> [!CAUTION]
> The used versions in the different modules are not up to date and might need to be changed. 
> Otherwise you could risk security issues in the various dependencies and/or problems with your Gradle setup.