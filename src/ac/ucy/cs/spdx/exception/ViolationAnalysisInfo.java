package ac.ucy.cs.spdx.exception;

import java.util.ArrayList;
import java.util.Arrays;

import ac.ucy.cs.spdx.compatibility.LicenseCompatibility;
import ac.ucy.cs.spdx.license.License;
import ac.ucy.cs.spdx.parser.LicenseExpression;

public class ViolationAnalysisInfo {
	private ArrayList<SpdxLicensePairConflictError> unarySpdxErrors;
	private ArrayList<License> proposedLicenses;
	private boolean violationDetected;
	private boolean isAdjustFeasible;

	public ViolationAnalysisInfo(SpdxLicensePairConflictError... unaryErrors) {
		String[] declared = getMultipleDeclaredLicenses(unaryErrors);

		this.unarySpdxErrors = new ArrayList<SpdxLicensePairConflictError>();
		this.unarySpdxErrors.addAll(Arrays.asList(unaryErrors));
		this.proposedLicenses = new ArrayList<License>();

		if (LicenseCompatibility.areCompatible(declared)) {
			this.setViolationDetected(false);
		} else {
			this.setViolationDetected(true);
			proposedLicenses.addAll(LicenseCompatibility
					.proposeLicense(declared));
			if (proposedLicenses.isEmpty()) {
				this.setAdjustFeasible(false);
			} else {
				this.setAdjustFeasible(true);
			}
		}
	}

	/**
	 * @return the unarySpdxErrors
	 */
	public ArrayList<SpdxLicensePairConflictError> getUnarySpdxErrors() {
		return unarySpdxErrors;
	}

	/**
	 * @param unarySpdxErrors
	 *            the unarySpdxErrors to set
	 */
	public void setUnarySpdxErrors(
			ArrayList<SpdxLicensePairConflictError> unarySpdxErrors) {
		this.unarySpdxErrors = unarySpdxErrors;
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

	public static String[] getMultipleDeclaredLicenses(
			SpdxLicensePairConflictError... unaryErrors) {
		ArrayList<String> multipleDeclared = new ArrayList<String>();
		for (SpdxLicensePairConflictError unaryError : unaryErrors) {
			for (String declared : unaryError.getDeclaredLicenses()) {
				if (!multipleDeclared.contains(declared)) {
					if(!Arrays.asList(LicenseExpression.ignoredLicenseList).contains(declared)){
						multipleDeclared.add(declared);
					}		
				}
			}

		}

		return multipleDeclared.toArray(new String[multipleDeclared.size()]);
	}

	/**
	 * @return the violationDetected
	 */
	public boolean isViolationDetected() {
		return violationDetected;
	}

	/**
	 * @param violationDetected
	 *            the violationDetected to set
	 */
	public void setViolationDetected(boolean violationDetected) {
		this.violationDetected = violationDetected;
	}

	/**
	 * @return the isAdjustFeasible
	 */
	public boolean isAdjustFeasible() {
		return isAdjustFeasible;
	}

	/**
	 * @param isAdjustFeasible
	 *            the isAdjustFeasible to set
	 */
	public void setAdjustFeasible(boolean isAdjustFeasible) {
		this.isAdjustFeasible = isAdjustFeasible;
	}

	@Override
	public String toString() {
		for (SpdxLicensePairConflictError error : this.unarySpdxErrors) {
			if (!error.areCompatible()) {
				return error.toString();
			}
		}

		StringBuilder violationEcho = new StringBuilder();
		violationEcho.append("The spdx files ");
		for (SpdxLicensePairConflictError error : this.getUnarySpdxErrors()) {
			violationEcho.append(error.getSpdxCaptured().getFileName() + ", ");
		}
		violationEcho.delete(violationEcho.length() - 2,
				violationEcho.length() - 1);

		if (this.violationDetected) {
			violationEcho.append(" have some compatibility issues.\n");
			if (this.isAdjustFeasible) {
				violationEcho
						.append("The violation is adjustable and the proposed licenses that can be used for the matter are : \n");
				for (License l : this.proposedLicenses) {
					violationEcho.append(l.getLicenseName() + "\n");
				}

			} else {
				violationEcho
						.append("The violation is not adjustable and there are no proposed licenses that can be used for the matter.\nPlease review your licenses again.");
			}
		} else {
			violationEcho.append(" have no violation of compatibility issues.");
		}

		return violationEcho.toString();
	}

	public String toJson() {
		StringBuilder violationJSON = new StringBuilder();
		violationJSON.append("{" + "\"unaries\"" + ":" + "[");

		for (SpdxLicensePairConflictError error : this.unarySpdxErrors) {
			violationJSON.append(error.toJson() + ",");
		}

		violationJSON.deleteCharAt(violationJSON.length() - 1);
		violationJSON.append("]" + "," + "\"violation\"" + ":");
		violationJSON.append("" + this.violationDetected + "" + ","
				+ "\"adjustable\"" + ":" + "" + this.isAdjustFeasible + ""
				+ "," + "\"proposal\"" + ":" + "[");

		for (License license : this.proposedLicenses) {
			violationJSON.append("{\"identifier\"" + ":" + "\""
					+ license.getIdentifier() + "\"},");
		}

		if (!this.proposedLicenses.isEmpty()) {
			violationJSON.deleteCharAt(violationJSON.length() - 1);
		}
		violationJSON.append("]}");

		return violationJSON.toString();
	}
}
