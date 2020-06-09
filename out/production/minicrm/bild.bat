set LIB=../lib

set CLASSPATH=.;%LIB%/*;C:\Program Files (x86)\Apache Software Foundation\Tomcat 9.0\lib\servlet-api.jar
javac -source 1.6 -target 1.6 user\minicrm\common\beans\*.java
javac -source 1.6 -target 1.6 user\minicrm\server\util\*.java
javac -source 1.6 -target 1.6 user\minicrm\zk\model\*.java
javac -source 1.6 -target 1.6 user\minicrm\zk\util\*.java


java org.apache.openjpa.enhance.PCEnhancer user\minicrm\common\beans\*.java
java org.apache.openjpa.enhance.PCEnhancer user\minicrm\zk\util\Role.java