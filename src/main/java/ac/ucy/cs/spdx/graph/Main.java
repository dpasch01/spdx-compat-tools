package ac.ucy.cs.spdx.graph;

import ac.ucy.cs.spdx.dot.DotPath;
import ac.ucy.cs.spdx.exception.LicenseEdgeAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseNodeAlreadyExistsException;
import ac.ucy.cs.spdx.license.License.Category;

public class Main {

	public static void main(String[] args) {
		LicenseGraph.initialize(DotPath.GRAPHDOT_PATH);
		
		try {
			LicenseGraph.addLicenseNode("Entessa_Eurosym",
					Category.STRONG_COPYLEFT, "Eurosym", "Entessa");
		} catch (LicenseNodeAlreadyExistsException e) {
			e.printStackTrace();
		}

		try {
			LicenseGraph.connectNode(false, "Entessa_Eurosym", "Libpng_Zlib");
		} catch (LicenseEdgeAlreadyExistsException e) {
			e.printStackTrace();
		}

		LicenseGraph.echoGraph();

		
		LicenseGraph.exportGraph();

	}
}
