# cics-liberty-minibank-example

CICS Minibank is a fully portable JEE7 application which can be developed and tested on laptops with Websphere Development Toolkit(WDT), and
can be ported to CICS Liberty as production environment. The application contains two main parts:

1. A frontend application using JSF, CDI, JAX-RS and JSONP to receive user input from Web pages and to pass request to a bankend application with JSON. 
The Java source components and the artifact of the frontend application can be found in the Minibank-JEE7-Frontend directory in this repository.

2. A backend application using JAX-RS, JSONP, CDI, JPA to update/query database based on the content in JSON requests. 
The Java source components and the artifact of the backend application can be found in the Minibank-JEE7-Backend directory in this repository.


## License
This project is licensed under [Apache License Version 2.0](LICENSE).   

## Contents

###Frontend application
- [*Minibank-JEE7-Frontend/src*](Minibank-JEE7-Frontend/src)

	Source code of frontend. In `entity`  folder, it includes POJO classes for data transferring with backend server. In `managedBean` folder, it includes **CDI Beans** for **JSF** to use. These seven Beans match the seven key functions in our application. In `Util` folder, it includes constants and JAX Client class.
	
- [*Minibank-JEE7-Frontend/WebContent*](Minibank-JEE7-Frontend/WebContent)

	Web pages' source code and configurations for web layer, e.g. web.xml. In `minibank-pages` folder, it includes the .xhtml pages we use and they use **JSF** tags to transfer data with CDI Beans.
	
- [*Minibank-JEE7-Frontend/wlp*](Minibank-JEE7-Frontend/wlp)

	**server.xml** configuration for frontend Liberty Server for your reference. When you start your own Liberty server for frontend, you can refence to it for your server.xml configuration.
	
- [*Minibank-JEE7-Frontend/com.ibm.cicsdev.minibank.frontend.war*](Minibank-JEE7-Frontend/com.ibm.cicsdev.minibank.frontend.war)

	war file for frontend.

	***NOTE:*** *if you want to try our samples quickly, you can put it in dropins folder of your Liberty frontend server, then the frontend part can run on server automatically.*


###Backend application

- [*Minibank-JEE7-Backend/src*](Minibank-JEE7-Backend/src)

	Source code of backend. In `DTO` file are date transfer objects to communicate with frontend. For `entities` folder it includes the entities using in **JPA** and will be persist into datebase when the application start. `JAXConfig` is the configuration class that includes the JAX-RS services need to be initiated when the application start. `service` folder includes REST services the backend supply.
	
- [*Minibank-JEE7-Backend/WebContent*](Minibank-JEE7-Backend/WebContent)

	Web configuration files for reference in this folder, e.g. web.xml.
- [*Minibank-JEE7-Backend/wlp*](Minibank-JEE7-Backend/wlp)

	**server.xml** configuration for backend Liberty Server. If you setup your own Liberty server, please replace your server.xml file with this one.
	
- [*Minibank-JEE7-Backend/com.ibm.cicsdev.minibank.backend.war*](Minibank-JEE7-Backend/com.ibm.cicsdev.minibank.backend.war)
	
	war file for backend. Like frontend.
	
	***NOTE:*** *If you want to try our samples quickly, you can put it in dropins folder of your Liberty backend server, then the backend part can run on server automatically.*
	
###Database table definition
- [*Minibank_DDL_DB2.jcl*](DB-Tables/Minibank_DDL_DB2.jcl)

	DDL file for DB2 datebase creation.
- [*Minibank_DDL_Derby.sql*](DB-Tables/Minibank_DDL_Derby.sql)

	DDL file for Derby database creation.

## Pre-reqs

* CICS TS V5.3 or later
* Java SE 1.7 or later on the z/OS system
* Java SE 1.7 or later on the workstation
* Eclipse with WebSphere Developer Tools(WDT) and CICS Explorer SDK installed. 

Two ways to install WDT and CICS Explorer SDK as follows.

