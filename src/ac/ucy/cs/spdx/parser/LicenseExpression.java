package ac.ucy.cs.spdx.parser;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * LicenseExpression class provides us with an object containing the declared
 * license or licenses of an SPDXPackage, and whether or not those licenses are
 * conjunctive or disjunctive.
 * 
 * @version 1.2
 * @author dpasch01
 */
public class LicenseExpression {
	private ArrayList<String> licenses;
	private boolean disjunctive;

	/**
	 * Tells us whether or not the expression describes conjunctive or
	 * disjunctive licenses of the SPDXPackage.
	 * 
	 * @return boolean
	 */
	public boolean isDisjunctive() {
		return disjunctive;
	}

	/**
	 * Static method that receives a String of the license expression and
	 * returns a LicenseExpression object of that String.
	 * 
	 * @param String
	 * @return {@link LicenseExpression}
	 */
	public static LicenseExpression parseExpression(String licenseExp) {
		LicenseExpression expObject = new LicenseExpression();
		String[] exportedLicenses;

		licenseExp = licenseExp.replace(")", "");
		licenseExp = licenseExp.replace("(", "");

		if (licenseExp.contains(" AND ")) {
			expObject.disjunctive = false;
			exportedLicenses = licenseExp.split(" AND ");
		} else {
			expObject.disjunctive = true;
			exportedLicenses = licenseExp.split(" OR ");
		}

		expObject.addLicenses(exportedLicenses);
		return expObject;
	}

	/**
	 * Default constructor of the {@link LicenseExpression} class
	 * 
	 * @return {@link LicenseExpression}
	 */
	public LicenseExpression() {
		this.licenses = new ArrayList<String>();
		this.disjunctive = false;
	}

	/**
	 * Adds the licenses table passed as parameter in the licenses
	 * {@link ArrayList} of the current LicenseExpression object.
	 * 
	 * @param String
	 *            []
	 */
	public void addLicenses(String[] licenses) {
		this.licenses.addAll(Arrays.asList(licenses));
	}

	/**
	 * Returns the {@link ArrayList} of the current LicenseExpression object.
	 * 
	 * @return {@link ArrayList}
	 */
	public ArrayList<String> getLicenses() {
		return this.licenses;
	}
	
	/**
	 * @return the declaredLicenses String[]
	 */
	public String[] getLicensesArray() {
		return this.licenses.toArray(new String[licenses.size()]);
	}

}
