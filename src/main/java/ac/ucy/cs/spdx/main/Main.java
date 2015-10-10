package ac.ucy.cs.spdx.main;

import java.io.IOException;
import java.util.HashMap;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.license.AnyLicenseInfo;
import org.spdx.rdfparser.license.ExtractedLicenseInfo;
import org.spdx.rdfparser.model.ExternalDocumentRef;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;

import ac.ucy.cs.spdx.dot.DotPath;
import ac.ucy.cs.spdx.exception.SpdxLicensePairConflictError;
import ac.ucy.cs.spdx.exception.UnsupportedSpdxVersionException;
import ac.ucy.cs.spdx.exception.ViolationAnalysisInfo;
import ac.ucy.cs.spdx.graph.LicenseGraph;
import ac.ucy.cs.spdx.parser.CaptureLicense;

@SuppressWarnings("unused")
public class Main {

	public static void main(String[] args) throws InvalidSPDXAnalysisException {
		LicenseGraph.initialize(DotPath.GRAPHDOT_PATH);

		CaptureLicense captureAnomos = null;
		CaptureLicense captureHunspell = null;
		CaptureLicense captureMlply = null;
		
		SpdxThread threadAnomos = new SpdxThread("C:/Users/dpasch01/Documents/SPDX Example Files/anomos-0.9.5.rdf");
		SpdxThread threadHunspell = new SpdxThread("C:/Users/dpasch01/Documents/SPDX Example Files/ckeditor4.4.7.rdf");
		SpdxThread threadMlpy = new SpdxThread("C:/Users/dpasch01/Documents/SPDX Example Files/Mlpy-3.5.0.rdf");

		long startTime = System.currentTimeMillis();

		threadAnomos.start();
		threadHunspell.start();
		threadMlpy.start();

		try {
			threadAnomos.join();
			threadHunspell.join();
			threadMlpy.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;

		captureAnomos = threadAnomos.getCapturedLicense();
		captureHunspell = threadHunspell.getCapturedLicense();
		captureMlply = threadMlpy.getCapturedLicense();

		System.out.println("Anomos : " + threadAnomos.getExecutionTime());
		System.out.println(threadAnomos.getCapturedLicense().getSpdxPackage().getLicenseDeclared());
		System.out.println("Hunspell : " + threadHunspell.getExecutionTime());
		System.out.println(threadHunspell.getCapturedLicense().getSpdxPackage().getLicenseDeclared());
		System.out.println("Mlply : " + threadHunspell.getExecutionTime());
		System.out.println(threadMlpy.getCapturedLicense().getSpdxPackage().getLicenseDeclared());
		System.out.println("Total execution time : " + executionTime);
		System.out.println();

		SpdxLicensePairConflictError pairAnomos = null;
		try {
			pairAnomos = new SpdxLicensePairConflictError(
					captureAnomos);
		} catch (UnsupportedSpdxVersionException e) {
			e.printStackTrace();
		}
		SpdxLicensePairConflictError pairHunspell = null;
		try {
			pairHunspell = new SpdxLicensePairConflictError(
					captureHunspell);
		} catch (UnsupportedSpdxVersionException e) {
			e.printStackTrace();
		}
		SpdxLicensePairConflictError pairMlpy = null;
		try {
			pairMlpy = new SpdxLicensePairConflictError(
					captureMlply);
		} catch (UnsupportedSpdxVersionException e) {
			e.printStackTrace();
		}
		
		ViolationAnalysisInfo analysisInfo = new ViolationAnalysisInfo(
				pairAnomos, pairHunspell, pairMlpy);
		
		System.out.println(analysisInfo.toJson());
	}

}
