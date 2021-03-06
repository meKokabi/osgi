/*
 * Copyright (c) OSGi Alliance (2017). All Rights Reserved.
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
package org.osgi.test.cases.jaxrs.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("whiteboard/string")
public class StringResource {

	private final String message;

	public StringResource(String message) {
		this.message = message;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getValues() {
		return message;
	}

	@GET
	@Path("length")
	@Produces(MediaType.TEXT_PLAIN)
	public int getLength() {
		return message.length();
	}

}
