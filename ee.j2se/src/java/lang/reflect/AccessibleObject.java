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

package java.lang.reflect;
public class AccessibleObject implements java.lang.reflect.AnnotatedElement {
	protected AccessibleObject() { } 
	public <T extends java.lang.annotation.Annotation> T getAnnotation(java.lang.Class<T> var0) { return null; }
	public java.lang.annotation.Annotation[] getAnnotations() { return null; }
	public java.lang.annotation.Annotation[] getDeclaredAnnotations() { return null; }
	public boolean isAccessible() { return false; }
	public boolean isAnnotationPresent(java.lang.Class<? extends java.lang.annotation.Annotation> var0) { return false; }
	public void setAccessible(boolean var0) { }
	public static void setAccessible(java.lang.reflect.AccessibleObject[] var0, boolean var1) { }
}
