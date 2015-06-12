package ac.ucy.cs.spdx.service;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringEscapeUtils;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ac.ucy.cs.spdx.exception.SpdxLicensePairConflictError;
import ac.ucy.cs.spdx.exception.ViolationAnalysisInfo;
import ac.ucy.cs.spdx.parser.CaptureLicense;
import ac.ucy.cs.spdx.parser.ParseRdf;

import com.fasterxml.jackson.databind.JsonNode;

@Path("/spdx/")
public class SpdxViolationAnalysis {
	private static String lastCorrected;
	
	@POST
	@Path("/analyze/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String analyzeSpdx(JsonNode spdxFilesContent) {
		
		ArrayList<SpdxLicensePairConflictError> fileNames = new ArrayList<SpdxLicensePairConflictError>();

		for (JsonNode fileNode : spdxFilesContent.get("files")) {
			String fileName = fileNode.get("filename").toString();
			fileName = fileName.substring(1, fileName.length() - 1);
			String content = fileNode.get("content").toString();
			content = StringEscapeUtils.unescapeXml(content);
			content = content.substring(1, content.length() - 1);
			try {
				fileNames.add(new SpdxLicensePairConflictError(
						new CaptureLicense(ParseRdf.parseToRdf(fileName,
								content))));
			} catch (IOException | InvalidLicenseStringException
					| InvalidSPDXAnalysisException e) {
				e.printStackTrace();
			}
		}

		ViolationAnalysisInfo analysis = new ViolationAnalysisInfo(
				fileNames.toArray(new SpdxLicensePairConflictError[fileNames
						.size()]));

		return analysis.toJson();//{"count":"","files":[{"filename":"","content":""}]}
	}

	@POST
	@Path("/validate/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String validateSpdx(JsonNode fileNode) {

		String fileName = fileNode.get("filename").toString();
		fileName = fileName.substring(1, fileName.length() - 1);
		String content = fileNode.get("content").toString();
		content = StringEscapeUtils.unescapeXml(content);
		content = content.substring(1, content.length() - 1);

		SpdxLicensePairConflictError analysis = null;
		try {
			analysis = new SpdxLicensePairConflictError(new CaptureLicense(
					ParseRdf.parseToRdf(fileName, content)));
		} catch (InvalidLicenseStringException | IOException
				| InvalidSPDXAnalysisException e) {
			e.printStackTrace();
		}

		return analysis.toJson();//{"filename":"","content":""}
	}
	
	@GET
	@Path("/download/")
	@Produces(MediaType.TEXT_XML)
	public Response downloadCorrected() throws Exception {	
		File xmlFile=new File(getLastCorrected());
		
		ResponseBuilder response = Response.ok((Object) xmlFile);
		response.header("Content-Disposition", "attachment; filename="
				+ xmlFile.getName());
		return response.build();
	}
	
	@POST
	@Path("/correct/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_XML)
	public Response correctSpdx(JsonNode fileNode) throws Exception {

		String fileName = fileNode.get("filename").toString();
		fileName = fileName.substring(1, fileName.length() - 1);

		final String LICENSE_HTML = "http://spdx.org/licenses/";

		String contentXML = fileNode.get("content").toString();
		contentXML = StringEscapeUtils.unescapeXml(contentXML);
		contentXML = contentXML.substring(1, contentXML.length() - 1);

		String newDeclared = fileNode.get("declared").toString();
		newDeclared = newDeclared.substring(1, newDeclared.length() - 1);

		String fullpath = ParseRdf.parseToRdf(fileName, contentXML);
		setLastCorrected(fullpath);
		
		File xmlFile = new File(fullpath);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xmlFile);
				
		if(doc.getElementsByTagName("licenseDeclared").item(0).getAttributes()
				.getNamedItem("rdf:resource")==null){
			Element e= (Element)doc.getElementsByTagName("licenseDeclared").item(0);
			e.setAttribute("rdf:resource", LICENSE_HTML + newDeclared);
		}else{
			doc.getElementsByTagName("licenseDeclared").item(0).getAttributes()
			.getNamedItem("rdf:resource")
			.setNodeValue(LICENSE_HTML + newDeclared);
		}
		
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		PrintWriter writer = new PrintWriter(xmlFile);
		writer.print("");
		writer.close();

		StreamResult result = new StreamResult(xmlFile);
		
		transformer.transform(source, result);

		ResponseBuilder response = Response.ok((Object) xmlFile);
		response.header("Content-Disposition", "attachment; filename="
				+ fileName);
		return response.build();// {"filename":"anomos","declared":"Apache-2.0","content":""}

	}

	/**
	 * @return the lastCorrected
	 */
	public static String getLastCorrected() {
		return lastCorrected;
	}

	/**
	 * @param lastCorrected the lastCorrected to set
	 */
	public static void setLastCorrected(String lastCorrected) {
		SpdxViolationAnalysis.lastCorrected = lastCorrected;
	}
}
