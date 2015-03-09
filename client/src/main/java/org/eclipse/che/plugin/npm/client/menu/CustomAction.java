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
package org.eclipse.che.plugin.npm.client.menu;


import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.app.CurrentProject;

/**
 * Allow to hide elements if the current project is not an angular project.
 *
 * @author Florent Benoit
 */
public abstract class CustomAction extends Action {

    private AppContext appContext;

    public CustomAction(AppContext appContext, String name, String description) {
        super(name, description, null);
        this.appContext = appContext;
    }

    /** {@inheritDoc} */
    @Override
    public void update(ActionEvent e) {
        CurrentProject activeProject = appContext.getCurrentProject();
        if (activeProject != null) {
            final String projectTypeId = activeProject.getRootProject().getType();
            boolean isAngularJSProject = "AngularJS".equals(projectTypeId);
            e.getPresentation().setVisible(isAngularJSProject);
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}
