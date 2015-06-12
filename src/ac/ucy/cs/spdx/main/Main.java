package ac.ucy.cs.spdx.main;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;

import ac.ucy.cs.spdx.dot.DotPath;
import ac.ucy.cs.spdx.exception.SpdxLicensePairConflictError;
import ac.ucy.cs.spdx.exception.ViolationAnalysisInfo;
import ac.ucy.cs.spdx.graph.LicenseGraph;
import ac.ucy.cs.spdx.parser.CaptureLicense;

public class Main {

	public static void main(String[] args) throws InvalidSPDXAnalysisException {
		LicenseGraph.initialize(DotPath.GRAPHDOT_PATH);

		CaptureLicense captureAnomos = null;
		CaptureLicense captureHunspell = null;

		SpdxThread threadAnomos = new SpdxThread("examples/anomos.rdf");
		SpdxThread threadHunspell = new SpdxThread("examples/hunspell.rdf");

		long startTime = System.currentTimeMillis();

		threadAnomos.start();
		threadHunspell.start();

		try {
			threadAnomos.join();
			threadHunspell.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;

		captureAnomos = threadAnomos.getCapturedLicense();
		captureHunspell = threadHunspell.getCapturedLicense();

		System.out.println("Anomos : " + threadAnomos.getExecutionTime());
		System.out.println("Hunspell : " + threadHunspell.getExecutionTime());
		System.out.println("Total execution time : " + executionTime);
		System.out.println();

		SpdxLicensePairConflictError pairAnomos = new SpdxLicensePairConflictError(
				captureAnomos);
		SpdxLicensePairConflictError pairHunspell = new SpdxLicensePairConflictError(
				captureHunspell);

		ViolationAnalysisInfo analysisInfo = new ViolationAnalysisInfo(
				pairAnomos, pairHunspell);
		System.out.println(analysisInfo.toString());
	}

}
