# Introduction #

I will explain here how to download and deploy Mobicents SMSC GW.

NOTE : Mobicents SMSC is under AGPL License.


# Details #


1) We need to download and compile last JAIN SLEE (how to do it - see at site of the project): https://code.google.com/p/jain-slee.
At the end of this procedure we will have JBOSS server with deployed there JAIN-SLEE.

2) Set a environment variable JBOSS\_HOME to the path where you have installed JBOSS server.
For example if we have installed JBOSS to the /opt/smsc/jboss folder use commands:
  * windows:
    * set JBOSS\_HOME=C:\opt\smsc\jboss
  * linux:
    * export JBOSS\_HOME=/opt/smsc/jboss

3) download and compile sctp library https://code.google.com/p/sctp version "release-1.4.0.FINAL"
  * git clone https://code.google.com/p/sctp
  * cd sctp
  * git checkout release-1.4.0.FINAL
  * update the root pom.xml file: in `<repositories>` section replace
```
<url>http://oss.sonatype.org/content/groups/public/</url>
```
with
```
<url>https://oss.sonatype.org/content/groups/public/</url>
```
(so just replace http->https)
  * mvn clean install -Dmaven.test.skip=true
  * cd ..

4) download and compile asn library https://code.google.com/p/jasn version "release-2.0.2.FINAL"
  * git clone https://code.google.com/p/jasn
  * cd jasn
  * git checkout release-2.0.2.FINAL
  * update the root pom.xml file: in `<repositories>` section replace
```
<url>http://oss.sonatype.org/content/groups/public/</url>
```
with
```
<url>https://oss.sonatype.org/content/groups/public/</url>
```
(so just replace http->https)
  * mvn clean install -Dmaven.test.skip=true
  * cd ..

5) download, compile jss7 library https://code.google.com/p/jss7 version "release-2.1.0.FINAL" and deploy CLI components to jboss server.
We need to clone it from sources, checkout "release-2.1.0.FINAL" tag and compile.
  * git clone https://code.google.com/p/jss7
  * cd jss7
  * git checkout release-2.1.0.FINAL
  * update the root pom.xml file:
    * a) in `<repositories>` section
replace
```
<url>http://oss.sonatype.org/content/groups/public/</url>
```
with
```
<url>https://oss.sonatype.org/content/groups/public/</url>
```
(so just replace http->https)
      * b) comment out in the `<modules>` section two last modules (sgw and tools - we do not need them):
replace
```
		<module>sgw</module>
		<module>tools</module>
```
with
```
<!--
		<module>sgw</module>
		<module>tools</module>
-->
```
  * mvn clean install -P jboss -Dmaven.test.skip=true
  * cd ..

6) remove deployed jss7 stack from jboss (we do not need it):
  * delete the folder: jboss/default/deploy/mobicents-ss7-service

7) download and compile SIP RA https://code.google.com/p/jain-slee.sip/ last version
  * git clone https://code.google.com/p/jain-slee.sip/
  * cd jain-slee.sip
  * comment out all modules excluding "resources/sip11" (the first one) from "jain-slee.sip/pom.xml"
`replace`
```
	<modules>
		<module>resources/sip11</module>
		<module>enablers/sip-publication-client</module>
		<module>enablers/sip-subscription-client</module>
		<module>examples/sip-b2bua</module>			
		<module>examples/sip-uas</module>			
		<module>examples/sip-jdbc-registrar</module>			
		<module>examples/sip-wake-up</module>
	</modules>
```
`with`
```
	<modules>
		<module>resources/sip11</module>
<!--
		<module>enablers/sip-publication-client</module>
		<module>enablers/sip-subscription-client</module>
		<module>examples/sip-b2bua</module>			
		<module>examples/sip-uas</module>			
		<module>examples/sip-jdbc-registrar</module>			
		<module>examples/sip-wake-up</module>
-->
	</modules>
```
  * mvn clean install -Dmaven.test.skip=true
  * cd ..

8) clone the last version of SMSC project https://code.google.com/p/smscgateway/ master branch:
  * git clone https://code.google.com/p/smscgateway/
  * cd smscgateway
  * mvn clean install -Dmaven.test.skip=true

9) if you need of CDR's generating you need to update jboss/server/default/conf/jboss-log4j.xml by adding there two extra sections for logging like following:
```
	<appender name="CDR" class="org.jboss.logging.appender.DailyRollingFileAppender"> 
	    <errorHandler class="org.jboss.logging.util.OnlyOnceErrorHandler"/> 
	    <param name="File" value="${jboss.server.home.dir}/log/cdr.log"/> 
	    <param name="Append" value="true"/> 
	    <param name="MaxFileSize" value="500KB"/>
	    <param name="MaxBackupIndex" value="1"/>
	    <param name="Threshold" value="DEBUG"/>

	    <param name="DatePattern" value="'.'yyyy-MM-dd"/>

	    <layout class="org.apache.log4j.PatternLayout"> 
		<param name="ConversionPattern" value="%d %-5p [%c] %m%n"/> 
	    </layout> 
	</appender> 
```
and
```
	<category name="org.mobicents.smsc.domain.library.CdrGenerator" additivity="false"> 
	    <priority value="DEBUG" /> 
	    <appender-ref ref="CDR"/> 
	</category> 
```

10) run SMSC by the commands:
  * for linux:
    * export JBOSS\_HOME=/<...>/jboss
    * cd <...>/jboss/bin
    * ./run.sh
  * for windows:
    * set JBOSS\_HOME=<...>\jboss
    * cd <...>/jboss/bin
    * run

11) configuring of SMSC GW for a simple test:
  * a) run CLI interface
    * for linux:
      * cd <...>/jboss/bin
      * ./ss7-cli.sh
    * for windows:
      * cd <...>/jboss/bin
      * ss7-cli
  * b) connect to SMSC GW server
    * connect
  * c) create ESME (for SMPP connection) that will accept messages for the number 6666
    * smpp esme create test test -1 -1 TRANSCEIVER SERVER password test esme-ton -1 esme-npi -1 esme-range 6666 source-range 6666 routing-range 6666
    * smpp esme start test
  * d) configure SIP connector that will accept messages for the number 5555
    * smsc sip modify SIP cluster-name cluster host 127.0.0.1 port 5065 routing-ton -1 routing-npi -1 routing-range 5555 counters-enabled false

12) run any SMPP utility you wish to connect to SMSC (as systemId and password == test (port is 2776)).
You can run a provided Mobicents SMPP Simulator. To run it:
for linux:
  * cd <folder where we have cloned Mobicents SMSC GW>/smscgateway/tools/smpp-simulator/bootstrap/target/simulator-smpp/bin
    * ./run.sh
  * for windows:
    * cd <folder where we have cloned Mobicents SMSC GW>\smscgateway\tools\smpp-simulator\bootstrap\target\simulator-smpp\bin
    * run
Then the GUI application is opened. We can use default SMPP Simulator's settings for connecting to SMSC GW (as it has been configured at the previous step). Then press "Run test" button - "Start a sesion" button. SMPP Simulator will connect to SMSC GW.

13) run any SIP phone that supports messaging, For example Linphone.
Configure it so it listern 5065 port for incoming SIP requests. SMSC GW by default listern port 5060. Use the following SIP address for sending messages to SMSC GW: "sip:6666@127.0.0.1:5060"

14) now you can send a message from SMPP Simulator to SIP Phone (press "Submit a message" button at SMPP Simulator) or send a message from SIP phone to SMPP Simulator.