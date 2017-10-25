/*
 * Copyright 2013 Stanley Shyiko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.shyiko.mysql.binlog.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:stanley.shyiko@gmail.com">Stanley Shyiko</a>
 */
public class ByteArrayInputStream extends InputStream {

    private InputStream inputStream;
    private Integer peek;
    private int blockLength = -1;
    private int fixedSize = 0;
    private static Charset defCharset = Charset.forName("UTF-8");

    public ByteArrayInputStream(InputStream inputStream, Charset charset) {
        this.inputStream = inputStream;
        this.defCharset = charset;
    }

    public ByteArrayInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ByteArrayInputStream(byte[] bytes) {
        this(new java.io.ByteArrayInputStream(bytes));
    }

    public ByteArrayInputStream(byte[] bytes, Charset charset) {
        this(new java.io.ByteArrayInputStream(bytes), charset);
    }

    public ByteArrayInputStream subStream(int length) throws IOException {
        byte[] bytes = read(length);
        return new ByteArrayInputStream(bytes, this.defCharset);
    }


    public Charset getCharset() {
        return defCharset;
    }

    public <T extends ByteArrayInputStream> T setCharset(Charset charset) {
        this.defCharset = charset;
        return (T) this;
    }

    /**
     * Read int written in little-endian format.
     */
    public int readInteger(int length) throws IOException {
        int result = 0;
        for (int i = 0; i < length; ++i) {
            result |= (this.read() << (i << 3));
        }
        return result;
    }

    public int readInteger2() throws IOException {
        int result = this.read();
        result |= this.read() << 8;
        return result;
    }

    public int readInteger3() throws IOException {
        int result = this.read();
        result |= this.read() << 8;
        result |= this.read() << 16;
        return result;
    }

    public int readInteger4() throws IOException {
        int result = this.read();
        result |= this.read() << 8;
        result |= this.read() << 16;
        result |= this.read() << 24;
        return result;
    }

    /**
     * Read long written in little-endian format.
     */
    public long readLong(int length) throws IOException {
        long result = 0;
        for (int i = 0; i < length; ++i) {
            result |= (((long) this.read()) << (i << 3));
        }
        return result;
    }

    /**
     * Read fixed length string.
     */
    public String readString(int length) throws IOException {
        return new String(read(length), defCharset);
    }

    /**
     * Read fixed length string (given charset)
     */
    public String readString(int length, Charset charset) throws IOException {
        return new String(read(length), charset);
    }

    /**
     * Read fixed length string with possible zero padding (with default charset)
     */
    public String readStringPadded(int length) throws IOException {
        return readStringPadded(length,  defCharset);
    }

    /**
     * Read fixed length string with possible zero padding (with charset)
     */
    public String readStringPadded(int length, Charset charset) throws IOException {
        byte arr[] = read(length);
        int off = 0;
        for(;off < arr.length; off++) {
            if(arr[off] == 0) {
                break;
            }
        }
        return new String(arr,0,off, charset);
    }

    /**
     * Read variable-length string. Preceding packed integer indicates the length of the string.
     */
    public String readLengthEncodedString() throws IOException {
        return readString(readPackedInteger(), defCharset);
    }

    /**
     * Read variable-length string. Preceding packed integer indicates the length of the string.
     */
    public String readLengthEncodedString(Charset charset) throws IOException {
        return readString(readPackedInteger(), charset);
    }

    /**
     * Read variable-length string. End is indicated by 0x00 byte.
     */
    public String readZeroTerminatedString() throws IOException {
        return readZeroTerminatedString(defCharset);
    }

    /**
     * Read variable-length string. End is indicated by 0x00 byte (with charset)
     */
    public String readZeroTerminatedString(Charset charset) throws IOException {
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        for (int b; (b = this.read()) != 0; ) {
            s.writeInteger(b, 1);
        }
        return new String(s.toByteArray(), charset);
    }

    /**
     * Read variable-length string. End is indicated by 0x00 byte (or to length)
     */
    public String readZeroTerminatedEOFString(Charset charset) throws IOException {
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        for(;;) {
            int b = this.read();
            if(b <= 0) {
                break;
            }
            s.writeInteger(b, 1);
        }
        return new String(s.toByteArray(), charset);
    }
    public String readZeroTerminatedEOFString() throws IOException {
        return readZeroTerminatedEOFString(defCharset);
    }


