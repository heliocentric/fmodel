/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel;

/**
 *
 * @author Helio
 */
public class Viewer {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		Model main = new Model();
		main.AddCurrency(new Currency("USD", "US Dollar"));
	}
}
