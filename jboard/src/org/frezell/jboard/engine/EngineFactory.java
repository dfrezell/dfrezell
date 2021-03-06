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
package org.frezell.jboard.engine;

import org.frezell.jboard.engine.impl.*;

import java.util.logging.*;

public class EngineFactory {
	public static final Engine createEngine(String engineClassName) {
		Engine engine = null;
		try {
			Class clazz = ClassLoader.getSystemClassLoader().loadClass(engineClassName);
			engine = (Engine) clazz.newInstance();
		} catch (ClassNotFoundException cnfe) {
			Logger.getAnonymousLogger().warning(cnfe.getMessage());
		} catch (IllegalAccessException iae) {
			Logger.getAnonymousLogger().warning(iae.getMessage());
		} catch (InstantiationException ie) {
			Logger.getAnonymousLogger().warning(ie.getMessage());
		}
		return engine;
	}

	public static final Engine createGnuChessEngine() {
		return new GnuChessEngine();
	}

	public static final Engine createNullEngine() {
		return new NullEngine();
	}

	public static final Engine createHumanEngine() {
		return new HumanEngine();
	}
}
