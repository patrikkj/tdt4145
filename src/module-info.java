/**
 * @author Patrik
 */
module iexercise {
	exports main.app;
	exports main.utils;

	requires transitive java.sql;
	requires javafx.base;
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	requires com.jfoenix;
}