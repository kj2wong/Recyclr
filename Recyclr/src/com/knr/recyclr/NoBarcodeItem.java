package com.knr.recyclr;

public class NoBarcodeItem {

	String itemName;
	DisposeType type;
	String comment;
	
	public enum DisposeType {
        Recycle, Trash, Compost;
   } 
	
	public NoBarcodeItem(String name, DisposeType type, String comment) {
		this.itemName = name;
		this.type = type;
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return this.itemName + " : " + this.type.toString();
	}
}
