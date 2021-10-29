#This is where you CUSTOM your authentication for HIVE!
hive/src/java/org/apache/hive/exntu/ExntuAuthenticator.java
https://github.com/choonglee170924/hive/blob/main/src/java/org/apache/hive/exntu/ExntuAuthenticator.java

javac -cp /opt/hive/lib/hive-service-2.3.9.jar:/root/jar/spring-security-core-2.0.2.jar ExntuAuthenticator.java -d .
jar cf exntu-auth.jar org
cp exntu-auth.jar /opt/hive/lib/

cp spring-security-core-2.0.2.jar HIVE_HOVE/lib/
cp exntu-auth.jar HIVE_HOVE/lib/

hive-site.xml
...
  <property>
    <name>hive.server2.authentication</name>
    <value>CUSTOM</value>
  </property>
  <property>
    <name>hive.server2.custom.authentication.class</name>
    <value>org.apache.hive.exntu.ExntuAuthenticator</value>
  </property>
...


