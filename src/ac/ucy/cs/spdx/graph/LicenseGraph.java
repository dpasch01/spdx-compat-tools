package ac.ucy.cs.spdx.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.SimpleDirectedGraph;

import ac.ucy.cs.spdx.dot.DotEdge;
import ac.ucy.cs.spdx.dot.DotFile;
import ac.ucy.cs.spdx.dot.DotPath;
import ac.ucy.cs.spdx.exception.LicenseAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseEdgeAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseNodeAlreadyExistsException;
import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.license.License.Category;

/**
 * This is License Graph class. It is a simple implementation of
 * SimpleDirectedGraph from jgrapht library. It consists of several methods of
 * traversing, creating and editing the licenseGrapht of LicenseUtils class.
 * 
 * @author dpasch01
 * @version 1.2
 */
public class LicenseGraph {

	public static SimpleDirectedGraph<LicenseNode, LicenseEdge> licenseGraph = new SimpleDirectedGraph<LicenseNode, LicenseEdge>(
			LicenseEdge.class);
	private static HashMap<String, LicenseNode> nodeIdentifiers = new HashMap<String, LicenseNode>();
	public static HashSet<LicenseEdge> licenseEdges = new HashSet<LicenseEdge>();
	private static DotFile dotFile;

	/**
	 * The main and most important function of the {@link LicenseGraph} class.
	 * It initializes the graph by parsing the .dot file, creating the nodes and
	 * connecting with the edges.
	 */
	public static void initialize(String path) {

		try {
			dotFile = new DotFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String nodeIdentifier : dotFile.getNodeIdentifiers().values()) {
			LicenseNode licenseNode = null;
			try {
				licenseNode = new LicenseNode(nodeIdentifier,
						Category.UNCATEGORIZED,
						dotFile.getNodeLicenses(nodeIdentifier));
			} catch (LicenseNodeAlreadyExistsException e) {
				e.printStackTrace();
			}
			nodeIdentifiers.put(nodeIdentifier, licenseNode);
			licenseGraph.addVertex(licenseNode);
		}

		for (DotEdge dotEdge : dotFile.getEdgeIdentifiers()) {
			LicenseNode lnFrom = LicenseNode.findLicenseNode(dotEdge.getFrom());
			LicenseNode lnTo = LicenseNode.findLicenseNode(dotEdge.getTo());
			LicenseEdge ledge = null;
			try {
				ledge = new LicenseEdge(lnFrom, lnTo, dotEdge.isTransitive());
			} catch (LicenseEdgeAlreadyExistsException e) {
				e.printStackTrace();
			}
			licenseEdges.add(ledge);

			licenseGraph.addEdge(lnFrom, lnTo, ledge);
		}

	}

	/**
	 * Exports the state of the graph in the required .dot format. This has been
	 * made possible by the use of {@link DOTExporter} object of jgrapht.
	 * 
	 */
	public static void exportGraph() {
		IntegerNameProvider<LicenseNode> p0 = new IntegerNameProvider<>();
		VertexNameProvider<LicenseNode> p1 = new VertexNameProvider<LicenseNode>() {

			@Override
			public String getVertexName(LicenseNode v) {
				return v.toString();
			}

		};

		ComponentAttributeProvider<LicenseEdge> p4 = new ComponentAttributeProvider<LicenseEdge>() {

			@Override
			public Map<String, String> getComponentAttributes(LicenseEdge e) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				if (e.isTransitive()) {
					map.put("color", "black");
				} else {
					map.put("color", "maroon");
				}
				return map;
			}
		};

		DOTExporter<LicenseNode, LicenseEdge> exporter = new DOTExporter<LicenseNode, LicenseEdge>(
				p0, p1, null, null, p4);
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(DotPath.GRAPHDOT_PATH));
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		Writer w = bw;
		exporter.export(w, licenseGraph);
	}

	/**
	 * Creates a new License Node with the Licenses passed as parameter and
	 * defines whether or not is transitive.
	 * 
	 * @param boolean
	 * @param String
	 * @param String
	 *            []
	 * @throws LicenseNodeAlreadyExistsException
	 */
	public static void addLicenseNode(String nodeIdentifier, Category category,
			String... licenseIdentifiers)
			throws LicenseNodeAlreadyExistsException {
		if (LicenseNode.NodeExists(licenseIdentifiers)) {
			throw new LicenseNodeAlreadyExistsException(nodeIdentifier
					+ " already exists in the system.");
		}
		for (String lIdentifier : licenseIdentifiers) {
			if (!License.licenseExists(lIdentifier)) {
				try {
					new License(lIdentifier, lIdentifier,
							Category.UNCATEGORIZED);
				} catch (LicenseAlreadyExistsException e) {
					e.printStackTrace();
				}
			}
		}

		LicenseNode ln = null;
		try {
			ln = new LicenseNode(nodeIdentifier, category, licenseIdentifiers);
		} catch (LicenseNodeAlreadyExistsException e) {
			e.printStackTrace();
		}
		nodeIdentifiers.put(nodeIdentifier, ln);
		licenseGraph.addVertex(ln);
	}

	/**
	 * Connects the License Node passed as parameter with the other Nodes also
	 * passed as parameters by creating new edge between them and defining
	 * whether or not is transitive
	 * 
	 * @param boolean
	 * @param String
	 * @param String
	 *            []
	 * @throws LicenseEdgeAlreadyExistsException
	 */
	public static void connectNode(boolean isTransitive, String nodeIdentifier,
			String... nodeIdentifiers) throws LicenseEdgeAlreadyExistsException {

		LicenseNode ln = LicenseNode.findLicenseNode(nodeIdentifier);
		for (int i = 0; i < nodeIdentifiers.length; i++) {
			if (edgeExists(nodeIdentifier, nodeIdentifiers[i])) {
				throw new LicenseEdgeAlreadyExistsException(nodeIdentifier + " -> " + nodeIdentifiers[i]
						+ " already exists in the system.");
			}
			LicenseEdge licenseEdge = null;
			try {
				licenseEdge = new LicenseEdge(ln,
						LicenseNode.findLicenseNode(nodeIdentifiers[i]),
						isTransitive);
			} catch (LicenseEdgeAlreadyExistsException e) {
				e.printStackTrace();
			}

			licenseEdges.add(licenseEdge);
			licenseGraph.addEdge(ln,
					LicenseNode.findLicenseNode(nodeIdentifiers[i]),
					licenseEdge);
		}
	}

	/**
	 * Prints the graph state on screen.
	 */
	public static void echoGraph() {
		for (LicenseEdge edge : licenseEdges) {
			System.out.println(edge);
		}
	}

	/**
	 * Checks whether or not, an edge passed as parameter exists and returns
	 * true if so and false if not.
	 * 
	 * @param String
	 * @param String
	 * @return boolean
	 */
	private static boolean edgeExists(String from, String to) {
		boolean exists = false;
		for (LicenseEdge le : licenseEdges) {
			if (from.equals(le.getV1().getNodeIdentifier())
					&& to.equals(le.getV2().getNodeIdentifier())) {
				return true;
			}
		}
		return exists;
	}
}
