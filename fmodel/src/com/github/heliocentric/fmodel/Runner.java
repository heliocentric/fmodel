/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel;

import CommandUI.CommandControl;
import CommandUI.CommandView;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author helio
 */
public class Runner {
	public static void main(String [ ] args) throws Exception
	{
		Model model = new Model(new CommandControl(), new CommandView());
		try {
			model.Start();
		} catch (Exception ex) {
			Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
