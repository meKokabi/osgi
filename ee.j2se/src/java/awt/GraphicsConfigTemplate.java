/*
 * Copyright (c) OSGi Alliance (2001, 2013). All Rights Reserved.
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

package java.awt;
public abstract class GraphicsConfigTemplate implements java.io.Serializable {
	public final static int PREFERRED = 2;
	public final static int REQUIRED = 1;
	public final static int UNNECESSARY = 3;
	public GraphicsConfigTemplate() { } 
	public abstract java.awt.GraphicsConfiguration getBestConfiguration(java.awt.GraphicsConfiguration[] var0);
	public abstract boolean isGraphicsConfigSupported(java.awt.GraphicsConfiguration var0);
}
