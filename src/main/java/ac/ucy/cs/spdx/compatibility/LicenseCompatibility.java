package ac.ucy.cs.spdx.compatibility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.SimpleDirectedGraph;

import ac.ucy.cs.spdx.exception.LicenseNodeNotFoundException;
import ac.ucy.cs.spdx.graph.LicenseEdge;
import ac.ucy.cs.spdx.graph.LicenseGraph;
import ac.ucy.cs.spdx.graph.LicenseNode;
import ac.ucy.cs.spdx.license.License;

public class LicenseCompatibility {
	/**
	 * It checks the List of edges passed as parameters for the presence of a
	 * nonTransive edge. There can be only one nonTransive edge in the List and
	 * that one must be the last. If these specifications are met, the method
	 * returns true, otherwise false.
	 * 
	 * @param List
	 *            <LicenseEdge>
	 * @return boolean
	 */
	public static boolean checkEdgesForTransitive(List<LicenseEdge> edgeList) {
		for (int i = 0; i < edgeList.size() - 1; i++) {
			if (edgeList.get(i).isTransitive())
				return false;
		}
		return true;
	}

	public static Set<String> transitiveBfs(SimpleDirectedGraph<LicenseNode, LicenseEdge> graph, String startVertex) {
		Set<String> visitedNodes = new LinkedHashSet<String>();
		Set<String> visitedNonTransitiveNodes = new LinkedHashSet<String>();

		Queue<String> queue = new LinkedList<String>();
		queue.add(startVertex);
		visitedNodes.add(startVertex);
		while (!queue.isEmpty()) {
			LicenseNode node = LicenseNode.findLicenseNode(queue.remove());
			Iterator<LicenseEdge> children = graph.outgoingEdgesOf(node).iterator();
			while (children.hasNext()) {
				LicenseEdge edge = children.next();
				String nextNode = edge.getV2().getNodeIdentifier();
				if (!edge.isTransitive()) {
					visitedNonTransitiveNodes.add(nextNode);
				} else if (!visitedNodes.contains(nextNode)) {
					visitedNodes.add(nextNode);
					queue.add(nextNode);
				}

			}
		}
		visitedNodes.addAll(visitedNonTransitiveNodes);
		return visitedNodes;
	}

	public static Set<String> bfs(SimpleDirectedGraph<LicenseNode, LicenseEdge> graph, String startVertex) {
		Set<String> visitedNodes = new LinkedHashSet<String>();
		Queue<String> queue = new LinkedList<String>();
		queue.add(startVertex);
		visitedNodes.add(startVertex);
		while (!queue.isEmpty()) {
			LicenseNode node = LicenseNode.findLicenseNode(queue.remove());
			Iterator<LicenseEdge> children = graph.outgoingEdgesOf(node).iterator();
			while (children.hasNext()) {
				LicenseEdge edge = children.next();
				String nextNode = edge.getV2().getNodeIdentifier();
				if (!visitedNodes.contains(nextNode)) {
					visitedNodes.add(nextNode);
					queue.add(nextNode);
				}

			}
		}
		return visitedNodes;
	}

	public static boolean areCompatible(String... licenses) {

		for (String license1 : licenses) {
			for (String license2 : licenses) {
				try {
					if (!areCompatible(license1, license2)) {
						return false;
					}
				} catch (LicenseNodeNotFoundException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return true;
	}

	/**
	 * It checks if the two license identifiers passed as parameters are
	 * compatible with each other. It checks that by getting the licenseNode
	 * that matches the license identifiers and calculates all the possible
	 * paths between them in the licenseGraph. If one of paths that have been
	 * found returns true in the checkEdgesForTransitive then they are
	 * compatible, else if all the paths are exhausted it returns false.
	 * 
	 * @param String
	 * @param String
	 * @return boolean
	 * @throws LicenseNodeNotFoundException
	 */
	public static boolean areCompatible(String licenseIdentifier1, String licenseIdentifier2)
			throws LicenseNodeNotFoundException {
		LicenseNode v1 = null;
		LicenseNode v2 = null;

		for (LicenseNode ln : LicenseNode.getLicenseNodes()) {
			if (ln.containsLicense(licenseIdentifier2)) {
				v2 = ln;
			}
			if (ln.containsLicense(licenseIdentifier1)) {
				v1 = ln;
			}
		}

		if (v1 == null) {
			throw new LicenseNodeNotFoundException(licenseIdentifier1 + " not found in any license node.");
		}

		if (v2 == null) {
			throw new LicenseNodeNotFoundException(licenseIdentifier2 + " not found in any license node.");
		}

		if (v1.equals(v2)) {
			return true;
		}

		Set<String> v1Visited = transitiveBfs(LicenseGraph.licenseGraph, v1.getNodeIdentifier());
		Set<String> v2Visited = transitiveBfs(LicenseGraph.licenseGraph, v2.getNodeIdentifier());
		if (v1Visited.contains(v2.getNodeIdentifier())) {
			return true;
		} else if (v2Visited.contains(v1.getNodeIdentifier())) {
			return true;
		}

		return false;
	}

	/**
	 * It checks if the two license identifiers passed as parameters have an
	 * identical compatible license based on a traversal of the licenseGrap.
	 * Each time the traversal finds a possible license, it adds it in the
	 * proposals List. After all the licenses are exhausted, it returns the
	 * proposal List.
	 * 
	 * @param String
	 * @param String
	 * @return ArrayList<License>
	 */
	public static ArrayList<License> proposeLicense(String licenseIdentifier1, String licenseIdentifier2) {
		HashSet<License> proposals = new HashSet<License>();
		for (License l : License.getLicenses()) {
			try {
				if (areCompatible(licenseIdentifier1, l.getIdentifier())) {
					if (areCompatible(licenseIdentifier2, l.getIdentifier()))
						proposals.add(l);
				}
			} catch (LicenseNodeNotFoundException e) {
				e.printStackTrace();
				continue;
			}
		}

		return new ArrayList<License>(proposals);
	}

	/**
	 * Acts the same as proposeLicense(String license1,String license2), but
	 * instead of only two license identifiers, it accepts a series of String
	 * license identifiers and propose licenses compatible with all of them.
	 * 
	 * @param String
	 *            []
	 * @return ArrayList<License>
	 * @throws LicenseNodeNotFoundException 
	 */
	public static ArrayList<License> proposeLicense(String... declared){
		Set<String> proposals = new HashSet<String>();
		ArrayList<License> licensearray = new ArrayList<License>();
		LicenseNode v1 = null;

		int i = 0;
		for (String licenseIdentifier1 : declared) {
			for (LicenseNode ln : LicenseNode.getLicenseNodes()) {
				if (ln.containsLicense(licenseIdentifier1)) {
					v1 = ln;
				}
			}

			if (v1 == null) {
				try {
					throw new LicenseNodeNotFoundException(licenseIdentifier1 + " not found in any license node.");
				} catch (LicenseNodeNotFoundException e) {
					continue;
				}
			}

			Set<String> v1Visited = transitiveBfs(LicenseGraph.licenseGraph, v1.getNodeIdentifier());

			if (i == 0) {
				proposals.addAll(v1Visited);
			} else {

				proposals.retainAll(v1Visited);
			}

			if (proposals.isEmpty()) {
				return new ArrayList<License>();
			}
			i++;
		}

		proposals.removeAll(Arrays.asList(declared));
		for (String liString : proposals) {
			licensearray.add(License.findLicense(liString));
		}
		return licensearray;
	}
}
