package ac.ucy.cs.spdx.main;

import java.io.IOException;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.spdxspreadsheet.InvalidLicenseStringException;

import ac.ucy.cs.spdx.parser.CaptureLicense;

public class SpdxThread extends Thread {

	private String path;
	private long executionTime;
	private CaptureLicense capturedLicense;

	public SpdxThread(String path) {
		this.path = path;
	}

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();

		try {
			this.capturedLicense = new CaptureLicense(path);
		} catch (InvalidLicenseStringException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidSPDXAnalysisException e) {
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		this.executionTime = executionTime;
	}

	public CaptureLicense getCapturedLicense() {
		return this.capturedLicense;
	}

	public long getExecutionTime() {
		return this.executionTime;
	}
}
