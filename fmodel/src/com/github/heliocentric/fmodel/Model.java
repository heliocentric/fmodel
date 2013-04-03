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
	public void Start() throws Exception {
		this.World = new World("test");
	}
	public void Stop() throws Exception {
		this.World.Close();
	}
}
