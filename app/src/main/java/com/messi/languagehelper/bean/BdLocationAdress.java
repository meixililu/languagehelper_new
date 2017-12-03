package com.messi.languagehelper.bean;

public class BdLocationAdress {

	private String country_code_iso;
	private String country;
	private String province;
	private String city;
	private String district;
	private String adcode;

	public String getCountry_code_iso() {
		return country_code_iso;
	}

	public void setCountry_code_iso(String country_code_iso) {
		this.country_code_iso = country_code_iso;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAdcode() {
		return adcode;
	}

	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}
}