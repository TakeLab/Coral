package hr.fer.zemris.ktlab.sap.test;

import hr.fer.zemris.ktlab.sap.util.DataModel;

public class TestDataModel {
	
	public static void main(String[] args) {
		DataModel dm = new DataModel();
		int key1 = dm.add1("aaa");
		int key2 = dm.add2("sssa");
		int key3 = dm.add2("asdwad");
		
		dm.addConnection(key1, key2);
		dm.addConnection(key1, key3);
		
		System.out.println("Connections for " + key1 + ": " + dm.getConnections(key1));
		System.out.println("Connections for " + key2 + ": " + dm.getConnections(key2));
		
		dm.splitElement(key3, 2, true);
		
		for (Integer key : dm.getKeys2()) {
			System.out.println(key + ", " + dm.getElement(key));
		}
		
		System.out.println("-----");
		
		dm.combineElements(key2, key3);
		
		for (Integer key : dm.getKeys2()) {
			System.out.println(key + ", " + dm.getElement(key));
		}
	}

}
