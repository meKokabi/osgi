/*
 * Copyright (c) OSGi Alliance (2001, 2009). All Rights Reserved.
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

package java.lang;
@java.lang.annotation.Target(value={java.lang.annotation.ElementType.TYPE,java.lang.annotation.ElementType.FIELD,java.lang.annotation.ElementType.METHOD,java.lang.annotation.ElementType.PARAMETER,java.lang.annotation.ElementType.CONSTRUCTOR,java.lang.annotation.ElementType.LOCAL_VARIABLE})
@java.lang.annotation.Retention(value=java.lang.annotation.RetentionPolicy.SOURCE)
public @interface SuppressWarnings {
	java.lang.String[] value();
}
