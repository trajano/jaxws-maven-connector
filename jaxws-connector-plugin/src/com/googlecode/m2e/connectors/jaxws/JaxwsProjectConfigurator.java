/*******************************************************************************
 * Copyright (c) 2011 Callista Enterprise AB - Bj�rn Beskow
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package com.googlecode.m2e.connectors.jaxws;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.jdt.AbstractSourcesGenerationProjectConfigurator;

public class JaxwsProjectConfigurator extends AbstractSourcesGenerationProjectConfigurator {
	@Override
	protected String getOutputFolderParameterName() {
		return "sourceDestDir";
	}

	@Override
	public AbstractBuildParticipant getBuildParticipant(IMavenProjectFacade projectFacade, MojoExecution execution,
			IPluginExecutionMetadata executionMetadata) {
		return new JaxwsBuildParticipant(execution);
	}
}
