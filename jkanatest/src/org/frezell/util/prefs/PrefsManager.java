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


package org.frezell.util.prefs;


import org.frezell.util.Singleton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Drew Frezell
 * @version $Revision: 1.3 $
 */
public class PrefsManager implements Singleton {
    private static PrefsManager g_self = new PrefsManager();
    private static HashMap g_map = new HashMap();
    private boolean m_has14 = true;

    private PrefsManager() {
        try {
			// apparently this check doesn't work in the latest java
            //if (java.util.prefs.Preferences.class != Object.class) {
                Runtime.getRuntime().addShutdownHook(new ShutdownHook());
            //}
        } catch (Throwable e) {
            m_has14 = false;
        }
    }

    public static PrefsManager instance() {
        return g_self;
    }

    public void put(Class clazz, String key, String def) {
        if (!m_has14) {
            return;
        }

        Preferences prefs = userNodeForPackage(clazz);
        prefs.put(key, def);
    }

    public void putBoolean(Class clazz, String key, boolean def) {
        if (!m_has14) {
            return;
        }

        Preferences prefs = userNodeForPackage(clazz);
        prefs.putBoolean(key, def);
    }

    public void putByteArray(Class clazz, String key, byte[] def) {
        if (!m_has14) {
            return;
        }

        Preferences prefs = userNodeForPackage(clazz);
        prefs.putByteArray(key, def);
    }

    public void putDouble(Class clazz, String key, double def) {
        if (!m_has14) {
            return;
        }

        Preferences prefs = userNodeForPackage(clazz);
        prefs.putDouble(key, def);
    }

    public void putFloat(Class clazz, String key, float def) {
        if (!m_has14) {
            return;
        }

        Preferences prefs = userNodeForPackage(clazz);
        prefs.putFloat(key, def);
    }

    public void putInt(Class clazz, String key, int def) {
        if (!m_has14) {
            return;
        }

        Preferences prefs = userNodeForPackage(clazz);
        prefs.putInt(key, def);
    }

    public void putLong(Class clazz, String key, long def) {
        if (!m_has14) {
            return;
        }

        Preferences prefs = userNodeForPackage(clazz);
        prefs.putLong(key, def);
    }

    public String get(Class clazz, String key, String def) {
        if (!m_has14) {
            return def;
        }

        Preferences prefs = userNodeForPackage(clazz);
        return prefs.get(key, def);
    }

    public boolean getBoolean(Class clazz, String key, boolean def) {
        if (!m_has14) {
            return def;
        }

        Preferences prefs = userNodeForPackage(clazz);
        return prefs.getBoolean(key, def);
    }

    public byte[] getByteArray(Class clazz, String key, byte[] def) {
        if (!m_has14) {
            return def;
        }

        Preferences prefs = userNodeForPackage(clazz);
        return prefs.getByteArray(key, def);
    }

    public double getDouble(Class clazz, String key, double def) {
        if (!m_has14) {
            return def;
        }

        Preferences prefs = userNodeForPackage(clazz);
        return prefs.getDouble(key, def);
    }

    public float getFloat(Class clazz, String key, float def) {
        if (!m_has14) {
            return def;
        }

        Preferences prefs = userNodeForPackage(clazz);
        return prefs.getFloat(key, def);
    }

    public int getInt(Class clazz, String key, int def) {
        if (!m_has14) {
            return def;
        }

        Preferences prefs = userNodeForPackage(clazz);
        return prefs.getInt(key, def);
    }

    public long getLong(Class clazz, String key, long def) {
        if (!m_has14) {
            return def;
        }

        Preferences prefs = userNodeForPackage(clazz);
        return prefs.getLong(key, def);
    }

    private Preferences userNodeForPackage(Class clazz) {
        Preferences prefs = (Preferences) g_map.get(clazz);

        if (prefs == null) {
            prefs = Preferences.userNodeForPackage(clazz);
            g_map.put(clazz, prefs);
        }

        return prefs;
    }

    private class ShutdownHook extends Thread {
        public void run() {
            Iterator it = g_map.values().iterator();
            Preferences pref;

            while (it.hasNext()) {
                pref = (Preferences) it.next();
                try {
                    pref.flush();
                } catch (BackingStoreException bse) {
                    bse.printStackTrace();
                }
            }
        }
    }
}


