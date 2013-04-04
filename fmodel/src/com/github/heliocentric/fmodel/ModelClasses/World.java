/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel.ModelClasses;
import com.github.heliocentric.fmodel.Controller;
import com.github.heliocentric.fmodel.View;
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
	public World(Controller control, View view, String Name) throws Exception {
		this.setController(control);
		this.setView(view);
		this._Constructor("~", Name);
	}
	
	
	private Connection _Connection;
	
	
	
	private void _Constructor(String Path, String Name) throws Exception {
		Class.forName("org.h2.Driver");
		this._Connection = DriverManager.getConnection("jdbc:h2:" + Path + File.separator + Name,"sa","");
		if (this.GetVersion().toString().equals("0.0.0")) 
		{
			this.Create();
			this.Debug(99, "New database created.");
		}
		this.Upgrade();
		this.Debug(99, "Schema Version: " + this.GetVersion().toString());
	}

	public void Create() throws SQLException {
		if (CreateV1_1_0() == true) {
			
		}
	}
	public boolean CreateV1_1_0() throws SQLException {
		String query;
		Statement stmt;
		int rs;
		
		stmt = this._Connection.createStatement();
		query = "CREATE TABLE tblConfig (fldConfigID INT PRIMARY KEY AUTO_INCREMENT, fldConfigName VARCHAR(255), fldConfigValue VARCHAR(255))";
		rs = stmt.executeUpdate(query);
		
		stmt = this._Connection.createStatement();
		query = "INSERT INTO tblConfig (fldConfigName,fldConfigValue) VALUES('schema_major',1)";
		rs = stmt.executeUpdate(query);
		
		stmt = this._Connection.createStatement();
		query = "INSERT INTO tblConfig (fldConfigName,fldConfigValue) VALUES('schema_minor',1)";
		rs = stmt.executeUpdate(query);
		
		stmt = this._Connection.createStatement();
		query = "INSERT INTO tblConfig (fldConfigName,fldConfigValue) VALUES('schema_revision',0)";
		rs = stmt.executeUpdate(query);
		
		return true;
	}
	public void Upgrade() {
		String query;
		Statement stmt;
		int rs;
		Version ver = this.GetVersion();
		if (ver.getMajor() == 1) {
			/*
			 * Schema 1.1.1 upgrade
			 */
			if (this.GetVersion().toString().equals("1.1.0")) {
				try {
					
					stmt = this._Connection.createStatement();
					query = "CREATE TABLE tblEntity (fldEntityID VARCHAR(36) PRIMARY KEY, fldEntityType VARCHAR(36))";
					rs = stmt.executeUpdate(query);
					
					stmt = this._Connection.createStatement();
					query = "UPDATE tblConfig SET fldConfigValue = '1' WHERE fldConfigName = 'schema_revision'";
					rs = stmt.executeUpdate(query);
					
				} catch (SQLException ex) {
					this.DebugException(0, ex);
				}
			}
			
			/*
			 * Schema 1.1.2 upgrade
			 * 
			 */
			if (this.GetVersion().toString().equals("1.1.1")) {
				try {
					
					stmt = this._Connection.createStatement();
					query = "ALTER TABLE tblEntity ADD fldEntityCreated INT";
					rs = stmt.executeUpdate(query);
					
					stmt = this._Connection.createStatement();
					query = "UPDATE tblConfig SET fldConfigValue = '2' WHERE fldConfigName = 'schema_revision'";
					rs = stmt.executeUpdate(query);
					
				} catch (SQLException ex) {
					this.DebugException(0, ex);
				}
			}
			
			/*
			 * Schema 1.1.3 upgrade
			 * 
			 */
			if (this.GetVersion().toString().equals("1.1.2")) {
				try {
					
					stmt = this._Connection.createStatement();
					query = "CREATE TABLE listEntityType (fldEntityTypeID VARCHAR(36) PRIMARY KEY, fldEntityTypeName VARCHAR(255))";
					rs = stmt.executeUpdate(query);
					
					stmt = this._Connection.createStatement();
					query = "INSERT INTO listEntityType (fldEntityTypeID,fldEntityTypeName) VALUES('463E0291-35C6-4261-8054-004897FAD77F','Person')";
					rs = stmt.executeUpdate(query);

					stmt = this._Connection.createStatement();
					query = "INSERT INTO listEntityType (fldEntityTypeID,fldEntityTypeName) VALUES('3E8FA71F-D827-40B7-826E-88C9B2DAA326','CorporateEntity')";
					rs = stmt.executeUpdate(query);
					
					stmt = this._Connection.createStatement();
					query = "INSERT INTO listEntityType (fldEntityTypeID,fldEntityTypeName) VALUES('E0B867EB-D9C1-4D1E-997D-5B79A5F5EB14','Nature')";
					rs = stmt.executeUpdate(query);
					
					
					stmt = this._Connection.createStatement();
					query = "UPDATE tblConfig SET fldConfigValue = '3' WHERE fldConfigName = 'schema_revision'";
					rs = stmt.executeUpdate(query);
					
				} catch (SQLException ex) {
					this.DebugException(0, ex);
				}
			}
		}
	}
	public Version GetVersion()  {
		/*
		 * Here we attempt to get the schema version of the database, using the three components, major, minor, and revision.
		 * 
		 * Revision is the most common change, and are directly linear, and it will automatically be updated.
		 * 
		 * Minor is not automatic. Minor versions implement any change that cannot be safely done in a single transaction,
		 * and therefore requires a new database file to be created. This is a restriction of h2, but it makes sense to do a proper rollback.
		 * 
		 * Major is a big change that is non-trivial to roll back.
		 */
		Version ver = new Version(0,0,0);
		String query = "SELECT * FROM tblConfig WHERE fldConfigName = 'schema_major' OR fldConfigName = 'schema_minor' OR fldConfigName = 'schema_revision'";
		Statement stmt;
		try {
			stmt = this._Connection.createStatement();
			ResultSet rs =  stmt.executeQuery(query);
			int Major = 0;
			int Minor = 0;
			int Revision = 0;
			while (rs.next()) {
				String Name = rs.getString("fldConfigName");
				if (Name.equals("schema_major")) 
				{
					Major = rs.getInt("fldConfigValue");
				} else if (Name.equals("schema_minor")) {
					Minor = rs.getInt("fldConfigValue");
				} else if (Name.equals("schema_revision")) {
					Revision = rs.getInt("fldConfigValue");
				}
			}
			ver = new Version(Major, Minor, Revision);
		} catch (SQLException ex) {
			
			if (ex.getErrorCode() == 42102) {
				this.DebugException(99,ex);
				return ver;
			} else {
				this.DebugException(0, ex);
			}
		}
		return ver;
	}
	
	public void Close() throws Exception {
		this._Connection.close();
	}
	private void DebugException(int Level, Exception Error) {
		this.Debug(Level, Error.toString());
	}
	private void Debug(int Level, String Error) {
		ViewMessage message = new ViewMessage();
		message.Type = ViewMessage.Types.View_Debug;
		message.setString(Error);
		message.setInteger(Level);
		this._view.Enqueue(message);
	}
	private View _view;
	public final void setView(View view) {
		this._view = view;
	}
	private Controller _controller;
	public final void setController(Controller control) {
		this._controller = control;
	}
}
