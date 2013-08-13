/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.directwebremoting.util;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletOutputStream;

/**
 * This is not the evil hack you are looking for.
 * 
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public final class WriterOutputStream extends ServletOutputStream {

	/**
	 * ctor using platform default encoding
	 * 
	 * @param writer
	 */
	public WriterOutputStream(final Writer writer) {
		this.writer = writer;
	}

	/**
	 * ctor that allows us to specify how strings are created
	 * 
	 * @param writer
	 * @param encoding
	 */
	public WriterOutputStream(final Writer writer, final String encoding) {
		this.writer = writer;
		this.encoding = encoding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletOutputStream#print(java.lang.String)
	 */
	@Override
	public void print(final String s) throws IOException {
		this.writer.write(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(final byte[] ba) throws IOException {
		if (this.encoding == null) {
			this.writer.write(new String(ba));
		} else {
			this.writer.write(new String(ba, this.encoding));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	@Override
	public void write(final byte[] ba, final int off, final int len) throws IOException {
		if (this.encoding == null) {
			this.writer.write(new String(ba, off, len));
		} else {
			this.writer.write(new String(ba, off, len, this.encoding));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#write(int)
	 */
	@Override
	public synchronized void write(final int bite) throws IOException {
		this.buffer[0] = (byte) bite;
		this.write(this.buffer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#close()
	 */
	@Override
	public void close() throws IOException {
		if (this.writer != null) {
			this.writer.close();
			this.writer = null;
			this.encoding = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.OutputStream#flush()
	 */
	@Override
	public void flush() throws IOException {
		this.writer.flush();
	}

	/**
	 * The destination of all our printing
	 */
	private Writer writer;

	/**
	 * What string encoding should we use
	 */
	private String encoding = null;

	/**
	 * Buffer for write(int)
	 */
	private final byte[] buffer = new byte[1];
}
