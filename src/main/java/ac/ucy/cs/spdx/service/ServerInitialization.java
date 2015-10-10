package ac.ucy.cs.spdx.service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ac.ucy.cs.spdx.dot.DotPath;
import ac.ucy.cs.spdx.graph.LicenseGraph;

public class ServerInitialization implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		//Destroy everything.
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LicenseGraph.initialize(DotPath.GRAPHDOT_PATH);
	}

}
