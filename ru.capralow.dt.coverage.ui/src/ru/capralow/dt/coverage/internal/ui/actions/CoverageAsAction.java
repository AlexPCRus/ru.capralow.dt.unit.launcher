/*******************************************************************************
 * Copyright (c) 2006, 2019 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 ******************************************************************************/
package ru.capralow.dt.coverage.internal.ui.actions;

import org.eclipse.debug.ui.actions.LaunchShortcutsAction;

import ru.capralow.dt.coverage.internal.ui.CoverageUIPlugin;

/**
 * Action implementation for "Coverage as" menu.
 */
public class CoverageAsAction extends LaunchShortcutsAction {

	public CoverageAsAction() {
		super(CoverageUIPlugin.ID_COVERAGE_LAUNCH_GROUP);
	}

}
