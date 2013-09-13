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

package org.frezell.japanese;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.io.*;
import java.nio.charset.Charset;

public class Kana {
    private String m_hiragana;
    private String m_katakana;
    private ArrayList m_romaji;

    public Kana(String hiragana, String katakana, String romaji) {
        m_hiragana = hiragana;
        m_katakana = katakana;
        m_romaji = new ArrayList();
        String[] split = romaji.split(",", 0);
        for (int i = 0; i < split.length; i++) {
            m_romaji.add(split[i]);
        }
    }

    public String getHiragana() {
        return m_hiragana;
    }

    public String getKatakana() {
        return m_katakana;
    }

    public String getRomaji() {
        return (String) m_romaji.get(0);
    }

    public boolean isRomajiMatch(String romaji) {
        for (int i = 0; i < m_romaji.size(); i++) {
            if (romaji.equals(m_romaji.get(i))) {
                return true;
            }
        }

        return false;
    }

    public static Kana[] getKanaList() {
        return genList(parseXml());
    }

    private static NodeList parseXml() {
        NodeList list = null;
        try {
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setIgnoringElementContentWhitespace(true);

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(
                            "font/kanalist.xml");
            InputSource source = new InputSource(new BufferedReader(new InputStreamReader(is, "UTF-8")));
            // Create the builder and parse the file
            Document doc = factory.newDocumentBuilder().parse(source);
            list = doc.getElementsByTagName("character");
            is.close();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static Kana[] genList(NodeList list) {
        ArrayList arrayList = new ArrayList(120);
        Node node = null;
        NamedNodeMap attr = null;

        for (int i = 0; i < list.getLength(); i++) {
            node = list.item(i);
            attr = node.getAttributes();
            arrayList.add(new Kana(attr.getNamedItem("hiragana").getNodeValue(),
                    attr.getNamedItem("katakana").getNodeValue(),
                    attr.getNamedItem("romaji").getNodeValue()));
        }
        return (Kana[]) arrayList.toArray(new Kana[arrayList.size()]);
    }
}
