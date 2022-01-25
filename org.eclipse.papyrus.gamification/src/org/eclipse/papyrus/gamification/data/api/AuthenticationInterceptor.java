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

package org.eclipse.papyrus.gamification.data.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author maximesavaryleblanc
 *
 */
public class AuthenticationInterceptor implements Interceptor {

	private String authToken;

	public AuthenticationInterceptor(String token) {
		this.authToken = token;
	}

	@Override
	public Response intercept(Chain chain) throws IOException {
		Request original = chain.request();

		Request.Builder builder = original.newBuilder()
				.header("Authorization", authToken);

		Request request = builder.build();
		return chain.proceed(request);
	}
}