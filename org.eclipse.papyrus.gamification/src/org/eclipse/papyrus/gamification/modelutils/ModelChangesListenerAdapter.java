/*****************************************************************************
 * Copyright (c) 2020 CEA LIST and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   CEA LIST - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.gamification.modelutils;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.papyrus.gamification.games.framework.communication.OnModelChangedItf;

/**
 * @author maximesavaryleblanc
 *
 */

public class ModelChangesListenerAdapter extends EContentAdapter {

	private OnModelChangedItf onModelChangedItf;

	public ModelChangesListenerAdapter(OnModelChangedItf onModelChangedItf) {
		super();
		this.onModelChangedItf = onModelChangedItf;
	}


	/**
	 * @see org.eclipse.emf.ecore.util.EContentAdapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 *
	 * @param notification
	 */
	@Override
	public void notifyChanged(Notification notification) {
		onModelChangedItf.onModelChanged(notification);
	}
}