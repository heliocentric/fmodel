/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel.Model;

/**
 *
 * @author helio
 */
public abstract class Entity extends Object {
	public abstract void SetWorld(World world);
	public abstract void Tick();
}
