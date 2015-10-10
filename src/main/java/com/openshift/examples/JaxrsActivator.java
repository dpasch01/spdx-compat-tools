package com.openshift.examples;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import ac.ucy.cs.spdx.service.Compatibility;
import ac.ucy.cs.spdx.service.SpdxViolationAnalysis;

@ApplicationPath("/rest")
public class JaxrsActivator extends Application {

     public Set<Class<?>> getClasses() {
         Set<Class<?>> s = new HashSet<Class<?>>();
         s.add(HelloWorld.class);
         s.add(Compatibility.class);
         s.add(SpdxViolationAnalysis.class);
         return s;
     }
}