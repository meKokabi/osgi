/*
 * Copyright (c) OSGi Alliance (2014). All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osgi.test.cases.async.services;

import org.osgi.test.cases.async.services.types.Argument;

public interface MyService {
	public static final String METHOD_doSlowStuff = "doSlowStuff";
	public static final String METHOD_countSlowly = "countSlowly";
	public static final String METHOD_failSlowly = "failSlowly";
	public static final String METHOD_slowNonAsyncStuff = "slowNonAsyncStuff";
	public static final String METHOD_delegateFail = "delegateFail";
	public static final String METHOD_take = "take";
	

	void doSlowStuff(int times) throws Exception;
	
	int countSlowly(int times) throws Exception;

	int failSlowly(int times) throws Exception;

	int slowNonDelegateStuff(int times) throws Exception;

	int delegateFail() throws Exception;

	void take(Argument arg);
}