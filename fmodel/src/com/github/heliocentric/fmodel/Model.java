/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel;

import com.github.heliocentric.fmodel.ModelClasses.World;

/**
 *
 * @author Helio
 */
public class Model {
	public World World;
	public Model(Controller control, View view) throws Exception {
		this.World = new World("test");
		
	}
	public void Start() throws Exception {
	}
	public void Stop() throws Exception {
		this.World.Close();
	}
}
