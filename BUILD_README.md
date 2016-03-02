Building nucleus-docviewer
==============

## Prerequisites

- Gradle 2.8
- Java 8
- pdf2swf 0.9.2


## Running Build

The default task is *war* which is provided by plugin. So running *gradle* from command line will run *war* and it will create a war in build/libs folder. Note that there is artifact name specified in build file and hence it will take the name from directory housing the project.

Once the war is created, copy that file to tomcat web-server and rename war file name as nucleus-docviewer.war, then start the tomcat web-server.


## Configuration

1) Copy the ```document-viewer.properties``` file from project folder to nucleus conf folder and replace the appropriate config settings.

2) In tomcat web-server add the below line in ```context.xml```

<Environment name="config-settings-doc" value="/opt/nucleus/conf/document-viewer.properties" type="java.lang.String" override="false"/>