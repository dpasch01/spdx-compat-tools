SPDX License Compatibility RESTful Service
==========================================

----
## what is SPDX?
see [SPDX](https://spdx.org/about-spdx)

> The Software Package Data Exchange® (SPDX®) specification is a standard format for communicating the components, licenses and copyrights associated with a software package. The vision of SPDX is achieve license compliance with minimal cost across the supply chain. Ideally, upstream component developers begin the process by supplying SPDX flies as part of their downloads. Users of those components therefore have a starting point for the SPDX files they create for their "customers," and so on. If everything is working properly, the provenance of each piece of code is researched and documented only once during its journey through a supply chain, and that information is passed on in parallel with the code in the SPDX format.

----
## Requirements
1. Java JRE 1.7 or higher
2. Apache Tomcat 7.0
3. Apache Maven
4. A Java IDE, preferably Eclipse IDE

----
## Installation
1. Clone the repository in you workspace.
2. Open Eclipse IDE and select **import -> Existing Projects into Workspace -> Select root directory -> Browse in the directory of the repository -> Finish**. 
3. If an error appears on Dynamic Web Module project facet just go to project's properties, select **Projects Facets** and uncheck the **Dynamic Web Module**.
4. Go to **Maven -> Update Project**.
5. If Apache Tomcat is not at you servers panel then **Add New Server** and select **Tomcat v7.0 Server**, configure its runtime environment and click **Next**. 
6. Select **Add and Remove** on the Apache Tomcat 7.0 and add the project's name from available to configured.
7. You are almost ready. Lastly go to **ac.ucy.cs.spdx.compatibility.ServerPath** and manually add the absolute path to the **graph.dot** file.
8. Now right click on the server and press **Start**.

----
## Usage
Everything is available under *http://localhost:8080/SPDXLicenseCompatibility/rest/*