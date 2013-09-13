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

package org.frezell.swing;

import javax.swing.*;
import java.awt.*;

public class ImageButton extends JButton {
    public ImageButton(Image main) {
        super(new ImageIcon(main));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    public void setImage(Image image) {
        setIcon(new ImageIcon(image));
    }

    public void setDisabledImage(Image image) {
        setDisabledIcon(new ImageIcon(image));
    }

    public void setRolloverImage(Image image) {
        setRolloverIcon(new ImageIcon(image));
    }

    public void setPressedImage(Image image) {
        setPressedIcon(new ImageIcon(image));
    }

    public void setSelectedImage(Image image) {
        setSelectedIcon(new ImageIcon(image));
    }

    public void setDisabledSelectedImage(Image image) {
        setDisabledSelectedIcon(new ImageIcon(image));
    }

    public void setRolloverSelectedImage(Image image) {
        setRolloverSelectedIcon(new ImageIcon(image));
    }
}
