/*
 * 
 *
 * Developed by Drew Frezell <dfrezell@yahoo.com>
 *
 * Changelog:
 * Tue Nov 10 14:07:40 2009 - created
 *
 */

package org.frezell.robofarm;

import java.awt.*;

/**
 * Short class description
 *
 * Long class description
 *
 * @author  <a href="mailto:dfrezell@yahoo.com">Drew Frezell</a>
 * @version 0.0.1
 */
public class RoboFarm
{
	/**
	 * Main function
	 */
	public static void main(String[] args) {
		PointerInfo ptr = MouseInfo.getPointerInfo();
		Point pt = ptr.getLocation();
		System.out.println("x = " + pt.x + ", y = " + pt.y);
		try {
			Robot rob = new Robot();
			rob.setAutoDelay(500);
			for (int i = 0; i < 50; i++) {
				rob.mouseMove(pt.x + (i * 5), pt.y + (i * 2));
			}
		} catch (AWTException awte) {
			awte.printStackTrace();
		}
	}

}

/* Modeline for ViM {{{
 * vim: set ts=4:
 * }}} */


