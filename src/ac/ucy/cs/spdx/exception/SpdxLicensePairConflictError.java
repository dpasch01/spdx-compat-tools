package ac.ucy.cs.spdx.exception;

import java.util.ArrayList;
import java.util.Arrays;

import ac.ucy.cs.spdx.compatibility.LicenseCompatibility;
import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.parser.CaptureLicense;
import ac.ucy.cs.spdx.parser.LicenseExpression;

public class SpdxLicensePairConflictError {
	private ArrayList<String> declaredLicenses;
	private ArrayList<License> proposedLicenses;
	private boolean areCompatible = true;
	private boolean isAdjustable = true;
	private CaptureLicense spdxCaptured;

	public SpdxLicensePairConflictError(CaptureLicense spdxCaptured) {
		LicenseExpression expression = spdxCaptured.getLicenseExpression();

		this.declaredLicenses = new ArrayList<String>();
				
		for(String declared:expression.getLicenses()){
			if(!Arrays.asList(LicenseExpression.ignoredLicenseList).contains(declared)){
				this.declaredLicenses.add(declared);
			}		
		}
		
		this.proposedLicenses = new ArrayList<License>();
		if ((!expression.isDisjunctive())
				&& (!LicenseCompatibility.areCompatible(expression
						.getLicensesArray()))) {
			this.setCompatible(false);
			this.proposedLicenses.addAll(LicenseCompatibility
					.proposeLicense(expression.getLicensesArray()));
			if (this.proposedLicenses.isEmpty()) {
				setAdjustable(false);
			}
		}

		this.setSpdxCaptured(spdxCaptured);
	}

	@Override
	public String toString() {
		StringBuilder errorEcho = new StringBuilder();
		errorEcho.append("The file " + spdxCaptured.getFileName());
		if (this.areCompatible) {
			errorEcho.append(" has no violations.");
		} else {
			errorEcho
					.append(" has violation considering the declared licenses compatibility.\n");
			if (this.isAdjustable) {
				errorEcho
						.append("The violations are adjustable and the proposed licenses are:\n");
				for (License license : this.proposedLicenses) {
					errorEcho.append(license.getIdentifier() + "\n");
				}
			} else {
				errorEcho
						.append("The violations are not adjustable, please reconsider your declared licenses.");
			}
		}

		return errorEcho.toString();
	}

	/**
	 * @return the declaredLicenses
	 */
	public ArrayList<String> getDeclaredLicenses() {
		return declaredLicenses;
	}

	/**
	 * @return the declaredLicenses String[]
	 */
	public String[] getDeclaredLicensesArray() {
		return (String[]) declaredLicenses.toArray();
	}

	/**
	 * @param declaredLicenses
	 *            the declaredLicenses to set
	 */
	public void setDeclaredLicenses(ArrayList<String> declaredLicenses) {
		this.declaredLicenses = declaredLicenses;
	}

	/**
	 * @return the proposedLicenses
	 */
	public ArrayList<License> getProposedLicenses() {
		return proposedLicenses;
	}

	/**
	 * @param proposedLicenses
	 *            the proposedLicenses to set
	 */
	public void setProposedLicenses(ArrayList<License> proposedLicenses) {
		this.proposedLicenses = proposedLicenses;
	}

	/**
	 * @return the areCompatible
	 */
	public boolean areCompatible() {
		return areCompatible;
	}

	/**
	 * @param areCompatible
	 *            the areCompatible to set
	 */
	public void setCompatible(boolean areCompatible) {
		this.areCompatible = areCompatible;
	}

	/**
	 * @return the isAdjustable
	 */
	public boolean isAdjustable() {
		return isAdjustable;
	}

	/**
	 * @param isAdjustable
	 *            the isAdjustable to set
	 */
	public void setAdjustable(boolean isAdjustable) {
		this.isAdjustable = isAdjustable;
	}

	/**
	 * @return the spdxCaptured
	 */
	public CaptureLicense getSpdxCaptured() {
		return spdxCaptured;
	}

	/**
	 * @param spdxCaptured
	 *            the spdxCaptured to set
	 */
	public void setSpdxCaptured(CaptureLicense spdxCaptured) {
		this.spdxCaptured = spdxCaptured;
	}

	public String toJson() {
		StringBuilder errorJSON = new StringBuilder();
		errorJSON.append("{" + "\"file\"" + ":" + "\""
				+ this.getSpdxCaptured().getFileName() + "\",\"declared\":[");

		for (String declared : this.declaredLicenses) {
			errorJSON
					.append("{\"identifier\"" + ":" + "\"" + declared + "\"},");
		}

		if (!this.declaredLicenses.isEmpty()) {
			errorJSON.deleteCharAt(errorJSON.length() - 1);
		}
		errorJSON.append("]" + "," + "\"compatible\"" + ":");
		errorJSON.append("" + this.areCompatible + "" + ","
				+ "\"adjustable\"" + ":" + "" + this.isAdjustable + ""
				+ "," + "\"proposal\"" + ":" + "[");

		for (License license : this.proposedLicenses) {
			errorJSON.append("{\"identifier\"" + ":" + "\""
					+ license.getIdentifier() + "\"},");
		}

		if (!this.proposedLicenses.isEmpty()) {
			errorJSON.deleteCharAt(errorJSON.length() - 1);
		}
		errorJSON.append("]}");

		return errorJSON.toString();
	}

}
