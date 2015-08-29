package eu.luckyApp.stickers.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class SettingsController implements Initializable {

	@FXML
	private TextField settingsRepeatAmount;

	private Preferences userPrefs = Preferences.userNodeForPackage(MainController.class);

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		int repeatAmount = userPrefs.getInt("settings.repeatamount", 20);
		settingsRepeatAmount.setText(String.valueOf(repeatAmount));
	}

	@FXML
	public void saveSettingsBtnHandler(ActionEvent evt) {

		userPrefs.put("settings.repeatamount", settingsRepeatAmount.getText());
	//	Button btn = (Button) evt.getSource();
		//Stage stage = (Stage) btn.getScene().getWindow();
		//stage.close();
	}
}
