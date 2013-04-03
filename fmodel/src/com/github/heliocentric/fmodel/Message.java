/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel;

/**
 *
 * @author helio
 */
public abstract class Message {
	public String _string;
	public String getString() {
		return _string;
	}
	public void setString(String string) {
		this._string = string;
	}
	public Integer _integer;
	public Integer getInteger() {
		return _integer;
	}
	public void setInteger(Integer integer) {
		this._integer = integer;
	}
}
