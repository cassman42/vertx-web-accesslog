/*
 * Copyright (c) 2016-2017 Roman Pierson
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 
 * which accompanies this distribution.
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */
package com.mdac.vertx.web.accesslogger.configuration.pattern;

import java.util.Map;

import io.vertx.core.MultiMap;

public class HeaderElement implements AccessLogElement{

	private final Mode mode;
	private final String identifier; 
	
	private enum Mode{
		
		INCOMING,	
		OUTGOING
		
	}
	public HeaderElement() {
		this.mode = null;
		this.identifier = null;
	}
	
	private HeaderElement(final Mode mode, final String identifier) {
		this.mode = mode;
		this.identifier = identifier;
	}

	@Override
	public ExtractedPosition findInRawPatternInternal(final String rawPattern) {
		
		final int index = rawPattern.indexOf("%{");
		
		if(index >= 0){
				
			
				int indexEndConfiguration = rawPattern.indexOf("}");
			
				if(indexEndConfiguration > index 
					&& rawPattern.length() > indexEndConfiguration
					&& (rawPattern.charAt(indexEndConfiguration + 1) == 'i' || rawPattern.charAt(indexEndConfiguration + 1) == 'o'))
				{
					String configurationString = rawPattern.substring(index + 2, indexEndConfiguration);
					
					return new ExtractedPosition(index, configurationString.length() + 4, new HeaderElement(rawPattern.charAt(indexEndConfiguration + 1) == 'i' ? Mode.INCOMING : Mode.OUTGOING, configurationString));
				}
			
		}
		
		
		return null;
	}
	
	@Override
	public String getFormattedValue(final Map<String, Object> values) {
		
		final MultiMap headers =  Mode.INCOMING.equals(this.mode) ? (MultiMap) values.get("requestHeaders") : (MultiMap) values.get("responseHeaders");
		
		if(headers.contains(this.identifier)){
			return headers.get(this.identifier);
		} else {
			return "-";
		}
		
	}

}
