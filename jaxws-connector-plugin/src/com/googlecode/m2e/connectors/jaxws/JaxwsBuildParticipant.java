/*******************************************************************************
 * Copyright (c) 2011 Callista Enterprise AB - Björn Beskow
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.googlecode.m2e.connectors.jaxws;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class JaxwsBuildParticipant extends MojoExecutionBuildParticipant {

	public JaxwsBuildParticipant(MojoExecution execution) {
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {
		IMaven maven = MavenPlugin.getMaven();
		BuildContext buildContext = getBuildContext();

		// check if initial generation has been done
		File stale = maven.getMojoParameterValue(getSession(),
				getMojoExecution(), "staleFile", File.class);
		if (stale != null && stale.exists()) {
			// check if any of the binding files have changed
			File source = maven.getMojoParameterValue(getSession(),
					getMojoExecution(), "bindingDirectory", File.class);
			Scanner ds = buildContext.newScanner(source); // delta or full
															// scanner
			try {
				ds.scan();
				String[] includedBindingFiles = ds.getIncludedFiles();
				// check if any of the wsdl files have changed
				source = maven.getMojoParameterValue(getSession(),
						getMojoExecution(), "wsdlDirectory", File.class);
				ds = buildContext.newScanner(source); // delta or full
														// scanner
				ds.scan();
				String[] includedWsdlFiles = ds.getIncludedFiles();
				// If no changes, simply return
				if ((includedBindingFiles == null || includedBindingFiles.length <= 0)
						&& (includedWsdlFiles == null || includedWsdlFiles.length <= 0)) {
					return null;
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		// Otherwise, execute mojo
		final Set<IProject> result = super.build(kind, monitor);

		// tell m2e builder to refresh generated files
		File generated = maven.getMojoParameterValue(getSession(),
				getMojoExecution(), "sourceDestDir", File.class);
		if (generated != null) {
			buildContext.refresh(generated);
		}

		return result;
	}
}
