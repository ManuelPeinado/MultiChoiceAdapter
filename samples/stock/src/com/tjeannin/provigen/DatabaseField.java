package com.tjeannin.provigen;

class DatabaseField {

	private String name;
	private String type;
	private boolean unique = false;
	private String onConflict;

	public DatabaseField(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getOnConflict() {
		return onConflict;
	}

	public void setOnConflict(String onConflict) {
		this.onConflict = onConflict;
	}
}
