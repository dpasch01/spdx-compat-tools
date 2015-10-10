package ac.ucy.cs.spdx.license;

import java.util.ArrayList;

public class IgnoredLicenses {

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
	public static ArrayList<String> ignoredLicenseList = new ArrayList<String>();
	
	{
		ignoredLicenseList.add(SAME_AS);
		ignoredLicenseList.add(NO_LICENSE_FOUND);
		ignoredLicenseList.add(DUAL_LICENSE);
		ignoredLicenseList.add(UNCLASSIFIED);
		ignoredLicenseList.add(TRADEMARK_REF);
		ignoredLicenseList.add(OTHER);
		ignoredLicenseList.add(SEE_FILE);
		ignoredLicenseList.add(FSF);
		ignoredLicenseList.add(FSF_REF);
		ignoredLicenseList.add(RESTRICTED_RIGHTS);
		ignoredLicenseList.add(UNRAR_RESTRICTION);
		ignoredLicenseList.add(NOT_PUBLIC_DOMAIN);
	}
	
}
