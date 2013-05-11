/**
 * Copyright 2013 Maxime Bossard
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

package fr.mby.portal.coreimpl.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import fr.mby.portal.core.message.IInternalMimeReply;

/**
 * @author Maxime Bossard - 2013
 * 
 */
public class AbstractMimeReply extends AbstractReply implements IInternalMimeReply {

	/** Logger. */
	private static final Logger LOG = LogManager.getLogger(AbstractMimeReply.class);

	private String contentType;

	private final String characterEncoding;

	private final OutputStream backingStream;

	private OutputStream outputStream;

	private PrintWriter writer;

	private boolean commited = false;

	private boolean writerUsed = false;

	private boolean outputStreamUsed = false;

	private Locale locale;

	/**
	 * @param backingOutputStream
	 * @param backingStream
	 * 
	 */
	public AbstractMimeReply(final HttpServletResponse response, final OutputStream backingOutputStream) {
		super();

		this.contentType = response.getContentType();
		this.characterEncoding = response.getCharacterEncoding();
		this.backingStream = backingOutputStream;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public void setContentType(final String type) throws IllegalArgumentException {
		this.contentType = type;
	}

	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding;
	}

	@Override
	public synchronized PrintWriter getWriter() throws IOException, IllegalStateException {
		Assert.state(!this.outputStreamUsed, "getOutputStream() has previously been called !");

		this.writerUsed = true;

		if (this.writer == null) {
			this.writer = new PrintWriterProxy(this.backingStream);
		}

		return this.writer;
	}

	@Override
	public synchronized OutputStream getOutputStream() throws IOException, IllegalStateException {
		Assert.state(!this.writerUsed, "getWriter() has previously been called !");

		this.outputStreamUsed = true;

		if (this.outputStream == null) {
			this.outputStream = new OutputStreamProxy();
		}

		return this.outputStream;
	}

	@Override
	public synchronized void flushBuffer() throws IOException {
		if (this.commited) {
			return;
		}

		this.commited = true;

		if (this.writer != null) {
			this.writer.flush();
		}

		this.backingStream.flush();
	}

	@Override
	public boolean isCommitted() {
		return this.commited;
	}

	@Override
	public Locale getLocale() {
		return this.locale;
	}

	@Override
	public OutputStream getBackingOutputStream() {
		return this.backingStream;
	}

	/**
	 * Proxy PrinterWriter to bind the flushing with the AbstractMimeReply.
	 * 
	 * @author Maxime Bossard - 2013
	 * 
	 */
	private class PrintWriterProxy extends PrintWriter {

		/**
		 * @param file
		 * @param csn
		 * @throws FileNotFoundException
		 * @throws UnsupportedEncodingException
		 */
		public PrintWriterProxy(final File file, final String csn) throws FileNotFoundException,
				UnsupportedEncodingException {
			super(file, csn);
		}

		/**
		 * @param file
		 * @throws FileNotFoundException
		 */
		public PrintWriterProxy(final File file) throws FileNotFoundException {
			super(file);
		}

		/**
		 * @param out
		 * @param autoFlush
		 */
		public PrintWriterProxy(final OutputStream out, final boolean autoFlush) {
			super(out, autoFlush);
		}

		/**
		 * @param out
		 */
		public PrintWriterProxy(final OutputStream out) {
			super(out);
		}

		/**
		 * @param fileName
		 * @param csn
		 * @throws FileNotFoundException
		 * @throws UnsupportedEncodingException
		 */
		public PrintWriterProxy(final String fileName, final String csn) throws FileNotFoundException,
				UnsupportedEncodingException {
			super(fileName, csn);
		}

		/**
		 * @param fileName
		 * @throws FileNotFoundException
		 */
		public PrintWriterProxy(final String fileName) throws FileNotFoundException {
			super(fileName);
		}

		/**
		 * @param out
		 * @param autoFlush
		 */
		public PrintWriterProxy(final Writer out, final boolean autoFlush) {
			super(out, autoFlush);
		}

		/**
		 * @param out
		 */
		public PrintWriterProxy(final Writer out) {
			super(out);
		}

		@Override
		public void flush() {
			super.flush();
			try {
				AbstractMimeReply.this.flushBuffer();
			} catch (final IOException e) {
				AbstractMimeReply.LOG.error("Error while flushing AbstractMimeReply buffer.", e);
			}
			super.flush();
		}

		@Override
		public void close() {
			Assert.state(false, "close() call is forbidden on IMimeReply PrintWriter !");
		}

	}

	/**
	 * Proxy OutputStream to bind the flushing with the AbstractMimeReply.
	 * 
	 * @author Maxime Bossard - 2013
	 * 
	 */
	private class OutputStreamProxy extends OutputStream {

		@Override
		public void write(final int b) throws IOException {
			AbstractMimeReply.this.backingStream.write(b);
		}

		@Override
		public void write(final byte[] b) throws IOException {
			AbstractMimeReply.this.backingStream.write(b);
		}

		@Override
		public void write(final byte[] b, final int off, final int len) throws IOException {
			AbstractMimeReply.this.backingStream.write(b, off, len);
		}

		@Override
		public void flush() throws IOException {
			AbstractMimeReply.this.flushBuffer();
		}

		@Override
		public void close() throws IOException {
			Assert.state(false, "close() call is forbidden on IMimeReply OutputStream !");
		}

	}

}