    public byte[] read(int length) throws IOException {
        byte[] bytes = new byte[length];
        fill(bytes, 0, length);
        return bytes;
    }

    public void fill(byte[] bytes, int offset, int length) throws IOException {
        int remaining = length;
        while (remaining != 0) {
            int read = read(bytes, offset + length - remaining, remaining);
            if (read == -1) {
                throw new EOFException();
            }
            remaining -= read;
        }
    }

    public BitSet readBitSet(int length, boolean bigEndian) throws IOException {
        // according to MySQL internals the amount of storage required for N columns is INT((N+7)/8) bytes
        byte[] bytes = read((length + 7) >> 3);
        bytes = bigEndian ? bytes : reverse(bytes);
        BitSet result = new BitSet();
        for (int i = 0; i < length; i++) {
            if ((bytes[i >> 3] & (1 << (i % 8))) != 0) {
                result.set(i);
            }
        }
        return result;
    }

    private byte[] reverse(byte[] bytes) {
        for (int i = 0, length = bytes.length >> 1; i < length; i++) {
            int j = bytes.length - 1 - i;
            byte t = bytes[i];
            bytes[i] = bytes[j];
            bytes[j] = t;
        }
        return bytes;
    }

    /**
     * @see #readPackedNumber()
     */
    public int readPackedInteger() throws IOException {
        int b = this.read();
        if (b < 251) {
            return b;
        } else if (b == 251) {
            throw new IOException("try to read NULL packed integer");
        } else if (b == 252) {
            return readInteger(2);
        } else if (b == 253) {
            return readInteger(3);
        } else if (b == 254) {
            long v = readLong(8);
            if (v > Integer.MAX_VALUE) {
                throw new IOException("Stumbled upon long even though int expected: " + v);
            }
            return (int) v;
        } else {
            throw new IOException("Unexpected packed number byte " + b);
        }
    }

    /**
     * Format (first-byte-based):<br/>
     * 0-250 - The first byte is the number (in the range 0-250). No additional bytes are used.<br/>
     * 251 - SQL NULL value<br/>
     * 252 - Two more bytes are used. The number is in the range 251-0xffff.<br/>
     * 253 - Three more bytes are used. The number is in the range 0xffff-0xffffff.<br/>
     * 254 - Eight more bytes are used. The number is in the range 0xffffff-0xffffffffffffffff.
     */
    public Number readPackedNumber() throws IOException {
        int b = this.read();
        if (b < 251) {
            return b;
        } else if (b == 251) {
            return null;
        } else if (b == 252) {
            return (long) readInteger2();
        } else if (b == 253) {
            return (long) readInteger3();
        } else if (b == 254) {
            return readLong(8);
        }
        throw new IOException("Unexpected packed number byte " + b);
    }

    @Override
    public int available() throws IOException {
        if (blockLength != -1) {
            return blockLength;
        }
        return inputStream.available();
    }

    public int peek() throws IOException {
        if (peek == null) {
            peek = readWithinBlockBoundaries();
        }
        return peek;
    }

    @Override
    public int read() throws IOException {
        int result;
        if (peek == null) {
            result = readWithinBlockBoundaries();
        } else {
            result = peek;
            peek = null;
        }
        if (result == -1) {
            throw new EOFException();
        }
        return result;
    }

    private int readWithinBlockBoundaries() throws IOException {
        if (blockLength != -1) {
            if (blockLength == 0) {
                return -1;
            }
            blockLength--;
        }
        return inputStream.read();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    public void enterBlock(int length) {
        this.blockLength = length < -1 ? -1 : length;
    }

    public void skipToTheEndOfTheBlock() throws IOException {
        if (blockLength != -1) {
            skip(blockLength);
            blockLength = -1;
        }
    }

    public int getFixedSize() {
        return fixedSize;
    }

    public void setFixedSize(int fixedSize) {
        this.fixedSize = fixedSize;
    }
    
}
