package ac.ucy.cs.spdx.compatibility;

import ac.ucy.cs.spdx.dot.DotPath;
import ac.ucy.cs.spdx.exception.LicenseNodeNotFoundException;
import ac.ucy.cs.spdx.graph.LicenseGraph;

public class Main {

	public static void main(String[] args) {
		LicenseGraph.initialize(DotPath.GRAPHDOT_PATH);

		try {
			if (LicenseCompatibility.areCompatible("LGPL-2.1", "Apache-2.0")) {
				System.out.println("Yes they are.");
			} else {
				System.out.println("No they are not.");
				System.out.println(LicenseCompatibility.proposeLicense(
						"LGPL-2.1", "Apache-2.0"));
			}
		} catch (LicenseNodeNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println(LicenseCompatibility.proposeLicense("LGPL-2.1",
				"OSL-3.0", "Artistic-2.0"));

		System.out.println();

		if (LicenseCompatibility.areCompatible("GPL-3.0", "Libpng", "MPL-2.0")) {
			System.out.println("Yes they are.");
		} else {
			System.out.println("No they are not.");

		}

	}
}
