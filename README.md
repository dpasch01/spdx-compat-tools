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

#Graph expansion resources

| Path                 | Method | Request                                                                                         | Response                                                                                                                        |
|----------------------|--------|-------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
| /license/graph/      | GET    | -                                                                                               | Download graph.dot file                                                                                                         |
| /license/nodes/      | GET    | -                                                                                               | [[CDDL-1.1, CDDL-1.0], [Apache-1.0],..., [BSD-2-Clause-FreeBSD]]                                                                |
| /license/node/       | POST   | {"nodeIdentifier":"AAL","nodeCategory":"WEAK_COPYLEFT","nodelicenses":[{"identifier":"AAL"}]}   | {"status":"success","message":"AAL added in the system."}                                                                       |
| /license/compatible/ | POST   | {"licenses":[{"identifier":"Apache-2.0"},{"identifier":"MPL-2.0"}] }                            | {,"compatible": "true","adjustable": "false","proposals": [0] }                                                                 |
| /license/edges/      | GET    | -                                                                                               | [[Apache-2.0] -> [LGPL-3.0+, LGPL-3.0], [MIT, X11] -> [BSD-2-Clause-FreeBSD],..., [LGPL-3.0+, LGPL-3.0] -> [GPL-3.0, GPL-3.0+]] |
| /license/edge/       | POST   | {"nodeIdentifier":"APSL-1.0","transitivity":true,"nodeIdentifiers":[{"identifier":"AGPL-1.0"}]} | {"status":"success","message":"APSL-1.0 -> [AGPL-1.0] added in the system."}                                                    |
| /license/licenses/   | GET    | -                                                                                               | {"licenses":[{"identifier":"GPL-2.0+"},{"identifier":"BSD-2-Clause-FreeBSD"},...,{"identifier":"X11"}]}                         |

#SPDX compatibility resources

| Path            | Method | Request                                                                                             | Response                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
|-----------------|--------|-----------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| /spdx/analyze/  | POST   | {"filename":"The-name-of-spdx-file.rdf-or-spdx","content":"content-of-file"}                        | {"file":"anomos","extracted":[{"identifier":"GPL-3.0"},...,{"identifier":"GPL"}],"compatible":true,"adjustable":true,"proposal":[]}                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| /spdx/validate/ | POST   | {"count":2,"files":[{"filename":"ckeditor","content":".."},{"filename":"ckeditor","content":".."}]} | {"unaries":[{"file":"ckeditor","extracted":{"identifier":"LGPL-2.1+"},...,{"identifier":"MPL-1.1"}],"declared":[{"identifier":"LGPL-3.0"},{"identifier":"MPL-2.0"},{"identifier":"GPL-3.0"}],"compatible":true,"adjustable":true,"proposal":[]},{"file":"ckeditor","extracted":{"identifier":"LGPL-2.1+"},...,{"identifier":"MPL-1.1"}],"declared":[{"identifier":"LGPL-3.0"},{"identifier":"MPL-2.0"},{"identifier":"GPL-3.0"}],"compatible":true,"adjustable":true,"proposal":[]}],"compatible":false,"adjustable":false,"proposal":[]}],"violation":false,"adjustable":false,"proposal":[]} |
| /spdx/download/ | GET    | -                                                                                                   | Download corrected spdx file.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| /spdx/correct/  | POST   | {"filename":"anomos","declared":"Apache-2.0","content":""}                                          | Download corrected spdx file.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |