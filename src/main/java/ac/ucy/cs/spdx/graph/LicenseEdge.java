package ac.ucy.cs.spdx.graph;

import java.util.HashSet;

import org.jgrapht.graph.DefaultEdge;

import ac.ucy.cs.spdx.exception.LicenseEdgeAlreadyExistsException;

/**
 * This is License Edge class. It is custom graph edge implementation that
 * extends standard jgraph's DefaultEdge. It will be used in the labeled license
 * graph to separate transitive edges with non-transitive ones by the use of a
 * boolean field nonTransitive.
 * 
 * @author dpasch01
 * @version 1.2
 */
@SuppressWarnings("serial")
public class LicenseEdge extends DefaultEdge {
	private static HashSet<LicenseEdge> licenseEdges = new HashSet<LicenseEdge>();
	private LicenseNode v1;
	private LicenseNode v2;
	private boolean nonTransitive;

	/**
	 * Constructor for License Edge. Returns a {@link LicenseEdge} object from
	 * {@link LicenseNode} v1 to {@link LicenseNode} v2 and their transitivity
	 * status(true or false).
	 * 
	 * @param LicenseNode
	 * @param LicenseNode
	 * @param boolean
	 * @return {@link LicenseEdge}
	 * @throws LicenseEdgeAlreadyExistsException
	 */
	public LicenseEdge(LicenseNode v1, LicenseNode v2, boolean nonTransitive)
			throws LicenseEdgeAlreadyExistsException {
		if (LicenseEdge.edgeExists(v1.getNodeIdentifier(),
				v2.getNodeIdentifier())) {
			throw new LicenseEdgeAlreadyExistsException(v1 + " -> " + v2
					+ " already exists in the system.");
		}
		this.setV1(v1);
		this.setV2(v2);
		this.setTransitive(nonTransitive);
		licenseEdges.add(this);
	}

	/**
	 * Constructor for License Edge. Returns a {@link LicenseEdge} object from
	 * String v1 toString v2 and their transitivity status(true or false).
	 * 
	 * @param String
	 * @param String
	 * @param boolean
	 * @return {@link LicenseEdge}
	 * @throws LicenseEdgeAlreadyExistsException
	 */
	public LicenseEdge(String v1, String v2, boolean nonTransitive)
			throws LicenseEdgeAlreadyExistsException {
		if (LicenseEdge.edgeExists(v1, v2)) {
			throw new LicenseEdgeAlreadyExistsException(v1 + " -> " + v2
					+ " already exists in the system.");
		}
		this.setV1(LicenseNode.findLicenseNode(v1));
		this.setV2(LicenseNode.findLicenseNode(v2));
		this.setTransitive(nonTransitive);
		licenseEdges.add(this);
	}

	/**
	 * Returns true if this edge is Transitive.
	 * 
	 * @return boolean
	 */
	public boolean isTransitive() {
		return nonTransitive;
	}

	/**
	 * Sets true if this edge is Transitive.
	 * 
	 * @pass boolean
	 */
	public void setTransitive(boolean transitive) {
		this.nonTransitive = transitive;
	}

	/**
	 * Getter for the second license node.
	 * 
	 * @return LicenseNode
	 */
	public LicenseNode getV2() {
		return v2;
	}

	/**
	 * Setter for the second license node.
	 * 
	 * @param LicenseNode
	 */
	public void setV2(LicenseNode v2) {
		this.v2 = v2;
	}

	/**
	 * Getter for the first license node.
	 * 
	 * @return LicenseNode
	 */
	public LicenseNode getV1() {
		return v1;
	}

	/**
	 * Setter for the first license node.
	 * 
	 * @param LicenseNode
	 */
	public void setV1(LicenseNode v1) {
		this.v1 = v1;
	}

	/**
	 * toString() method for {@link LicenseEdge}
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return this.v1 + " -> " + this.v2;
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
