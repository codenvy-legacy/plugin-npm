/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.plugin.npm.client.menu;

import com.codenvy.api.analytics.logger.AnalyticsEventLogger;
import com.codenvy.api.builder.BuildStatus;
import com.codenvy.api.builder.dto.BuildOptions;
import com.codenvy.ide.api.action.ActionEvent;
import com.codenvy.ide.api.app.AppContext;
import com.codenvy.ide.api.event.RefreshProjectTreeEvent;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.plugin.npm.client.NpmExtension;
import com.codenvy.plugin.npm.client.builder.BuildFinishedCallback;
import com.codenvy.plugin.npm.client.builder.BuilderAgent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import java.util.Arrays;
import java.util.List;

/**
 * Action that install NPM dependencies.
 * @author Florent Benoit
 */
public class NpmInstallAction extends CustomAction implements BuildFinishedCallback {

    private DtoFactory dtoFactory;

    private BuilderAgent builderAgent;

    private EventBus eventBus;

    private boolean buildInProgress;

    private final AnalyticsEventLogger analyticsEventLogger;

    @Inject
    public NpmInstallAction(LocalizationConstant localizationConstant,
                            DtoFactory dtoFactory, BuilderAgent builderAgent, AppContext appContext,
                            AnalyticsEventLogger analyticsEventLogger, EventBus eventBus) {
        super(appContext, localizationConstant.npmInstallText(), localizationConstant.npmInstallDescription());
        this.dtoFactory = dtoFactory;
        this.builderAgent = builderAgent;
        this.analyticsEventLogger = analyticsEventLogger;
        this.eventBus = eventBus;
    }

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent e) {
        analyticsEventLogger.log(this);
        installDependencies();
    }


    public void installDependencies() {
        buildInProgress = true;
        List<String> targets = Arrays.asList("install");
        BuildOptions buildOptions = dtoFactory.createDto(BuildOptions.class).withTargets(targets).withBuilderName("npm");
        builderAgent.build(buildOptions, "Installation of npm dependencies...", "Npm dependencies successfully downloaded",
                           "Npm dependencies install failed", "npm", this);
    }

    @Override
    public void onFinished(BuildStatus buildStatus) {
        // and refresh the tree if success
        if (buildStatus == BuildStatus.SUCCESSFUL) {
            eventBus.fireEvent(new RefreshProjectTreeEvent());
        }

        // build finished
        buildInProgress = false;

    }


    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(!buildInProgress);
    }
}
