/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel.ModelClasses;
import com.github.heliocentric.fmodel.ViewMessage;
import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	public Version GetVersion()  {
		Version ver = new Version(0,0,0);
		String query = "SELECT * FROM tblConfig WHERE fldConfigName = 'schema_major' OR fldConfigName = 'schema_minor' OR fldConfigName = 'schema_revision'";
		Statement stmt;
		try {
			stmt = this._Connection.createStatement();
			ResultSet rs =  stmt.executeQuery(query);
		} catch (SQLException ex) {
			if (ex.getErrorCode() == 42102) {
				return ver;
			}
		}
		ver = new Version(1,0,0);
		return ver;
	}
	
	public void Close() throws Exception {
		this._Connection.close();
	}

	private void Debug(int Level, String Error) {
		System.out.println(Error);
	}
}
