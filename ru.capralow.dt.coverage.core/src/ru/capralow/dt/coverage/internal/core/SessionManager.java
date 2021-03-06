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
 * Adapted by Alexander Kapralov
 *
 ******************************************************************************/
package ru.capralow.dt.coverage.internal.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;

import ru.capralow.dt.coverage.core.ICoverageSession;
import ru.capralow.dt.coverage.core.ISessionListener;
import ru.capralow.dt.coverage.core.ISessionManager;

/**
 * ISessionManager implementation.
 */
public class SessionManager implements ISessionManager {

	private Object lock;
	private List<ISessionListener> listeners;

	private List<ICoverageSession> sessions;
	private Map<Object, List<ICoverageSession>> launchMap;
	private Map<String, ICoverageSession> profileMap;
	private ICoverageSession activeSession;

	public SessionManager() {
		this.lock = new Object();
		this.listeners = new ArrayList<>();
		this.sessions = new ArrayList<>();
		this.launchMap = new HashMap<>();
		this.profileMap = new HashMap<>();
		this.activeSession = null;
	}

	@Override
	public void activateSession(ICoverageSession session) {
		synchronized (lock) {
			if (sessions.contains(session) && !session.equals(activeSession)) {
				activeSession = session;
				fireSessionActivated(session);
			}
		}
	}

	@Override
	public void addSession(ICoverageSession session, boolean activate, ILaunch launch) {
		synchronized (lock) {
			if (session == null) {
				throw new IllegalArgumentException();
			}
			if (!sessions.contains(session)) {
				sessions.add(session);
				if (launch != null) {
					List<ICoverageSession> l = launchMap.get(launch);
					if (l == null) {
						l = new ArrayList<>();
						launchMap.put(launch, l);
					}
					l.add(session);
				}
				profileMap.put(session.getProfileName(), session);
				fireSessionAdded(session);
			}
			if (activate) {
				activeSession = session;
				fireSessionActivated(session);
			}
		}
	}

	@Override
	public void addSessionListener(ISessionListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		synchronized (lock) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	@Override
	public ICoverageSession getActiveSession() {
		synchronized (lock) {
			return activeSession;
		}
	}

	@Override
	public ICoverageSession getSessionByName(String name) {
		return profileMap.get(name);
	}

	@Override
	public List<ICoverageSession> getSessions() {
		synchronized (lock) {
			return new ArrayList<>(sessions);
		}
	}

	@Override
	public ICoverageSession mergeSessions(Collection<ICoverageSession> sessions1, String description,
			IProgressMonitor monitor) throws CoreException {
		monitor.beginTask(CoreMessages.MergingCoverageSessions_task, sessions1.size());

		// // Merge all sessions
		// final Set<URI> scope = new HashSet<>();
		// final Set<ILaunchConfiguration> launches = new HashSet<>();
		// final ProfilingResultsDataSource memory = new ProfilingResultsDataSource();
		// for (ICoverageSession session : sessions) {
		// scope.addAll(session.getScope());
		// if (session.getLaunchConfiguration() != null) {
		// launches.add(session.getLaunchConfiguration());
		// }
		// session.accept(memory, memory);
		// monitor.worked(1);
		// }
		//
		// // Adopt launch configuration only if there is exactly one
		// final ILaunchConfiguration launchconfiguration = launches.size() == 1 ?
		// launches.iterator().next() : null;
		// final ICoverageSession merged = new CoverageSession(description, scope,
		// memory, launchconfiguration);
		//
		// // Update session list
		// synchronized (lock) {
		// addSession(merged, true, null);
		// for (ICoverageSession session : sessions) {
		// removeSession(session);
		// }
		// }

		monitor.done();
		// return merged;
		return null;
	}

	@Override
	public void refreshActiveSession() {
		synchronized (lock) {
			if (activeSession != null) {
				fireSessionActivated(activeSession);
			}
		}
	}

	@Override
	public void removeAllSessions() {
		synchronized (lock) {
			removeSessions(sessions);
		}
	}

	@Override
	public void removeSession(ICoverageSession session) {
		synchronized (lock) {
			removeSessions(Collections.singleton(session));
		}
	}

	@Override
	public void removeSessionListener(ISessionListener listener) {
		synchronized (lock) {
			listeners.remove(listener);
		}
	}

	@Override
	public void removeSessionsFor(ILaunch launch) {
		synchronized (lock) {
			List<ICoverageSession> sessionsToRemove = launchMap.get(launch);
			if (sessionsToRemove != null) {
				removeSessions(sessionsToRemove);
			}
		}
	}

	private void fireSessionActivated(ICoverageSession session) {
		// copy to avoid concurrent modification issues
		for (ISessionListener l : new ArrayList<>(listeners)) {
			l.sessionActivated(session);
		}
	}

	private void fireSessionAdded(ICoverageSession session) {
		// copy to avoid concurrent modification issues
		for (ISessionListener l : new ArrayList<>(listeners)) {
			l.sessionAdded(session);
		}
	}

	private void fireSessionRemoved(ICoverageSession session) {
		// copy to avoid concurrent modification issues
		for (ISessionListener l : new ArrayList<>(listeners)) {
			l.sessionRemoved(session);
		}
	}

	private void removeSessions(Collection<ICoverageSession> sessionsToRemove) {
		// Clone as in some scenarios we're modifying the caller's instance
		sessionsToRemove = new ArrayList<>(sessionsToRemove);

		// Remove Sessions
		List<ICoverageSession> removedSessions = new ArrayList<>();
		for (ICoverageSession s : sessionsToRemove) {
			if (sessions.remove(s)) {
				removedSessions.add(s);
				for (List<ICoverageSession> mappedSessions : launchMap.values()) {
					mappedSessions.remove(s);
				}
			}
		}

		// Activate other session if active session was removed:
		boolean actived = sessionsToRemove.contains(activeSession);
		if (actived) {
			int size = sessions.size();
			activeSession = size == 0 ? null : sessions.get(size - 1);
		}

		// Fire events:
		for (ICoverageSession s : removedSessions) {
			fireSessionRemoved(s);
		}
		if (actived) {
			fireSessionActivated(activeSession);
		}
	}

}
