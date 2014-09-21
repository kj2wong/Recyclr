package com.knr.recyclr;

import org.json.*;

public class UpcItem {
	public String number;
	public String itemName;
	public String description;
	
	public UpcItem(String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			this.number = jsonObj.getString("number");
			this.itemName = jsonObj.getString("itemname");
			this.description = jsonObj.getString("description");
		} catch (JSONException e) {
			this.number = "0";
			this.itemName = "invalid item";
			this.description = "invalid UPC code";
		}
	}
}