/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel.ModelClasses;

import java.math.BigDecimal;

/**
 *
 * @author Helio
 */
public class Currency {
	
	private String _code;


	public String getCode() {
		return this._code;
	}
	public void setCode(String Code) {
		this._code = Code;
	}
	
	
	
	
	
	private String _description;
	public String getDescription() {
		return this._description;
	}
	public void setDescription(String Description) {
		this._description = Description;
	}
	
	
	
	
	
	private BigDecimal _total;
	public BigDecimal getTotal() {
		return this._total;
	}
	public void setTotal(BigDecimal Total) {
	}
	
	
	
	
	
	private BigDecimal _unclaimed;
	public BigDecimal getUnclaimed() {
		return this._unclaimed;
	}
	public void setUnclaimed(BigDecimal Unclaimed) {
		this._unclaimed = Unclaimed;
	}
	
	
	
	
	
	public Currency() {
		
	}
	Currency(String code, String description) {
		this._code = code;
		this._description = description;
	}
	public void Inject(Integer Value) {
		this._total.add(new BigDecimal(Value));
		
	}
}
