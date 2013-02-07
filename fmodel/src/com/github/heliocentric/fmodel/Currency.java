/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel;

import java.math.BigDecimal;

/**
 *
 * @author Helio
 */
public class Currency {
	private String _code;
	private String _description;
	public Currency() {
		
	}
	public String getCode() {
		return this._code;
	}
	public void setCode(String Code) {
		this._code = Code;
	}
	public String getDescription() {
		return this._description;
	}
	public void setDescription(String Description) {
		this._description = Description;
	}
	BigDecimal Total;
	BigDecimal Unclaimed;
}
