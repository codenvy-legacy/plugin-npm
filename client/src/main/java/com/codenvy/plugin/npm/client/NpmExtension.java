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
package com.codenvy.plugin.npm.client;

import com.codenvy.ide.api.action.ActionManager;
import com.codenvy.ide.api.action.Constraints;
import com.codenvy.ide.api.action.DefaultActionGroup;
import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.extension.builder.client.BuilderLocalizationConstant;
import com.codenvy.plugin.npm.client.menu.LocalizationConstant;
import com.codenvy.plugin.npm.client.menu.NpmInstallAction;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import static com.codenvy.ide.api.action.Anchor.AFTER;
import static com.codenvy.ide.api.action.IdeActions.GROUP_BUILD;

/**
 * Extension registering NPM commands
 * @author Florent Benoit
 */
@Singleton
@Extension(title = "NPM extension")
public class NpmExtension {

    @Inject
    public NpmExtension(ActionManager actionManager,
                          BuilderLocalizationConstant builderLocalizationConstant,
                          LocalizationConstant localizationConstantNpm,
                          final NpmInstallAction npmInstallAction) {

        // Register action
        actionManager.registerAction(localizationConstantNpm.npmInstallId(), npmInstallAction);

        // Get Build menu
        DefaultActionGroup buildMenuActionGroup = (DefaultActionGroup)actionManager.getAction(GROUP_BUILD);

        // create constraint
        Constraints afterBuildConstraints = new Constraints(AFTER, builderLocalizationConstant.buildProjectControlId());

        // Add NPM in build menu
        buildMenuActionGroup.add(npmInstallAction, afterBuildConstraints);

    }
}
