package eu.luckyApp.stickers.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.prefs.Preferences;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import eu.luckyApp.stickers.model.Material;
import eu.luckyApp.stickers.persers.Excel2003Parser;
import eu.luckyApp.stickers.persers.Parser;
import eu.luckyApp.stickers.persers.PdfParser;
import eu.luckyApp.stickers.utils.StickerUtils;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class MainController implements Initializable {//
	@FXML
	protected AnchorPane root;

	@FXML
	protected TableView<Material> materialsTable;

	@FXML
	protected TableColumn<Material, String> indexColumn;

	@FXML
	protected TableColumn<Material, String> nameColumn;

	@FXML
	protected TableColumn<Material, String> unitColumn;

	@FXML
	protected TableColumn<Material, Double> amountColumn;

	@FXML
	protected TableColumn<Material, String> storeColumn;

	@FXML
	protected TableColumn<Material, Number> lpColumn;

	@FXML
	protected TableColumn<Material, Boolean> deleteColumn;

	@FXML
	protected TextArea logTextArea;

	private FileChooser fileChooser;
	// private ResourceBundle bundle;

	private List<Material> materialsList = new ArrayList<>();
	private ObservableList<Material> observableMaterialList = FXCollections.observableArrayList();
	Preferences userPrefs = Preferences.userNodeForPackage(getClass());

	@FXML
	public void onFileLoad(ActionEvent event) {
		// create fileChooser
		fileChooser = new FileChooser();

		
		//File initialPath=new File()
		String path= Paths.get(".").toAbsolutePath().normalize().toString();


		fileChooser.setInitialDirectory(new File(userPrefs.get("file.location", path)));
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Pliki excel/pdf (*.xls, *.pdf)",
				"*.xls", "*.pdf");
		FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("Wszystkie pliki (*.*)", "*.*");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.getExtensionFilters().add(extFilter2);

		// load File and create observableList from it
		File choosedFile = fileChooser.showOpenDialog(null);
		if (choosedFile != null && choosedFile.canRead()) {

			Parser dao = null;
			switch (StickerUtils.getExtension(choosedFile)) {
			case "xls":

				dao = new Excel2003Parser(choosedFile);
				break;
			case "xlsx":
				break;
			case "pdf":
				dao = new PdfParser(choosedFile);
				break;
			default:
				break;

			}

			if (dao != null) {
			//	materialsList.addAll(dao.parseData());
				//materialsList.ad
				// observableMaterialList =
				// FXCollections.observableArrayList(materialsList);
				observableMaterialList.addAll(dao.parseData());
				materialsTable.setItems(observableMaterialList);
			}
			fileChooser.setInitialDirectory(choosedFile.getParentFile());
			userPrefs.put("file.location", choosedFile.getParent());
		}
		materialsTable.autosize();

	}

	@FXML
	protected void onClearAllBtnClick(ActionEvent evt) {
		this.observableMaterialList.removeAll(this.observableMaterialList);
		//this.materialsList.removeAll(materialsList);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// this.bundle = resources;
		materialsTable.setPlaceholder(new Label("Wczytaj dane"));
		materialsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		indexColumn.setCellValueFactory(new PropertyValueFactory<Material, String>("indexId"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Material, String>("indexName"));
		unitColumn.setCellValueFactory(new PropertyValueFactory<Material, String>("indexUnit"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<Material, Double>("indexAmount"));
		storeColumn.setCellValueFactory(new PropertyValueFactory<Material, String>("indexStore"));
		// TableColumn<Material, Number> lpColumn = new TableColumn<Material,
		// Number>("#");
		lpColumn.setSortable(false);
		lpColumn.setCellValueFactory(
				column -> new ReadOnlyObjectWrapper<Number>(materialsTable.getItems().indexOf(column.getValue()) + 1));
	/*	deleteColumn.setSortable(false);

		deleteColumn.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Material, Boolean>, ObservableValue<Boolean>>() {

					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Material, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});

		deleteColumn.setCellFactory(new Callback<TableColumn<Material, Boolean>, TableCell<Material, Boolean>>() {

			@Override
			public TableCell<Material, Boolean> call(TableColumn<Material, Boolean> p) {
				return new ButtonCell();
			}

		});*/

		// Set cell factory for cells that allow editing
		Callback<TableColumn<Material, String>, TableCell<Material, String>> cellFactory = p -> new EditingCell();

		Callback<TableColumn<Material, Double>, TableCell<Material, Double>> cellFactoryDouble = p -> new EditingCellDouble();

		indexColumn.setCellFactory(cellFactory);
		
		//update observable materials list when change index
	/*	indexColumn.setOnEditCommit(e->{
				Material m = e.getRowValue();
				m.setIndexId(e.getNewValue());
				observableMaterialList.set(e.getTablePosition().getRow(), m);
		
		});

		//update observable materials list when change name
		nameColumn.setCellFactory(cellFactory);
		nameColumn.setOnEditCommit(e -> {
			Material m = e.getRowValue();
			m.setIndexName(e.getNewValue());
			observableMaterialList.set(e.getTablePosition().getRow(), m);
		});

		//update observable materials list when change unit
		unitColumn.setCellFactory(cellFactory);
		unitColumn.setOnEditCommit(e -> {
			Material m = e.getRowValue();
			m.setIndexUnit(e.getNewValue());
			observableMaterialList.set(e.getTablePosition().getRow(), m);
		});

		//update observable materials list when change store
		storeColumn.setCellFactory(cellFactory);
		storeColumn.setOnEditCommit(e -> {
			Material m = e.getRowValue();
			m.setIndexStore(e.getNewValue());
			observableMaterialList.set(e.getTablePosition().getRow(), m);
		});
		
		//update observable materials list when change amount
		amountColumn.setCellFactory(cellFactoryDouble);
		amountColumn.setOnEditCommit(e -> {
			Material m = e.getRowValue();			
			m.setIndexAmount(e.getNewValue());
			System.out.println("nowa wartosc "+e.getNewValue());
			observableMaterialList.set(e.getTablePosition().getRow(), m);
		});*/
	}

	// EditingCell - for editing capability in a TableCell
	public static class EditingCell extends TableCell<Material, String> {
		private TextField textField;

		public EditingCell() {
		}

		@Override
		public void startEdit() {
			super.startEdit();

			if (textField == null) {
				createTextField();
			}
			setText(null);
			setGraphic(textField);
			textField.selectAll();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();
			setText((String) getItem());
			textField.setText((String) getItem());
			// MainController.this.materialsList.set(, element)

			setGraphic(null);
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());

					}
					setText(null);
					setGraphic(textField);
				} else {
					setText(getString());
					setGraphic(null);
				}
			}
		}

		private void createTextField() {
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ENTER) {
						commitEdit(textField.getText());

					} else if (t.getCode() == KeyCode.ESCAPE) {
						cancelEdit();
					}
				}
			});
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}

	class EditingCellDouble extends TableCell<Material, Double> {

		private TextField textField;

		public EditingCellDouble() {
		}

		@Override
		public void startEdit() {
			super.startEdit();

			if (textField == null) {
				createTextField();
			}

			setGraphic(textField);
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			textField.selectAll();
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText(String.valueOf(getItem()));
			textField.setText(String.valueOf(getItem()));
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		}

		@Override
		public void updateItem(Double item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (textField != null) {
						textField.setText(getString());
					}
					setGraphic(textField);
					setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				} else {
					setText(getString());
					setContentDisplay(ContentDisplay.TEXT_ONLY);
				}
			}
		}

		private void createTextField() {
			textField = new TextField(getString());
			textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			textField.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent t) {
					if (t.getCode() == KeyCode.ENTER) {
						commitEdit(Double.parseDouble(textField.getText()));
					} else if (t.getCode() == KeyCode.ESCAPE) {
						cancelEdit();
					}
				}
			});
		}

		private String getString() {
			return getItem() == null ? "" : getItem().toString();
		}
	}

	// Define the button cell
	private class ButtonCell extends TableCell<Material, Boolean> {
		final Button cellButton = new Button("Usuñ");

		ButtonCell() {

			cellButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent t) {

					observableMaterialList.remove(getTableRow().getIndex());

				}
			});
		}

		// Display button if the row is not empty
		@Override
		protected void updateItem(Boolean t, boolean empty) {
			super.updateItem(t, empty);
			if (!empty) {
				setGraphic(cellButton);

			}
		}
	}

	@FXML
	protected void onDeleteRowBtnHandle(ActionEvent evt) {

		observableMaterialList.removeAll(materialsTable.getSelectionModel().getSelectedItems());

		materialsTable.getSelectionModel().clearSelection();
	}

	@SuppressWarnings("unchecked")
	@FXML
	protected void onOpenFileHandler(ActionEvent evt) {

		try {
			FileInputStream fis = new FileInputStream("dataFile");
			ObjectInputStream ois = new ObjectInputStream(fis);
			this.observableMaterialList = FXCollections.observableList((List<Material>) ois.readObject());
			//this.observableMaterialList.
			// System.out.println(this.materialsList);
			//this.observableMaterialList.addAll(materialsList);
			materialsTable.setItems(observableMaterialList);
			// list.

			ois.close();
			fis.close();

			// this.list.
		} catch (IOException | ClassNotFoundException e) {

		}
	}

	@FXML
	protected void onSaveFileHandler(ActionEvent evt) {
		try {
			FileOutputStream fis = new FileOutputStream("dataFile");
			ObjectOutputStream oos = new ObjectOutputStream(fis);
			List<Material> ml=new ArrayList<Material>(observableMaterialList);
			oos.writeObject(ml);
			oos.close();
			fis.close();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setTitle("Zapis do pliku");
			alert.setContentText("Tablea zapisana do pliku!");
			alert.show();

			/*
			 * Dialogs.create() //.owner(stage) .title("Information Dialog")
			 * .masthead(null) .message("I have a great message for you!")
			 * .showInformation();
			 */

			// Dialogs.create().title("Zapis do pliku").showInformation();
			// Dialog d=new Dialog();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("on save ");
	}

	@FXML
	protected void onExitBtnHandler(ActionEvent evt) {
		Platform.exit();
	}

	@FXML
	protected void onOpenAboutProgramHandler(ActionEvent evt) {
		Parent root;

		try {
			root = FXMLLoader.load(getClass().getResource("about_program.fxml"));
			// root=new AnchorPane();

			Stage stage = new Stage();
			stage.setTitle("O programie");
			stage.setScene(new Scene(root));
			stage.show();

			// hide this current window (if this is whant you want
			// ((Node)(event.getSource())).getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	protected void onCreateStickerBtnHandler(ActionEvent evt) {
		//System.out.println("twórz naklejki");

		//System.out.println(observableMaterialList);
		StickerCreator stickerCreator = new StickerCreator(observableMaterialList);
		stickerCreator.setLogTextArea(logTextArea);
		Thread t=new Thread(stickerCreator);
		//t.setDaemon(true);
		t.start();
		//Platform.runLater(stickerCreator);
		//ExecutorService.submit(stickerCreator);
	//	Platform.runLater(stickerCreator);
	}

}
