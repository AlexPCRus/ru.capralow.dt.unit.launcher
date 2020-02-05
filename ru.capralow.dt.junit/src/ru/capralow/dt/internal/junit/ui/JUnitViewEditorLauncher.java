/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - bug fixes
 *     Brock Janiczak <brockj@tpg.com.au> - [JUnit] Add context menu action to import junit test results from package explorer - https://bugs.eclipse.org/bugs/show_bug.cgi?id=213786
 *******************************************************************************/
package ru.capralow.dt.internal.junit.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorLauncher;

import ru.capralow.dt.internal.junit.model.JUnitModel;
import ru.capralow.dt.internal.junit.util.ExceptionHandler;

public class JUnitViewEditorLauncher implements IEditorLauncher {

	@Override
	public void open(IPath file) {
		try {
			JUnitPlugin.getActivePage().showView(TestRunnerViewPart.NAME);
			JUnitModel.importTestRunSession(file.toFile());
		} catch (CoreException e) {
			ExceptionHandler.handle(e,
					JUnitMessages.JUnitViewEditorLauncher_dialog_title,
					JUnitMessages.JUnitViewEditorLauncher_error_occurred);
		}
	}

}
