/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author helio
 */
public class Runner {
	public static void main(String [ ] args)
	{
		Model model = new Model();
		try {
			model.Start();
		} catch (Exception ex) {
			Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
