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

package org.osgi.service.cm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.osgi.annotation.bundle.Requirement;
import org.osgi.namespace.implementation.ImplementationNamespace;
import org.osgi.service.cm.ConfigurationConstants;

/**
 * This annotation can be used to require the Configuration Admin
 * implementation. It can be used directly, or as a meta-annotation.
 * 
 * @author $Id$
 * @since 1.6
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
@Requirement(namespace = ImplementationNamespace.IMPLEMENTATION_NAMESPACE, //
		name = ConfigurationConstants.CONFIGURATION_ADMIN_IMPLEMENTATION, //
		version = ConfigurationConstants.CONFIGURATION_ADMIN_SPECIFICATION_VERSION)
public @interface RequireConfigurationAdmin {
	// This is a purely informational annotation and has no elements.
}