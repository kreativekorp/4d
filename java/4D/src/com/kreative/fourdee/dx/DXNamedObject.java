package com.kreative.fourdee.dx;

public abstract class DXNamedObject {
	protected String name;
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(String name) {
		this.name = (name != null) ? name : "";
	}
}