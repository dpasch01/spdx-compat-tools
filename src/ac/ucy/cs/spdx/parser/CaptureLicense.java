package ac.ucy.cs.spdx.parser;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.SPDXDocument;
import org.spdx.rdfparser.SPDXDocument.SPDXPackage;
import org.spdx.rdfparser.SPDXDocumentFactory;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.ExtractedLicenseInfo;
import org.spdx.rdfparser.license.LicenseInfoFactory;
import org.spdx.rdfparser.license.LicenseSet;
import org.spdx.rdfparser.model.SpdxDocument;
import org.spdx.rdfparser.model.SpdxPackage;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;

/**
 * CaptureLicenses class provides us with an object containing the declared
 * license or licenses of an SPDXPackage, and whether or not those licenses are
 * conjunctive or disjunctive. Also contains the {@link SPDXDocument} for each
 * {@link LicenseSet}
 * 
 * @version 1.2
 * @author dpasch01
 */
@SuppressWarnings("deprecation")
public class CaptureLicense {

	private String fileName;
	private SpdxDocument captureDocument;
	private LicenseExpression captureLicenses;
	private SpdxPackage capturePackage;

	/**
	 * Returns the concluded license of the current {@link SPDXPackage}. If
	 * there is no concluded license in the package it must return "NONE", or if
	 * the author for any reason didn't put "NONE", the returned value will be
	 * <code>null</code>
	 * 
	 * @return {@link AnyLicenseInfo}
	 */
	public AnyLicenseInfo getConcludedLicense() {
		return this.capturePackage.getConcludedLicenses();
	}

	/**
	 * Returns the extracted licenses of the current {@link SPDXPackage}. The
	 * array returned doesn't include the relationships between the
	 * licenses(conjunctive or disjunctive).
	 * 
	 * @return {@link ExtractedLicenseInfo[]}
	 */
	public ExtractedLicenseInfo[] getExtractedLicenses()
			throws InvalidSPDXAnalysisException {
		return this.captureDocument.getExtractedLicenseInfos();
	}

	/**
	 * Returns the declared licenses of the current {@link SPDXPackage}. The
	 * {@link AnyLicenseInfo} returned containes a license expression of the
	 * author's declared licenses for the {@link SpdxPackage} and if they are
	 * conjunctive or disjunctive.
	 * 
	 * @return {@link AnyLicenseInfo}
	 */
	public AnyLicenseInfo getDeclaredLicense()
			throws InvalidLicenseStringException, InvalidSPDXAnalysisException {
		AnyLicenseInfo declared = LicenseInfoFactory
				.parseSPDXLicenseString(this.capturePackage
						.getDeclaredLicense().toString());
		return declared;
	}

	/**
	 * Returns the declared licenses of the current {@link SPDXPackage}
	 * meta-data. For SPDX 2.0 the data license is declared to be CC0-1.0
	 * 
	 * @return {@link AnyLicenseInfo}
	 */
	public AnyLicenseInfo getDataLicense() throws InvalidSPDXAnalysisException {
		return this.captureDocument.getDataLicense();
	}

	/**
	 * Constructor for the {@link CaptureLicense} object. It initializes the
	 * fields with the {@link SPDXDocument}, {@link SpdxPackage} and the
	 * declared licenses expression via a {@link LicenseExpression} object.
	 * 
	 * @return {@link CaptureLicense}
	 */
	public CaptureLicense(String path) throws IOException,
			InvalidSPDXAnalysisException, InvalidLicenseStringException {

		SpdxDocument spdxDoc = SPDXDocumentFactory.createSpdxDocument(path);
		SpdxPackage spdxPackage = spdxDoc.getSpdxPackage();
		AnyLicenseInfo declared = LicenseInfoFactory
				.parseSPDXLicenseString(spdxPackage.getDeclaredLicense()
						.toString());

		this.setFileName(FilenameUtils.getBaseName(path));
		this.captureDocument = spdxDoc;
		this.capturePackage = spdxPackage;
		this.captureLicenses = LicenseExpression.parseExpression(declared
				.toString());
	}

	/**
	 * Returns the {@link LicenseExpression} object for the current
	 * {@link SPDXPackage} declared licenses.
	 * 
	 * @return {@link LicenseExpression}
	 */
	public LicenseExpression getLicenseExpression() {
		return this.captureLicenses;
	}

	/**
	 * Returns the {@link SPDXDocument} object for the current
	 * {@link CaptureLicense}
	 * 
	 * @return {@link SpdxDocument}
	 */
	public SpdxDocument getSpdxDocument() {
		return this.captureDocument;
	}

	/**
	 * Returns the {@link SpdxPackage} object for the current
	 * {@link CaptureLicense}
	 * 
	 * @return {@link SpdxPackage}
	 */
	public SpdxPackage getSpdxPackage() {
		return this.capturePackage;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * public SpdxLicensePairConflictError checkDeclaredLicenses(){ return null;
	 * }
	 */
}
