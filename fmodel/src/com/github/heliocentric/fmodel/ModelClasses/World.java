/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel.ModelClasses;
import java.io.File;
import java.sql.*;
/**
 *
 * @author helio
 */
public class World {
	public World(String Path, String Name) throws Exception {
		this._Constructor(Path, Name);
	}
	public World(String Name) throws Exception {
		this._Constructor("~", Name);
	}
	
	
	private Connection _Connection;
	
	
	
	private void _Constructor(String Path, String Name) throws Exception {
		Class.forName("org.h2.Driver");
		this._Connection = DriverManager.getConnection("jdbc:h2:" + Path + File.separator + Name,"sa","");
		if (this.GetVersion().toString().equals("0.0.0")) {
			
		}
		this.Upgrade();
	}

	public void Create() {
		if (CreateV1_1_0() == true) {
			
		}
	}
	public boolean CreateV1_1_0() {
		return true;
	}
	public void Upgrade() {
	
	}
	public Version GetVersion() {
		Version ver = new Version(0,0,0);
		
		return ver;
	}
	
	public void Close() throws Exception {
		this._Connection.close();
	}
}
