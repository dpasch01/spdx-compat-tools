package ac.ucy.cs.spdx.compatibility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;

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

	public static boolean areCompatible(String... licenses) {

		for (String license1 : licenses) {
			for (String license2 : licenses) {
				try {
					if (!areCompatible(license1, license2)) {
						if (!areCompatible(license2, license1)) {
							return false;
						}
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
	public static boolean areCompatible(String licenseIdentifier1,
			String licenseIdentifier2) throws LicenseNodeNotFoundException {
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
			throw new LicenseNodeNotFoundException(licenseIdentifier1
					+ " not found in any license node.");
		}

		if (v2 == null) {
			throw new LicenseNodeNotFoundException(licenseIdentifier2
					+ " not found in any license node.");
		}

		if (v1.equals(v2)) {
			return true;
		}

		List<GraphPath<LicenseNode, LicenseEdge>> paths = null;
		try {
			paths = new KShortestPaths<LicenseNode, LicenseEdge>(
					LicenseGraph.licenseGraph, v1, Integer.MAX_VALUE)
					.getPaths(v2);
		} catch (IllegalArgumentException iae) {
			return false;
		}

		if (paths != null) {
			int pathsWithClosure = 0;

			for (GraphPath<LicenseNode, LicenseEdge> gp : paths) {
				List<LicenseEdge> edgeList = gp.getEdgeList();

				if (edgeList.size() > 1) {
					for (LicenseEdge le : edgeList)
						if (le.isTransitive()) {
							pathsWithClosure++;
							break;
						}
				}
			}

			if (pathsWithClosure == paths.size()) {
				for (GraphPath<LicenseNode, LicenseEdge> gp : paths) {
					if (checkEdgesForTransitive(gp.getEdgeList()))
						return true;
				}
			} else
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
	public static ArrayList<License> proposeLicense(String licenseIdentifier1,
			String licenseIdentifier2) {
		HashSet<License> proposals = new HashSet<>();
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
	 */
	public static ArrayList<License> proposeLicense(String... declared) {
		HashSet<License> proposals = new HashSet<>();
		boolean areCompatible = false;

		outerloop: for (License l : License.getLicenses()) {
			areCompatible = true;
			for (String declaredLicense : declared) {
				try {
					if (!areCompatible(declaredLicense, l.getIdentifier())) {
						areCompatible = false;
						continue outerloop;
					}
				} catch (LicenseNodeNotFoundException e) {
					e.printStackTrace();
					continue;
				}
			}
			if (areCompatible) {
				proposals.add(l);
			}
		}

		return new ArrayList<License>(proposals);
	}
}