1. Download CICS Explorer (https://developer.ibm.com/mainframe/products/downloads/eclipse-tools/),

   Open CICS Explorer -> Help -> Install New Software, and Add repository site(http://public.dhe.ibm.com/ibmdl/export/pub/software/htp/zos/tools/aqua/),
   
   Expand 'IBM CICS Explorer' and select 'IBM CICS SDK for Servlet and JSP support' to install WDT within CICS Explorer SDK.
   
   You can get all above steps by this link(http://www-01.ibm.com/support/docview.wss?uid=swg24033579#Liberty).
   
2. Download Eclipse IDE for Java EE Developers(Luna) by this link(http://www.eclipse.org/downloads/packages/release/Luna/SR2) ,

   Install WDT in Eclipse by this link(https://developer.ibm.com/wasdev/getstarted/),
   
   Install CICS Explorer SDK by open Help -> Install New Software, and Add repository site(http://public.dhe.ibm.com/ibmdl/export/pub/software/htp/zos/tools/aqua/), select 'IBM CICS Explorer' and install it. 
   
   ***Notes***
   *If you can't start Eclipse in MacOS, please try the following workarounds.
   Switch to the folder containing the app.  The example app is zosexplorer.app in the zosexplorer folder in my Downloads folder.

     cd ~/Downloads/zosexplorer 

   Issue the command 

     xattr -dr com.apple.quarantine zosexplorer.app/ 

   You can now right click / CMD-click on the app and use the Open menu to run it for the first time.*  




## Configuration


### To try the samples in your laptop:


1. Download [Liberty](https://developer.ibm.com/wasdev/getstarted/) if you haven't installed it in your laptop. 

2. Download [Derby](https://db.apache.org/derby/derby_downloads.html) if you haven't installed yet, after that create a derby database in your laptop. We have already provided the DDL for the database needed, you can make your database using this DDL quite easily.Then put [*Minibank_DDL_Derby.sql*](DB-Tables/Minibank_DDL_Derby.sql) file under your Derby's 'bin' folder. According to [Derby Reference](https://builds.apache.org/job/Derby-docs/lastSuccessfulBuild/artifact/trunk/out/getstart/index.html) to create your own Derby database named ***minibank*** with the command as follows:
   
   Set JAVA_HOME by the command
   
     ***export JAVA_HOME=~/jdk1.8.0_112.jdk***
	 
   Set DERBY_HOME by the command
   
     ***export DERBY_HOME=~/db-derby-10.13.1.1-bin***
	 
   Set DERBY_HOME or JAVA_HOME as evnrionment varibles by the below example command

     ***export PATH="$DERBY_HOME/bin:$PATH"***

   run the following command in console to prepare your table in Derby
 
     ***ij (start ij tool to manage Derby)***
	 
   In ij prompt command line, please run 
   
     ***CONNECT 'jdbc:derby:minibank;create=true';***

     ***run '~/bin/Minibank_DDL_Derby.sql';***

	After creating the database, you can find a directory named 'minibank' under your current directory if above commands executes successfully.

	Quit the **ij** command by ***exit;*** and run ***startNetworkServer*** in 'bin'. This will set it to NetworkServer mode and start automatically listening on port 1527. 

3. In your Eclipse Servers view, create a Liberty server for backend. Edit the ***server.xml*** by referencing the one that we provide you in [*backend_server.xml*](Minibank-JEE7-Backend/wlp/server.xml). You need to change the label '<dataSource>','<databaseName>' to your own derby database path,and the same for label '<library>', change the '<fileset dir>' to your derby's installation path for libraries.
	After that,put the [*backend war*](Minibank-JEE7-Backend/com.ibm.cicsdev.minibank.backend.war) in backend liberty server's ***dropins*** folder, then it will deploy and run automatically.
4. The following step is for frontend part. Also you need to create another Liberty server for frontend. And edit the server.xml by referencing the one we provide in [*frontend_server.xml*](Minibank-JEE7-Frontend/wlp/server.xml).
	Then put [*frontend.war*](Minibank-JEE7-Backend/com.ibm.cicsdev.minibank.frontend.war) in this backend liberty server's ***dropins*** folder.
	
5. After these 4 steps above, visit <https://localhost:9080/com.ibm.cicsdev.minibank.frontend/> in your web browser. And now you can enjoy your Minibank Application!
	

***NOTE:*** 
*This is a fast way you can run your Minibank Application, but usually we don't recommand the dropins way. For standard way, you can import the projects [Minibank-JEE7-Backend](Minibank-JEE7-Backend) and [Minibank-JEE7-Frontend](Minibank-JEE7-Frontend) into your Eclipse and try to run them.*

*The 2 projects (Frontend and Backend) should be deployed on different Liberty servers. *

*Besides, in our examples, we use the default port 9080 in frontend server and port 9381 in backend server. For Derby, we use its default port 1527, of course you can use your own ports instead of them but don't forget to change the relative configurations. If you change your backend server's port, please make it consistent with your frontend for URL value at [IConstants.java](Minibank-JEE7-Frontend/src/com/ibm/cicsdev/minibank/frontend/util/IConstants.java) .*


### To port the samples in CICS Liberty
#### To add the resources to Eclipse:
1. Using an Eclipse development environment and import both frontend and backend as dynamic web projects.

2. Add the CICS Liberty JVM server libraries to the build path of your project. 

3. Ensure the web project is targeted to compile at a level that is compatible with the Java level being used on CICS. This can be achieved by editing the Java Project Facet in the project properties.

4. Create 2 CICS bundle projects called com.ibm.cicsdev.minibank.frontend and com.ibm.cicsdev.minibank.backend for the 2 projects and add the dynamic web projects include for the projects created in step 1.

#### To start a JVM server in CICS:
1. Enable Java support in the CICS region by adding the `SDFJAUTH` library to the `STEPLIB` concatenation and setting `USSHOME` and the `JVMPROFILEDIR` SIT parameters.

4. Define a Liberty JVM server called `DFHWLP` using the supplied sample definition DFHWLP in the CSD group `DFH$WLP`.

3. Copy the CICS sample `DFHWLP.jvmprofile` zFS file to the `JVMPROFILEDIR` directory specified above and ensure the `JAVA_HOME` variable is set correctly.

4. Add the JEE Liberty feature to server.xml by referencing to the [*backend_server.xml*](Minibank-JEE7-Backend/wlp/server.xml)  file we provide.

5. Install the `DFHWLP` resource defined in step 2 and ensure it becomes enabled.

6. Do the samething for the **frontend** so that we got 2 CICS with Liberty server running.

#### To create DB2 databse in CICS

Create the DB2 Database using the JCL file [*Minibank_DDL_DB2.jcl*](DB-Tables/Minibank_DDL_DB2.jcl) we provide.

#### To deploy the samples in 2 CICS regions:
1. Using the **CICS Explorer** export the CICS bundle project to a zFS directory.

2. Create a `CICS BUNDLE` definition referencing the zFS directory created in step 1.

3. Install the `CICS BUNDLE` resource.

4. Do the steps both for backend and frontend in 2 CICS regions.

***Note:*** *As your environement now is CICS, so you need to change your port settings in the projects. And also change your datasource to DB2 instead of Derby.*


#### Running the Example

Using a web browser you can visit your Minibank in CICS by the path <https://host:port/com.ibm.cicsdev.minibank.frontend/> in your web browser.

Replace the **host** and ***port*** with the host and port on the Liberty server which run your *frontend*.

And now you got your Minibank Application in CICS!

###Welcome contribution!
