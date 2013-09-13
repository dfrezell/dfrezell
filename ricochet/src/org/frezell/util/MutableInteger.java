/*
 * Copyright (c) 2003-2004 Drew Frezell
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

package org.frezell.util;

/**
 * The <code>MutableInteger</code> class wraps a value of the primitive type
 * <code>int</code> in an object. Unlike the <code>Integer</code> provided
 * by java, this class allows the value to be changed, in other words, it is
 * mutable, thus <code>MutableInteger</code>.
 *
 * @author Drew Frezell
 * @version $Revision: 1.1 $
 * @see java.lang.Integer
 */
public class MutableInteger extends Number implements Comparable {
    /**
     * Stored int value for the <code>MutableInteger</code> wrapper class.
     */
    private int m_value;

    /**
     * Constructs a newly allocated <code>MutableInteger</code> object that
     * represents the specified <code>int</code> value.
     *
     * @param value the value to be represented by the
     *              <code>MutableInteger</code> object.
     */
    public MutableInteger(int value) {
        m_value = value;
    }

    /**
     * Constructs a newly allocated <code>Integer</code> object that
     * represents the <code>int</code> value indicated by the
     * <code>String</code> parameter. The string is converted to an
     * <code>int</code> value in exactly the manner used by the
     * <code>parseInt</code> method for radix 10.
     *
     * @param s the <code>String</code> to be converted to an
     *          <code>Integer</code>.
     * @throws NumberFormatException if the <code>String</code> does not
     *                               contain a parsable integer.
     * @see java.lang.Integer#parseInt(java.lang.String, int)
     */
    public MutableInteger(String s) throws NumberFormatException {
        m_value = Integer.parseInt(s, 10);
    }

    /**
     * Get the int value of the class.
     *
     * @return the currently stored value.
     */
    public int getValue() {
        return m_value;
    }

    /**
     * Set the int value of the class.
     *
     * @param value new value to be set for the wrapper.
     */
    public void setValue(int value) {
        m_value = value;
    }

    /**
     * Add <code>value</code> to the stored int.
     *
     * @param value
     */
    public void add(int value) {
        m_value += value;
    }

    /**
     * Subtract <code>value</code> to the stored int.
     *
     * @param value
     */
    public void subtract(int value) {
        m_value -= value;
    }

    /**
     * Returns a <code>String</code> object representing this
     * <code>Integer</code>'s value. The value is converted to signed
     * decimal representation and returned as a string, exactly as if
     * the integer value were given as an argument to the {@link
     * java.lang.Integer#toString(int)} method.
     *
     * @return a string representation of the value of this object in
     *         base&nbsp;10.
     */
    public String toString() {
        return String.valueOf(m_value);
    }

    /**
     * Returns a hash code for this <code>Integer</code>.
     *
     * @return a hash code value for this object, equal to the
     *         primitive <code>int</code> value represented by this
     *         <code>Integer</code> object.
     */
    public int hashCode() {
        return m_value;
    }

    /**
     * Compares this object to the specified object.  The result is
     * <code>true</code> if and only if the argument is not
     * <code>null</code> and is an <code>Integer</code> object that
     * contains the same <code>int</code> value as this object.
     *
     * @param obj the object to compare with.
     * @return <code>true</code> if the objects are the same;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof MutableInteger) {
            return m_value == ((MutableInteger) obj).intValue();
        }
        return false;
    }

    /**
     * Compares two <code>MutableInteger</code> objects numerically.
     *
     * @param anotherInteger the <code>Integer</code> to be compared.
     * @return	the value <code>0</code> if this <code>Integer</code> is
     * equal to the argument <code>Integer</code>; a value less than
     * <code>0</code> if this <code>Integer</code> is numerically less
     * than the argument <code>Integer</code>; and a value greater
     * than <code>0</code> if this <code>Integer</code> is numerically
     * greater than the argument <code>Integer</code> (signed
     * comparison).
     */
    public int compareTo(MutableInteger anotherInteger) {
        return m_value - anotherInteger.m_value;
    }

    /**
     * Compares this <code>MutableInteger</code> object to another object.
     * If the object is an <code>MutableInteger</code>, this function behaves
     * like <code>compareTo(MutableInteger)</code>.  Otherwise, it throws a
     * <code>ClassCastException</code> (as <code>MutableInteger</code>
     * objects are only comparable to other <code>MutableInteger</code>
     * objects).
     *
     * @param o the <code>Object</code> to be compared.
     * @return the value <code>0</code> if the argument is a
     *         <code>Integer</code> numerically equal to this
     *         <code>Integer</code>; a value less than <code>0</code>
     *         if the argument is a <code>Integer</code> numerically
     *         greater than this <code>Integer</code>; and a value
     *         greater than <code>0</code> if the argument is a
     *         <code>Integer</code> numerically less than this
     *         <code>Integer</code>.
     * @see java.lang.Comparable
     */
    public int compareTo(Object o) {
        return compareTo((MutableInteger) o);
    }

    /**
     * Returns the value of this <code>MutableInteger</code> as a
     * <code>int</code>.
     */
    public int intValue() {
        return m_value;
    }

    /**
     * Returns the value of this <code>MutableInteger</code> as a
     * <code>long</code>.
     */
    public long longValue() {
        return (long) m_value;
    }

    /**
     * Returns the value of this <code>MutableInteger</code> as a
     * <code>float</code>.
     */
    public float floatValue() {
        return (float) m_value;
    }

    /**
     * Returns the value of this <code>MutableInteger</code> as a
     * <code>double</code>.
     */
    public double doubleValue() {
        return (double) m_value;
    }
}
