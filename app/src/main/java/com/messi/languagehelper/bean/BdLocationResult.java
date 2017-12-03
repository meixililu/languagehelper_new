package com.messi.languagehelper.bean;

public class BdLocationResult {

	private String formatted_address;
	private BdLocationAdress addressComponent;
	private String sematic_description;

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}

	public BdLocationAdress getAddressComponent() {
		return addressComponent;
	}

	public void setAddressComponent(BdLocationAdress addressComponent) {
		this.addressComponent = addressComponent;
	}

	public String getSematic_description() {
		return sematic_description;
	}

	public void setSematic_description(String sematic_description) {
		this.sematic_description = sematic_description;
	}
}