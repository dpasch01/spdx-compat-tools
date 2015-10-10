package ac.ucy.cs.spdx.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import ac.ucy.cs.spdx.compatibility.LicenseCompatibility;
import ac.ucy.cs.spdx.dot.DotPath;
import ac.ucy.cs.spdx.exception.LicenseEdgeAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseNodeAlreadyExistsException;
import ac.ucy.cs.spdx.graph.LicenseGraph;
import ac.ucy.cs.spdx.graph.LicenseNode;
import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.license.License.Category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/license/")
public class Compatibility {

	@GET
	@Path("/graph/")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getDotFile() throws Exception {

		File dotFile = new File(DotPath.GRAPHDOT_PATH);

		ResponseBuilder response = Response.ok((Object) dotFile);
		response.header("Content-Disposition", "attachment; filename="
				+ DotPath.GRAPHDOT_PATH);
		return response.build();

	}

	@GET
	@Path("/nodes/")
	@Produces(MediaType.TEXT_PLAIN)
	public String getNodes() {
		return LicenseNode.getLicenseNodes().toString();
	}

	@GET
	@Path("/edges/")
	@Produces(MediaType.TEXT_PLAIN)
	public String getEdges() {
		return LicenseGraph.licenseEdges.toString();
	}

	@POST
	@Path("/compatible/")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String areCompatible(String jsonString) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode fileNode = null;
		try {
			fileNode = mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> licenses = new ArrayList<String>();
		JsonNode licensesJSON = fileNode.get("licenses");
		StringBuilder compatibleJSON = new StringBuilder();

		for (int i = 0; i < licensesJSON.size(); i++) {
			String licenseId = licensesJSON.get(i).get("identifier").toString();
			licenseId = licenseId.substring(1, licenseId.length() - 1);
			licenses.add(licenseId);
		}

		boolean compatible = LicenseCompatibility.areCompatible(licenses
				.toArray(new String[licenses.size()]));
		boolean adjustable = true;
		ArrayList<License> proposed = new ArrayList<License>();

		if (!compatible) {
			LicenseCompatibility.proposeLicense(licenses
					.toArray(new String[licenses.size()]));
		}

		if (proposed.isEmpty()) {
			adjustable = false;
		}

		compatibleJSON.append("{\"compatible\":\"" + compatible
				+ "\",\"adjustable\":\"" + adjustable + "\",\"proposals\":[");
		for (License proposal : proposed) {
			compatibleJSON.append("{\"identifier\":\""
					+ proposal.getIdentifier() + "\"},");
		}

		if (adjustable) {
			compatibleJSON.deleteCharAt(compatibleJSON.length() - 1);
		}

		compatibleJSON.append("]}");
		return compatibleJSON.toString();// {"licenses":[{"identifier":"Apache-2.0"},{"identifier":"MPL-2.0"}]}
	}

	@POST
	@Path("/node/")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String addNode(String jsonString) {
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode licenseNode = null;
		try {
			licenseNode = mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> licenses = new ArrayList<String>();
		String nodeIdentifier = licenseNode.get("nodeIdentifier").toString();
		nodeIdentifier = nodeIdentifier.substring(1,
				nodeIdentifier.length() - 1);

		String nodeCategory = licenseNode.get("nodeCategory").toString();
		nodeCategory = nodeCategory.substring(1, nodeCategory.length() - 1);
		Category category = Category.UNCATEGORIZED;

		if(nodeCategory=="PERMISSIVE") {
			category = Category.PERMISSIVE;
		}else if(nodeCategory=="WEAK_COPYLEFT") {
			category = Category.WEAK_COPYLEFT;
		}else if(nodeCategory=="STRONG_COPYLEFT") {
			category = Category.STRONG_COPYLEFT;
		}else{
			category = Category.UNCATEGORIZED;
		}

		JsonNode licensesJSON = licenseNode.get("nodelicenses");

		for (int i = 0; i < licensesJSON.size(); i++) {
			String licenseId = licensesJSON.get(i).get("identifier").toString();
			licenseId = licenseId.substring(1, licenseId.length() - 1);
			licenses.add(licenseId);
		}

		try {
			LicenseGraph.addLicenseNode(nodeIdentifier, category,
					licenses.toArray(new String[licenses.size()]));
		} catch (LicenseNodeAlreadyExistsException e) {
			e.printStackTrace();
			return "{\"status\":\"failure\",\"message\":\"" + e.getMessage()
					+ "\"}";
		}

		LicenseGraph.exportGraph();

		return "{\"status\":\"success\",\"message\":\"" + nodeIdentifier
				+ " added in the system.\"}";// {"nodeIdentifier":"Caldera","nodeCategory":"PERMISSIVE","nodelicenses":[{"identifier":"Caldera"}]}
	}

	@POST
	@Path("/edge/")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public String addEdge(String jsonString) {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode licenseEdge = null;
		try {
			licenseEdge = mapper.readTree(jsonString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> licenseNodes = new ArrayList<String>();
		String nodeIdentifier = licenseEdge.get("nodeIdentifier").toString();
		nodeIdentifier = nodeIdentifier.substring(1,
				nodeIdentifier.length() - 1);

		String transitivity = licenseEdge.get("transitivity").toString();
		transitivity = transitivity.substring(1, transitivity.length() - 1);
		Boolean isTransitive = Boolean.parseBoolean(transitivity);

		JsonNode nodesJSON = licenseEdge.get("nodeIdentifiers");

		for (int i = 0; i < nodesJSON.size(); i++) {
			String node = nodesJSON.get(i).get("identifier").toString();
			node = node.substring(1, node.length() - 1);
			licenseNodes.add(node);
		}

		try {
			LicenseGraph.connectNode(isTransitive, nodeIdentifier,
					licenseNodes.toArray(new String[licenseNodes.size()]));
		} catch (LicenseEdgeAlreadyExistsException e) {
			e.printStackTrace();
			return "{\"status\":\"failure\",\"message\":\"" + e.getMessage()
					+ "\"}";
		}

		LicenseGraph.exportGraph();

		return "{\"status\":\"success\",\"message\":\"" + nodeIdentifier
				+ " -> " + licenseNodes.toString() + " added in the system.\"}";//{"nodeIdentifier":"Caldera","transitivity":"true","nodeIdentifiers":[{"identifier":"Apache-2.0"}]}
	}
	
	@GET
	@Path("/licenses/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLicenses() {
		return License.toJson();
	}
}
