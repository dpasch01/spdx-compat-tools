package ac.ucy.cs.spdx.license;

import java.util.ArrayList;
import java.util.HashSet;

import ac.ucy.cs.spdx.exception.LicenseAlreadyExistsException;

/**
 * This is License object class. It consists of several constructors and methods
 * for editing and accessing data. The object consists of the license name,
 * identifier and text.
 * 
 * @author dpasch01
 * @version 1.2
 */
public class License {
	public static enum Category {
		PERMISSIVE, WEAK_COPYLEFT, STRONG_COPYLEFT, UNCATEGORIZED
	};

	private String licenseName;
	private String identifier;
	private Category category = Category.UNCATEGORIZED;
	public static HashSet<License> licenses = new HashSet<License>();

	/**
	 * License constructor that creates a new License based on the license name,
	 * identifier. You can enter the category of the license but it is optional.
	 * 
	 * @param String
	 * @param String
	 * @param Category
	 * @return {@link License}
	 * @throws LicenseAlreadyExistsException
	 */
	public License(String name, String identifier, Category category)
			throws LicenseAlreadyExistsException {

		if (License.licenseExists(identifier)) {
			throw new LicenseAlreadyExistsException(identifier
					+ " already exists in the system.");
		}

		this.setLicenseName(name);
		if (category != null) {
			this.setCategory(category);
		}
		this.setIdentifier(identifier);
		License.licenses.add(this);
	}

	/**
	 * Returns true if the license based on the identifier passed as parameter
	 * exists in the licenses list. Returns false otherwise.
	 * 
	 * @param String
	 * @return boolean
	 */
	public static boolean licenseExists(String lIdentifier) {
		License license = null;
		for (License l : licenses) {
			if (l.getIdentifier().equals(lIdentifier))
				license = l;
		}
		if (license != null)
			return true;
		return false;
	}

	/**
	 * Getter for the License list.
	 * 
	 * @return ArrayList<License>
	 */
	public static ArrayList<License> getLicenses() {
		return new ArrayList<License>(licenses);
	}

	/**
	 * Getter for the current {@link License} category.
	 * 
	 * @return ArrayList<License>
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Setter for the current {@link License} category.
	 * 
	 * @return ArrayList<License>
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * Getter for the current {@link License} identifier.
	 * 
	 * @return ArrayList<License>
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Setter for the current {@link License} identifier.
	 * 
	 * @return ArrayList<License>
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Getter for the current {@link License} name.
	 * 
	 * @return ArrayList<License>
	 */
	public String getLicenseName() {
		return licenseName;
	}

	/**
	 * Setter for the current {@link License} name.
	 * 
	 * @return ArrayList<License>
	 */
	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	/**
	 * The toString() method implementation for License object.
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return this.getIdentifier();
	}

	/**
	 * Search the license list to find the license specified by the license
	 * identifier passed as parameter. Returns that license if so or null.
	 * 
	 * @param String
	 * @return License
	 */
	public static License findLicense(String licenseIdentifier) {
		License license = null;
		for (License l : licenses) {
			if (l.getIdentifier().equals(licenseIdentifier))
				license = l;
		}
		return license;
	}

	public static String toJson() {
		StringBuilder errorJSON = new StringBuilder();
		errorJSON.append("{"+"\"licenses\":[");

		for (License license : licenses) {
			errorJSON
					.append("{\"identifier\"" + ":" + "\"" + license.identifier + "\"},");
		}

		if (!licenses.isEmpty()) {
			errorJSON.deleteCharAt(errorJSON.length() - 1);
		}
		errorJSON.append("]}");

		return errorJSON.toString();
	}

}
