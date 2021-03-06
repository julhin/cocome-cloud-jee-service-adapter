**************************************************************
* R E A D M E
**************************************************************


1. Preliminary

	- Used Server: GlassFish Server OpenSource Edition 3.1.2/4.x
	- Java Version: JDK 1.7.0_51

2. Deployment

2.1 Using Eclipse

In order to use Eclipse IDE for deploying apps following tools
have to be installed:

- Eclipse Java EE Developer Tools
- Eclipse Java EE Web Tools

These tools normally are available under the standard download
side of used eclipse.

See http://download.eclipse.org/releases/mars

After having installed tools, the server adapter is still missing.
Do following:

- Open the JavaEE Perspective
- Go to the Servers View
- Right-Click and chose "New Server"
- Choose the Glassfish Version you use and name the new server
- Choose the location of the Glassfish installation on your local computer
- Choose a domain to use for the application or create a new one by 
  clicking on the + symbols to the right of the domain path. When creating a new 
  domain, portbase + 80 will be the port to access the deployed application 
  and portbase + 48 will be the port to access the admin panel of this domain.
    
a) Deploying an application like the service adapter:

-	Right-Click on the created Web-Application Project. In case of the 
    service adapter, right click on the service-adapter-ear project.
-	Select "Run As > Run on Server" and select the server you created.

b) Undeploying an application

- 	Switch to JavaEE-Perspective
-	Select the Servers-View
-	Expand the Server-App-Tree
-	Right-Click on the app which has to be undeployed
-	Select "Remove"

2.2 Using Command-Line

- Use the glassfish3\bin\asadmin tool to deploy the application.

Open new terminal and either switch to the directory glassfish\bin or if
you have set environment variables for glassfish tools, use direct  
asadmin command.

The syntax is as following:

>> asadmin deploy war-name

i.e. asadmin deploy service-adapter-ear/bin/service-adapter-ear.ear

After deployment, access the application by http://host:port/<application-context-root>

i.e. in localhost and port 8080: http://localhost:8080/<application-context-root>

-   For undeploying an application use the glassfish3\bin\asadmin undeploy command.
    Analog to the deploy command but use undeploy instead of deploy.


2.3 Using Admin-Console:

a) For deploying a new application

-   The admin console can be accessed by a simple browser like firefox, by 
    using the url http://host:4848 (which is normally the port for administration)

    i.e. http://localhost:4848
    
- 	Click the Applications node in the tree on the left.

    The Applications page is displayed.

- 	Click the Deploy button.

    The Deploy Applications or Modules page is displayed.

- 	Select Packaged File to be Uploaded to the Server, and click Browse.

- 	Navigate to the location in which you saved the hello.war sample, select the file, and click Open.

	You are returned to the Deploy Applications or Modules page.

- 	Specify a description in the Description field.

    i.e. service-adapter

- 	Accept the other default settings, and click OK.

    You are returned to the Applications page.

- 	Select the check box next to the hello application and click the Launch link to run the application.

    The default URL for the service-adapter is: http://host:port/de.kit.ipd.cocome.cloud.serviceadapter/

    i.e. http://localhost:8080:/de.kit.ipd.cocome.cloud.serviceadapter/

b) For undeploying an application
			
    Analog to deployment

 
4. Configuration Database

4.1 Create JDBC Connection Pool

For the connection pool you must decide which database service you are going to use.
We successfully used PostgreSQL and DerbyDB for this task (see Appendix A for DerbyDB).

4.1.1 With Command-Line tool asadmin

4.1.2 With Admin-Console

- Click on JBDC-Connection Pool in the project tree
- Click on new, follow the wizard

4.2 Create a JDBC Connection Resource 

A JDBC resource connects itself to a connection pool. It has a JDNI-Name and can have properties.
For a full connection at least a jdbc connection-pool and a jdbc resource is needed. 
The default resource name for the service-adapter is jdbc/CoCoMEDB and has to be created.
This name can be changed inside the service-adapter-ejb/ejbModule/META-INF/persistence.xml.

4.2.1 With Command-Line tool asadmin

4.2.2 With Admin-Console

-   Click on "JDBC Resources" in the project tree
-	Click on "New" and follow the wizard. As connection pool take the 
	appropriated one created in 4.1
- 	As the name of the new Resource use jdbc/CoCoMEDB
 
4.3 Configure the persistence.xml in the service-adapter-ejb/ejbModule/META-INF folder to 
    use the jta-data-source that you configured or skip this step if you use the defaults.
	
		
		
	
= Appendix =

A Setup of DerbyDB

A.1 Settings before derby can be started properly

the JRE has to have the permissions to listen to the port, where derby is going
to listen (default: 1527). Following has to be done, if starting derby fails 
for security reasons:

-	find out what is the current used java version by

>java -version (in a terminal)

i.e. java version "1.7.0_51"

-	Go to the directory where the corresponding JRE resides.

jre-dir\lib\security

i.e. C:\Program Files\Java\jdk1.7.0_51\jre\lib\security

-	open the java.policy file and add this line in the main grant{...}
    block

        permission java.net.SocketPermission "localhost:1527", "listen";
        
-	save the file. This can only be done, if no other java - program is
	using this file.
			
Now try again to start derby. This time, no security problems should appear.
	

A.2 Starting DerbyDB

Once a database should be used to store data, the database has to be started.
The database has to connect to the port (default: 1527). This can be accomplished
by starting the db manually or by script. 

Eclipse provides a convenient option to start the JavaDB automatically when
the App-Server starts.

	Window>Preferences>GlassfishPreferences->see checkboxes
		
		