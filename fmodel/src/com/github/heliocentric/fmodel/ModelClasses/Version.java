/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel.ModelClasses;

/**
 *
 * @author helio
 */
public class Version {
	@Override
	public String toString() {
		return String.valueOf(Major) + "." + String.valueOf(Minor) + "." + String.valueOf(Revision);
	}
	private int Major;
	private int Minor;
	private int Revision;
	public int getMajor() {
		return this.Major;
	}
	public int getMinor() {
		return this.Minor;
	}
	public int getRevision() {
		return this.Revision;
	}
	public Version(int Major, int Minor, int Revision) {
		this.Major = Major;
		this.Minor = Minor;
		this.Revision = Revision;
	}
	
}
