package ac.ucy.cs.spdx.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import ac.ucy.cs.spdx.exception.LicenseAlreadyExistsException;
import ac.ucy.cs.spdx.exception.LicenseNodeAlreadyExistsException;
import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.license.License.Category;

/**
 * This is License Node class. It is the representation of vertices in the
 * licenseGraph as seen in /resources/LicenseCompatGraph.png. It consists of
 * several useful methods for creating the nodes and accessing data of them.
 * 
 * @author dpasch01
 * @version 1.2
 */

public class LicenseNode {
	private static HashSet<LicenseNode> licenseNodes = new HashSet<LicenseNode>();
	private HashSet<License> licenses;
	private String nodeIdentifier;
	private Category category;

	/**
	 * Getter for the category of the License Node.
	 * 
	 * @return Category
	 */
	public Category getCategory() {
		return this.category;
	}

	/**
	 * Constructor for the License Node based on the identifier and the licenses
	 * passed as parameters.
	 * 
	 * @param String
	 * @param License
	 *            []
	 * @throws LicenseNodeAlreadyExistsException
	 */
	public LicenseNode(String nodeIdentifier, License... licencesArray)
			throws LicenseNodeAlreadyExistsException {
		if (LicenseNode.NodeExists((String[]) LicenseNode
				.getLicenseIdentifiers(licencesArray).toArray())) {
			throw new LicenseNodeAlreadyExistsException(nodeIdentifier
					+ " already exists in the system.");
		}
		this.setNodeIdentifier(nodeIdentifier);
		this.category = licencesArray[0].getCategory();
		licenses = new HashSet<License>();
		for (License l : licencesArray) {
			licenses.add(l);
		}
		licenseNodes.add(this);
	}

	/**
	 * Constructor for the License Node based on the identifier and the licenses
	 * passed as parameters in string format.
	 * 
	 * @param String
	 * @param String
	 *            []
	 * @throws LicenseNodeAlreadyExistsException
	 */
	public LicenseNode(String nodeIdentifier, Category category,
			String... licencesArray) throws LicenseNodeAlreadyExistsException {
		if (LicenseNode.NodeExists(licencesArray)) {
			throw new LicenseNodeAlreadyExistsException(nodeIdentifier
					+ " already exists in the system.");
		}
		this.setNodeIdentifier(nodeIdentifier);
		this.category = category;
		licenses = new HashSet<License>();
		for (String l : licencesArray) {
			License license = License.findLicense(l);
			if (license == null) {
				try {
					licenses.add(new License(l, l, Category.UNCATEGORIZED));
				} catch (LicenseAlreadyExistsException e) {
					e.printStackTrace();
				}
			} else {
				licenses.add(license);
			}
		}
		licenseNodes.add(this);
	}

	/**
	 * Getter for the licenses in the License Node.
	 * 
	 * @return HashSet<License>
	 */
	public HashSet<License> getLicenses() {
		return licenses;
	}

	/**
	 * Getter for the license identifiers in the License Node.
	 * 
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getLicenseIdentifiers(
			License... licencesArray) {
		ArrayList<String> identifiers = new ArrayList<String>();
		for (License license : licencesArray) {
			identifiers.add(license.getIdentifier());
		}
		return identifiers;
	}

	/**
	 * The toString() method implementation for Violation Analysis Info object.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return new ArrayList<License>(this.licenses).toString();
	}

	/**
	 * Getter for the License Node identifier.
	 * 
	 * @return String
	 */
	public String getNodeIdentifier() {
		return nodeIdentifier;
	}

	/**
	 * Setter for the License Node identifier.
	 * 
	 * @param String
	 */
	public void setNodeIdentifier(String nodeIdentifier) {
		this.nodeIdentifier = nodeIdentifier;
	}

	/**
	 * Searches and returns the licenseNodes for the License Node with the
	 * identifier equal to the one passed as parameter.
	 * 
	 * @param String
	 * @return LicenseNode
	 */
	public static LicenseNode findLicenseNode(String nodeIdentifier) {
		LicenseNode licenseNode = null;
		for (LicenseNode ln : licenseNodes) {
			if (ln.getNodeIdentifier().equals(nodeIdentifier))
				licenseNode = ln;
		}
		return licenseNode;
	}

	/**
	 * Searches the License Node for the specified License passed as parameter
	 * and returns true or false whether it finds it or not
	 * 
	 * @param String
	 * @return boolean
	 */
	public boolean containsLicense(String licenseIdentifier) {
		License license = License.findLicense(licenseIdentifier);

		if (license == null) {
			return false;
		} else if (licenses.contains(license)) {
			return true;
		}

		return false;
	}

	/**
	 * Searches the License Nodes for the license identifiers passed as
	 * parameters and returns true or false whether it finds it or not
	 * 
	 * @param String
	 *            []
	 * @return boolean
	 */
	public static boolean NodeExists(String... lIdentifiers) {
		boolean found = false;
		for (LicenseNode ln : licenseNodes) {
			found = true;

			for (String li : lIdentifiers) {
				boolean e = ln.containsLicense(li);
				found = found && e;
			}
			if (found) {
				return found;
			}
		}

		return found;
	}

	/**
	 * Getter for the set of all the License Nodes
	 * 
	 * @return HashSet<LicenseNode>
	 */
	public static HashSet<LicenseNode> getLicenseNodes() {
		return licenseNodes;
	}

	/**
	 * Returns true if the specified License Node passed as parameter is equal
	 * to the specific License Node the this method is called upon.
	 * 
	 * @param LicenseNode
	 * @return boolean
	 */
	public boolean equals(LicenseNode ln) {
		Comparator<LicenseNode> cmp = new Comparator<LicenseNode>() {
			@Override
			public int compare(LicenseNode o1, LicenseNode o2) {
				for (License l1 : o1.getLicenses()) {
					if (!o2.containsLicense(l1.getIdentifier())) {
						return -1;
					}
				}
				return 0;
			}
		};
		if (cmp.compare(this, ln) == 0)
			return true;
		return false;
	}
}
