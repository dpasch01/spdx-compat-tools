package com.openshift.examples;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloWorld {

	@GET
	@Path("/echo")
	@Produces(MediaType.TEXT_HTML)
	public String helloWorld(){
		return "<p>Hello, world!</p>";
	}
	
}
