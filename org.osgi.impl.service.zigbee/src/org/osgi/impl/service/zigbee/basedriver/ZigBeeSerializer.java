/*
 * Copyright (c) OSGi Alliance (2016). All Rights Reserved.
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

package org.osgi.impl.service.zigbee.basedriver;

import java.io.EOFException;
import org.osgi.service.zigbee.ZigBeeDataInput;
import org.osgi.service.zigbee.ZigBeeDataOutput;

/**
 * Utility class used to implement a ZCLFrame interface.
 */
public class ZigBeeSerializer {

	/**
	 * The buffer used to store the ZCLFrame payload.
	 */
	protected byte[]	data	= null;

	protected boolean	isEmpty	= true;

	int					index	= 0;

	/**
	 * Basic constructor. It creates a ZCLFrame with the passed ZCLHeader and
	 * with a maxPayloadZise of 30 bytes.
	 * 
	 * @param header a ZCLHeader instance
	 */
	public ZigBeeSerializer() {
		this.data = new byte[30];
		this.isEmpty = true;
	}

	/**
	 * Creates a ZigBeeSerializer and initalize it with the passed payload
	 * payload.
	 * 
	 * @param payload The ZCLFrame payload.
	 */

	public ZigBeeSerializer(byte[] payload) {
		this.data = payload;
		this.isEmpty = false;
		this.index = payload.length;
	}

	/**
	 * Creates a serializer with the specified maximum buffer size
	 * 
	 * @param size The maximum size of the internal buffer used to store the
	 *        data
	 */
	public ZigBeeSerializer(int size) {
		this.data = new byte[size];
	}

	/**
	 * Return a copy of the part of the internal buffer that actually contains
	 * data.
	 * 
	 * returned byte array (@see ZCLFrameImpl}
	 */

	public byte[] getBytes() {
		byte[] d = new byte[index];
		System.arraycopy(data, 0, d, 0, index);
		return d;
	}

	protected int getIndex() {
		return index;
	}

	public ZigBeeDataInput getDataInput() {
		return new ZigBeeDataInputImpl(this);
	}

	/**
	 * Returns an instance of ZigBeeDataOutput stream.
	 * 
	 * @return zigBeeDataOutput
	 */
	public ZigBeeDataOutput getDataOutput() {
		return new ZigBeeDataOutputImpl(this);
	}

	/**
	 * Read a byte from the internal data array at the specified index.
	 * 
	 * @param pos
	 * @return
	 * @throws EOFException
	 */
	public byte readByte(int pos) throws EOFException {
		try {
			if (pos >= index) {
				throw new EOFException();
			}
			return data[pos];
		} catch (IndexOutOfBoundsException e) {
			throw new EOFException();
		}
	}

	public int readInt(int pos, int size) throws EOFException {
		if (size <= 4 && size != 0) {
			return (int) readLong(pos, size);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public long readLong(int pos, int size) throws EOFException {
		if (size <= 8 && size != 0) {
			try {
				long l = 0;
				pos += size;
				for (int i = 0; i < size; i++) {
					short s = (short) (data[--pos] & 0xFF);
					l = (l <<= 8) | s;
				}
				pos += size;
				return l;
			} catch (IndexOutOfBoundsException e) {
				throw new EOFException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public float readFloat(int pos, int size) throws EOFException {
		if (size == 4) {
			int raw = readInt(pos, 4);
			return Float.intBitsToFloat(raw);
		} else if (size == 2) {
			int raw = readInt(pos, 2);

			// seee eemm mmmm mmmm, hidden bit is 1

			int s = (raw >> 15) & 0x01;
			int e = (raw >> 10) & 0x1f;
			int m = raw & 0x3ff;

			if (e == 31 && m != 0) {
				// NaN
				return Float.NaN;
			} else if (e == 31 && m == 0) {
				// Infinity
				if (s == 1) {
					return Float.NEGATIVE_INFINITY;
				} else {
					return Float.POSITIVE_INFINITY;
				}
			} else if (e == 0 && m == 0) {
				// Zero
				return 0f;
			} else if (e == 0) {
				int v = (s << 31) | m << 13;
				return Float.intBitsToFloat(v);
			} else {
				int v = (s << 31) | ((e - 15 + 127) << 23) | m << 13;
				return Float.intBitsToFloat(v);
			}
		} else {
			throw new IllegalArgumentException("invalid size");
		}
	}

	public double readDouble(int pos) throws EOFException {
		long raw = readLong(pos, 8);
		return Double.longBitsToDouble(raw);
	}

	public byte[] readBytes(int pos, int len) throws EOFException {
		if (pos + len > this.data.length) {
			throw new EOFException();
		}
		byte[] bytes = new byte[len];
		System.arraycopy(data, pos, bytes, 0, len);
		pos += len;
		return bytes;
	}

	public void writeByte(byte value) {
		data[index++] = value;
	}

	public void writeInt(int value, int size) {
		if (size == 0 || size > 4) {
			throw new IllegalArgumentException();
		}
		this.writeLong(value, size);
	}

	public void writeLong(long value, int size) {
		if (size == 0 || size > 8) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < size; i++) {
			data[index++] = (byte) (value & 0xFF);
			value >>= 8;
		}
	}

	public void writeFloat(float value, int size) {
		int bits;
		if (size == 4) {
			bits = Float.floatToRawIntBits(value);
			this.writeInt(bits, 4);
		} else if (size == 2) {
			bits = Float.floatToRawIntBits(value);

			// convert the 4 bytes float into a 2 bytes float
			int s = (bits >> 31) & 0x01;
			int e = ((bits >> 23) & 0xff);
			int m = bits & 0x7fffff;

			if (e == 0xff) {
				if (m == 0) {
					if (s == 0) {
						bits = 0x7c00; // +Infinite
					} else {
						bits = 0xfc00; // -Infinite
					}
				} else {
					// m != 0
					bits = 0x7d00; // NaN
				}
			} else if (e == 0) {
				// denormalized
				bits = (m >> 13) + s << 15;
			} else {
				// a number
				int e1 = e - 127 + 15;
				if (e1 > 31) {
					if (s == 0) {
						bits = 0x7c00; // +Infinite
					} else {
						bits = 0xfc00; // -Infinite
					}
				} else {
					bits = s << 15 | e1 << 10 | m >> 13;
				}
			}

			this.writeInt(bits, 2);
		} else {
			throw new IllegalArgumentException("invalid size");
		}
	}

	public void writeDouble(double value) {
		long bits = Double.doubleToRawLongBits(value);
		this.writeLong(bits, 8);
	}

	public void writeBytes(byte[] bytes, int length) {
		if ((bytes == null) || (length == 0)) {
			throw new IllegalArgumentException();
		}

		for (int i = 0; i < length; i++) {
			this.writeByte(bytes[i]);
		}
	}
}