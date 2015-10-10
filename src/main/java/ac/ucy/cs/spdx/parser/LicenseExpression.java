package ac.ucy.cs.spdx.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
	public static final String SAME_AS = "Same-license-as";
	public static final String NO_LICENSE_FOUND = "No_license_found";
	public static final String DUAL_LICENSE = "Dual-license";
	public static final String UNCLASSIFIED = "UnclassifiedLicense";
	public static final String TRADEMARK_REF = "Trademark-ref";
	public static final String OTHER = "See-doc(OTHER)";
	public static final String SEE_FILE = "See-file";
	public static final String FSF = "FSF";
	public static final String FSF_REF = "FSF-ref";
	public static final String RESTRICTED_RIGHTS = "Restricted-rights";
	public static final String UNRAR_RESTRICTION = "unRAR restriction";
	public static final String NOT_PUBLIC_DOMAIN = "NOT-public-domain";
	public static String[] ignoredLicenseList = {"Public-domain","Public-domain-ref","Same-license-as","No_license_found","Dual-license","UnclassifiedLicense","Trademark-ref","See-doc(OTHER)","See-file","FSF","FSF-ref","Restricted-rights","unRAR restriction","NOT-public-domain"};
	
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
	public static LicenseExpression parseExpression(String licenseExp,HashMap<String, String> referencedLicenses) {
		LicenseExpression expObject = new LicenseExpression();
		String[] exportedLicenses;

		licenseExp = licenseExp.replace(")", "");
		licenseExp = licenseExp.replace("(", "");

		if (licenseExp.contains(" OR ")) {
			expObject.disjunctive = true;
			exportedLicenses = licenseExp.split(" OR ");
		} else {
			expObject.disjunctive = false;
			exportedLicenses = licenseExp.split(" AND ");
		}
		
		expObject.addLicenses(exportedLicenses,referencedLicenses);
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
	public void addLicenses(String[] licenses,HashMap<String, String> referencedLicenses) {
		int index=0;
		for(String referenced:licenses){
			if(referenced.contains("LicenseRef")){
				licenses[index]=referencedLicenses.get(referenced);
			}
			index++;
		}
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
