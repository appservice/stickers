package eu.luckyApp.stickers.view;
	
import java.awt.SplashScreen;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class Main extends Application {
	

	
	private Stage primaryStage;
	
	@Override
	public void start(final Stage primaryStage) {
		try {
			//Get the splashscreen
			final SplashScreen splash = SplashScreen.getSplashScreen();

			
			this.primaryStage=primaryStage;
			Region root = (AnchorPane)FXMLLoader.load(getClass().getResource("naklejki2.fxml"));
			
			Preferences userPreferences=Preferences.userNodeForPackage(getClass());
		    // get window location from user preferences: use x=100, y=100, width=400, height=400 as default
		    double x = userPreferences.getDouble("stage.x", 100);
		    double y = userPreferences.getDouble("stage.y", 100);
		    double w = userPreferences.getDouble("stage.width", 800);
		    double h = userPreferences.getDouble("stage.height", 600);
		    
		    Image ico=new Image("qr_code_ic_red.png");
			primaryStage.getIcons().add(ico);
			
			
			Scene myScene=new Scene(root);
			myScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(myScene);	
			primaryStage.setX(x);
		    primaryStage.setY(y);
		    primaryStage.setWidth(w);
		    primaryStage.setHeight(h);
			primaryStage.setTitle("Naklejki");		
		
			primaryStage.show();
			


		//	System.out.println("dupa");
			//Close splashscreen
		    if (splash != null) {
		        //System.out.println("Closing splashscreen...");
		        splash.close();
		    }
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {
		Preferences userPrefs = Preferences.userNodeForPackage(getClass());
	    userPrefs.putDouble("stage.x", primaryStage.getX());
	    userPrefs.putDouble("stage.y", primaryStage.getY());
	    userPrefs.putDouble("stage.width", primaryStage.getWidth());
	    userPrefs.putDouble("stage.height", primaryStage.getHeight());
	}
	

	
}
