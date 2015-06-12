package ac.ucy.cs.spdx.license;

import ac.ucy.cs.spdx.exception.LicenseAlreadyExistsException;
import ac.ucy.cs.spdx.license.License.Category;

public class Main {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		License license = null;
		try {
			License Apache10 = new License("Apache License 1.0", "Apache-1.0",
					Category.PERMISSIVE);
			License AFL30 = new License("Academic Free License v3.0",
					"AFL-3.0", Category.PERMISSIVE);
			License MIT = new License("MIT License", "MIT", Category.PERMISSIVE);
			License X11 = new License("X11 License", "X11", Category.PERMISSIVE);
			License BSD2Clause = new License(
					"BSD 2-clause 'Simplified' License", "BSD-2-Clause",
					Category.PERMISSIVE);
			License BSD2ClauseFreeBSD = new License(
					"BSD 2-clause FreeBSD License", "BSD-2-Clause-FreeBSD",
					Category.PERMISSIVE);
			License BSD3Clause = new License(
					"BSD 3-clause 'New' or 'Revised' License", "BSD-3-Clause",
					Category.PERMISSIVE);
			License Apache20 = new License("Apache License 2.0", "Apache-2.0",
					Category.PERMISSIVE);
			License Zlib = new License("zlib License", "Zlib",
					Category.PERMISSIVE);
			License Libpng = new License("libpng License", "Libpng",
					Category.PERMISSIVE);
			License CDDL10 = new License(
					"Common Development and Distribution License 1.0",
					"CDDL-1.0", Category.WEAK_COPYLEFT);
			License CDDL11 = new License(
					"Common Development and Distribution License 1.1",
					"CDDL-1.1", Category.WEAK_COPYLEFT);
			License MPL11 = new License("Mozilla Public License 1.1",
					"MPL-1.1", Category.WEAK_COPYLEFT);
			License Artistic20 = new License("Artistic License 2.0",
					"Artistic-2.0", Category.WEAK_COPYLEFT);
			License MPL20 = new License("Mozilla Public License 2.0",
					"MPL-2.0", Category.WEAK_COPYLEFT);
			License LGPL21 = new License(
					"GNU Lesser General Public License v2.1 only", "LGPL-2.1",
					Category.WEAK_COPYLEFT);
			License LGPL21plus = new License(
					"GNU Lesser General Public License v2.1 or later",
					"LGPL-2.1+", Category.WEAK_COPYLEFT);
			License LGPL30 = new License(
					"GNU Lesser General Public License v3.0 only", "LGPL-3.0",
					Category.WEAK_COPYLEFT);
			License LGPL30plus = new License(
					"GNU Lesser General Public License v3.0 or later",
					"LGPL-3.0+", Category.WEAK_COPYLEFT);
			License GPL20 = new License("GNU General Public License v2.0 only",
					"GPL-2.0", Category.STRONG_COPYLEFT);
			License GPL20plus = new License(
					"GNU General Public License v2.0 or later", "GPL-2.0+",
					Category.STRONG_COPYLEFT);
			License GPL30 = new License("GNU General Public License v3.0 only",
					"GPL-3.0", Category.STRONG_COPYLEFT);
			License GPL30plus = new License(
					"GNU General Public License v3.0 or later", "GPL-3.0+",
					Category.STRONG_COPYLEFT);
			License AGPL30 = new License(
					"GNU Affero General Public License v3.0", "AGPL-3.0",
					Category.STRONG_COPYLEFT);
			License OSL30 = new License("Open Software License 3.0", "OSL-3.0",
					Category.STRONG_COPYLEFT);
			License EUPL11 = new License("European Union Public License 1.1",
					"EUPL-1.1", Category.STRONG_COPYLEFT);
			License CeCILL20 = new License(
					"CeCILL Free Software License Agreement v2.0",
					"CECILL-2.0", Category.STRONG_COPYLEFT);
			License AGPL10 = new License(
					"GNU Affero General Public License v1.0", "AGPL-1.0",
					Category.STRONG_COPYLEFT);
			license = new License("Apache 2.0", "Apache-2.0",
					Category.PERMISSIVE);
		} catch (LicenseAlreadyExistsException e) {
			e.printStackTrace();
		}
		
		System.out.println(License.toJson());
	}

}
