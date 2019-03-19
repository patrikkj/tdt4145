package main.app;

import main.database.DatabaseManager;


public class MainApp {
	
	public void run() {

		System.out.println("Skriv kode her ... ");
		
	}
	
	
	public static void main(String[] args) {
		MainApp app = new MainApp();
		DatabaseManager.openConnection();
		app.run();
		DatabaseManager.closeConnection();
	}
}

enum State {
	
}