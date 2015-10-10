package ac.ucy.cs.spdx.dot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class DotFile {
	public String path;
	public String content;
	private HashMap<Integer, String> nodeIdentifiers;
	private HashMap<String, String[]> nodeLicenseContent;
	private ArrayList<DotEdge> edgeIdentifiers;

	public DotFile(String path) throws IOException {
		this.path = path;
		this.content = new String(Files.readAllBytes(new File(path).toPath()));
		this.nodeIdentifiers = new HashMap<Integer, String>();
		this.nodeLicenseContent = new HashMap<String, String[]>();
		this.edgeIdentifiers = new ArrayList<DotEdge>();

		String[] dotLines = this.content.split("\\n");

		for (String line : dotLines) {

			int nodeStart = line.indexOf("\"[");

			if (nodeStart >= 0) {
				nodeStart += 2;
				
				int nodeEnd = line.indexOf("]\"");
				String nodeLicenses = (String) line.subSequence(nodeStart,
						nodeEnd);
		
				String[] lIdentifiers = nodeLicenses.split(", ");

				StringBuilder nodeIdentifier = new StringBuilder();
				for (String li : lIdentifiers) {
					nodeIdentifier.append(li + "_");
				}

				nodeIdentifier.deleteCharAt(nodeIdentifier.length() - 1);

				String nodeId = line.substring(0, line.indexOf('[')).trim();

				this.nodeIdentifiers.put(Integer.parseInt(nodeId),
						nodeIdentifier.toString());
				this.nodeLicenseContent.put(nodeIdentifier.toString(),
						lIdentifiers);

			} else if (line.contains("->")) {

				int from, to;
				boolean isTransitive = false;
				String fromIdentifier, toIdentifier;

				line = line.trim();

				if (line.charAt(line.indexOf('"') + 1) == 'b') {
					isTransitive = true;
				}

				line = line.substring(0, line.indexOf('['));
				String[] edgeNodes = line.split("->");

				from = Integer.parseInt(edgeNodes[0].trim());
				to = Integer.parseInt(edgeNodes[1].trim());
				fromIdentifier = nodeIdentifiers.get(from);
				toIdentifier = nodeIdentifiers.get(to);

				this.edgeIdentifiers.add(new DotEdge(fromIdentifier,
						toIdentifier, isTransitive));
			
			}
		}
	}

	/**
	 * @return the nodeLicenseContent
	 */
	public HashMap<String, String[]> getNodeLicenseContent() {
		return nodeLicenseContent;
	}

	/**
	 * @param nodeLicenseContent the nodeLicenseContent to set
	 */
	public void setNodeLicenseContent(HashMap<String, String[]> nodeLicenseContent) {
		this.nodeLicenseContent = nodeLicenseContent;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the nodeIdentifiers
	 */
	public HashMap<Integer, String> getNodeIdentifiers() {
		return nodeIdentifiers;
	}

	/**
	 * @param nodeIdentifiers
	 *            the nodeIdentifiers to set
	 */
	public void setNodeIdentifiers(HashMap<Integer, String> nodeIdentifiers) {
		this.nodeIdentifiers = nodeIdentifiers;
	}

	/**
	 * @return the edgeIdentifiers
	 */
	public ArrayList<DotEdge> getEdgeIdentifiers() {
		return edgeIdentifiers;
	}

	/**
	 * @param edgeIdentifiers
	 *            the edgeIdentifiers to set
	 */
	public void setEdgeIdentifiers(ArrayList<DotEdge> edgeIdentifiers) {
		this.edgeIdentifiers = edgeIdentifiers;
	}

	public String[] getNodeLicenses(String nodeIdentifier){
		return this.nodeLicenseContent.get(nodeIdentifier);
	}
}
