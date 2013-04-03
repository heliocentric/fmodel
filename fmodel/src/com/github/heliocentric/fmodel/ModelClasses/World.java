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
		
	}

	public void Create() {
		
	}
	public void Upgrade() {
	
	}
	public String GetVersion() {
		
	}
	
	public void Close() throws Exception {
		this._Connection.close();
	}
}
