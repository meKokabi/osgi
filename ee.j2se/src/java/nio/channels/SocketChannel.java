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

package java.nio.channels;
public abstract class SocketChannel extends java.nio.channels.spi.AbstractSelectableChannel implements java.nio.channels.ByteChannel, java.nio.channels.GatheringByteChannel, java.nio.channels.NetworkChannel, java.nio.channels.ScatteringByteChannel {
	protected SocketChannel(java.nio.channels.spi.SelectorProvider var0)  { super((java.nio.channels.spi.SelectorProvider) null); } 
	public abstract java.nio.channels.SocketChannel bind(java.net.SocketAddress var0) throws java.io.IOException;
	public abstract boolean connect(java.net.SocketAddress var0) throws java.io.IOException;
	public abstract boolean finishConnect() throws java.io.IOException;
	public abstract java.net.SocketAddress getRemoteAddress() throws java.io.IOException;
	public abstract boolean isConnected();
	public abstract boolean isConnectionPending();
	public static java.nio.channels.SocketChannel open() throws java.io.IOException { return null; }
	public static java.nio.channels.SocketChannel open(java.net.SocketAddress var0) throws java.io.IOException { return null; }
	public abstract int read(java.nio.ByteBuffer var0) throws java.io.IOException;
	public final long read(java.nio.ByteBuffer[] var0) throws java.io.IOException { return 0l; }
	public abstract <T> java.nio.channels.SocketChannel setOption(java.net.SocketOption<T> var0, T var1) throws java.io.IOException;
	public abstract java.nio.channels.SocketChannel shutdownInput() throws java.io.IOException;
	public abstract java.nio.channels.SocketChannel shutdownOutput() throws java.io.IOException;
	public abstract java.net.Socket socket();
	public final int validOps() { return 0; }
	public abstract int write(java.nio.ByteBuffer var0) throws java.io.IOException;
	public final long write(java.nio.ByteBuffer[] var0) throws java.io.IOException { return 0l; }
}
