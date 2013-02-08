/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.heliocentric.fmodel;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Helio
 */
public class Model {
	public Map<String,Currency> CurrencyList;
	public Model() {
		this.CurrencyList = new HashMap<String, Currency>();
	}
	public static Model Open() {
		Model model = new Model();
		return model;
	}
	public void Prep() {
		
	}
	public void AddCurrency(Currency currency) {
		this.CurrencyList.put(currency.getCode(), currency);
	}
	public void Close() {
		
	}
}
