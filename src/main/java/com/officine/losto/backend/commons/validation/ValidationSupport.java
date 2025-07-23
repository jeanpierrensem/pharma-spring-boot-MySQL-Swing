package com.officine.losto.backend.commons.validation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.GregorianCalendar;

import com.officine.losto.params.constant.ConstMessagesEN;


public abstract class ValidationSupport {

	protected boolean isNullOrEmptyString(String value) {
		return value == null || value.isEmpty();
	}

	protected boolean isNullValue(Object value) {
		return value == null; 
	}

	protected boolean isValueNotGreaterThanZero(Long value) {
		return isNullValue(value) || value <= 0;
	}

	protected boolean isValueNotGreaterZero(Long value) {
		return isNullValue(value) || value <= 0;
	}

	protected boolean isValueLessThanZero(int value) {
		return value < 0;
	}

	protected boolean isEmptyList(Collection<?> list) {
		return list.size() == 0;
	}

	
	protected  boolean isValidDate(String date) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(ConstMessagesEN.Params.DATE_FORMAT);
			dateFormat.setCalendar(new GregorianCalendar());
			dateFormat.setLenient(false);
			dateFormat.parse(date);
		}

		catch (ParseException pe) {
			System.out.println("Erreur de conversion de date "); 
			return false;
		}
		return true;
	}
	
	
	
	public boolean isInteger(String value) {
	    try {
	        Integer.parseInt(value);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}



}
