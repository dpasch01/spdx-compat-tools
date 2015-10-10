package ac.ucy.cs.spdx.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import ac.ucy.cs.spdx.compatibility.ServerPath;

public class ParseRdf {

	public static String parseToRdf(String baseName, String content)
			throws IOException {
		File rdf;
		File dir = new File(ServerPath.PROJECT_PATH+"temp/");
		

		if (!dir.exists())
			dir.mkdir();

		rdf = File.createTempFile(baseName, ".rdf", dir);
		rdf.deleteOnExit();

		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rdf), "UTF8"));
		out.append(content);
 
		out.flush();
		out.close();

		return rdf.getAbsolutePath();
	}

}
