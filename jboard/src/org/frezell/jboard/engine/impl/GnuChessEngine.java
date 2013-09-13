/*
 * Copyright (c) 2003 Drew Frezell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.frezell.jboard.engine.impl;

import org.frezell.jboard.*;
import org.frezell.jboard.engine.*;
import org.frezell.util.swing.*;

import java.io.*;
import java.util.logging.*;

public class GnuChessEngine implements Engine {
	public static final String EXE = "gnuchess xboard";
	public static final String MOVE_PREFIX = "My move is: ";

	private Output m_stdout;
	private Input m_stderr;
	private Input m_stdin;

	public GnuChessEngine() {
		init();
	}

	public void init() {
		Thread exe = new Thread(new ExeRunner());
		exe.start();
	}

	public void move(GameState state, Move opMove) {
		Worker worker = new Worker(state, opMove);
		worker.start();
	}

	public class Output {
		private BufferedWriter m_writer;

		public Output(OutputStream out) {
			m_writer = new BufferedWriter(new OutputStreamWriter(out));
		}

		public void write(String command) {
			try {
				m_writer.write(command);
				m_writer.write('\n');
				m_writer.flush();
			} catch (IOException ioe) {
				Logger.getAnonymousLogger().warning(ioe.getMessage());
			}
		}
	}

	public class Input {
		private BufferedReader m_reader;

		public Input(InputStream in) {
			m_reader = new BufferedReader(new InputStreamReader(in));
		}

		public String read() {
			String command = null;
			try {
				command = m_reader.readLine();
			} catch (IOException ioe) {
				Logger.getAnonymousLogger().warning(ioe.getMessage());
			}
			return command;
		}
	}

	public class ExeRunner implements Runnable {
		public void run() {
			try {
				final Process p = Runtime.getRuntime().exec(EXE);
				m_stdout = new Output(p.getOutputStream());
				m_stdin = new Input(p.getInputStream());
				m_stderr = new Input(p.getErrorStream());
				// Read the chess output.
				m_stdin.read();

				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run() {
						p.destroy();
					}
				});

				p.waitFor();
			} catch (IOException ioe) {
				Logger.getAnonymousLogger().warning(ioe.getMessage());
			} catch (InterruptedException ie) {
				Logger.getAnonymousLogger().warning(ie.getMessage());
			}
		}
	}

	public class Worker extends SwingWorker {
		private GameState m_state;
		private Move m_opMove;

		public Worker(GameState state, Move opMove) {
			m_state = state;
			m_opMove = opMove;
		}

		public Object construct() {
			m_stdout.write(m_opMove.getDescrip());
			String command = null;

			while (!(command = m_stdin.read()).startsWith(MOVE_PREFIX)) ;
			Move move = new Move(command.substring(MOVE_PREFIX.length()));

			return move;
		}

		public void finished() {
			Move move = (Move) get();
			m_state.move(move);
		}
	}
}