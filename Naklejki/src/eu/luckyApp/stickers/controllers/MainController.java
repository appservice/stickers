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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

import eu.luckyApp.stickers.creators.StickerCreator;
import eu.luckyApp.stickers.model.Material;
import eu.luckyApp.stickers.model.MaterialPropertyWrapper;
import eu.luckyApp.stickers.persers.Excel2003Parser;
import eu.luckyApp.stickers.persers.Parser;
import eu.luckyApp.stickers.persers.PdfParser;
import eu.luckyApp.stickers.utils.StickerUtils;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
	protected TableView<MaterialPropertyWrapper> materialsTable;

	@FXML
	protected TableColumn<MaterialPropertyWrapper, String> indexColumn;

	@FXML
	protected TableColumn<MaterialPropertyWrapper, String> nameColumn;

	@FXML
	protected TableColumn<MaterialPropertyWrapper, String> unitColumn;

	@FXML
	protected TableColumn<MaterialPropertyWrapper, Double> amountColumn;

	@FXML
	protected TableColumn<MaterialPropertyWrapper, String> storeColumn;

	@FXML
	protected TableColumn<MaterialPropertyWrapper, Number> lpColumn;

	@FXML
	protected TableColumn<Material, Boolean> deleteColumn;

	@FXML
	protected TextArea logTextArea;

	private FileChooser fileChooser;
	
	
	private IntegerProperty stickerAmount=new SimpleIntegerProperty();

	private boolean isCreatingSticker;
	// private ResourceBundle bundle;

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	// private List<Material> materialsList = new ArrayList<>();
	private ObservableList<MaterialPropertyWrapper> observableMaterialList = FXCollections.observableArrayList();
	Preferences userPrefs = Preferences.userNodeForPackage(getClass());

	@FXML
	public void onFileLoad(ActionEvent event) {
		// create fileChooser
		fileChooser = new FileChooser();

		// File initialPath=new File()
		String path = Paths.get(".").toAbsolutePath().normalize().toString();

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

				this.observableMaterialList = listToObservableListExtractor(dao.parseData());
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
		// this.materialsList.removeAll(materialsList);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// this.bundle = resources;
		materialsTable.setPlaceholder(new Label("Wczytaj dane"));
		materialsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// enable multi-selection
		materialsTable.getSelectionModel().setCellSelectionEnabled(true);
		materialsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		// enable copy/paste
		// TableUtils.installCopyPasteHandler(materialsTable);
		eu.luckyApp.stickers.utils.TableUtils.installCopyPasteHandler(materialsTable);



		indexColumn.setCellValueFactory(cellData -> cellData.getValue().indexIdProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().indexNameProperty());
		unitColumn.setCellValueFactory(cellData -> cellData.getValue().indexUnitProperty());
		amountColumn.setCellValueFactory(new PropertyValueFactory<MaterialPropertyWrapper, Double>("indexAmount"));
		storeColumn.setCellValueFactory(cellData -> cellData.getValue().indexStoreProperty());
		// TableColumn<Material, Number> lpColumn = new TableColumn<Material,
		// Number>("#");
		lpColumn.setSortable(false);
		lpColumn.setCellValueFactory(
				column -> new ReadOnlyObjectWrapper<Number>(materialsTable.getItems().indexOf(column.getValue()) + 1));
		

		// Set cell factory for cells that allow editing
		Callback<TableColumn<MaterialPropertyWrapper, String>, TableCell<MaterialPropertyWrapper, String>> cellFactory = p -> new EditingCell();

		Callback<TableColumn<MaterialPropertyWrapper, Double>, TableCell<MaterialPropertyWrapper, Double>> cellFactoryDouble = p -> new EditingCellDouble();

		indexColumn.setCellFactory(cellFactory);
		nameColumn.setCellFactory(cellFactory);
		unitColumn.setCellFactory(cellFactory);
		storeColumn.setCellFactory(cellFactory);
		amountColumn.setCellFactory(cellFactoryDouble);

		// update observable materials list when change index
		indexColumn.setOnEditCommit(e -> {
			MaterialPropertyWrapper m = e.getRowValue();
			m.indexIdProperty().set(e.getNewValue());
			System.out.println("zmieniono " + m.getIndexId());

			observableMaterialList.set(e.getTablePosition().getRow(), m);

		});

		// update observable materials list when change name

		nameColumn.setOnEditCommit(e -> {
			MaterialPropertyWrapper m = e.getRowValue();
			m.setIndexName(e.getNewValue());
			observableMaterialList.set(e.getTablePosition().getRow(), m);
		});
		
		
	/*	observableMaterialList.addListener(new ListChangeListener<MaterialPropertyWrapper>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends MaterialPropertyWrapper> c) {
				
				
				System.out.println("coœ sie zmieni³o "+c.);
				stickerAmount.add(c.getList().size());
			}
		});*/
		//amountColumn.
		
		
		/*
			 * 
			 * // update observable materials list when change unit
			 * unitColumn.setCellFactory(cellFactory);
			 * unitColumn.setOnEditCommit(e -> { Material m = e.getRowValue();
			 * m.setIndexUnit(e.getNewValue());
			 * observableMaterialList.set(e.getTablePosition().getRow(), m); });
			 * 
			 * // update observable materials list when change store
			 * storeColumn.setCellFactory(cellFactory);
			 * storeColumn.setOnEditCommit(e -> { Material m = e.getRowValue();
			 * m.setIndexStore(e.getNewValue());
			 * observableMaterialList.set(e.getTablePosition().getRow(), m); });
			 * 
			 * // update observable materials list when change amount
			 * amountColumn.setCellFactory(cellFactoryDouble);
			 * amountColumn.setOnEditCommit(e -> { Material m = e.getRowValue();
			 * m.setIndexAmount(e.getNewValue()); System.out.println(
			 * "nowa wartosc " + e.getNewValue());
			 * observableMaterialList.set(e.getTablePosition().getRow(), m); });
			 */
		
		
		
	}
   
	// EditingCell - for editing capability in a TableCell
	public static class EditingCell extends TableCell<MaterialPropertyWrapper, String> {
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

	class EditingCellDouble extends TableCell<MaterialPropertyWrapper, Double> {

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
			this.observableMaterialList = listToObservableListExtractor((List<Material>) ois.readObject());
			materialsTable.setItems(observableMaterialList);

			ois.close();
			fis.close();

			// this.list.
		} catch (IOException | ClassNotFoundException e) {

		}
	}

	@FXML
	protected void onSaveFileHandler(ActionEvent evt) {
		try {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText(null);
			alert.setTitle("Zapis do pliku");
			alert.setContentText("Czy zapisaæ tabelê do pamiêci?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {
				FileOutputStream fis = new FileOutputStream("dataFile");
				ObjectOutputStream oos = new ObjectOutputStream(fis);

				List<Material> ml = observableListToListExtractor(observableMaterialList);
				oos.writeObject(ml);
				oos.close();
				fis.close();
			}

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
		Button sourceButton = (Button) evt.getSource();
		// List myList=observableMaterialList;
		List<Material> myArrayList = observableListToListExtractor(this.observableMaterialList);

		// System.out.println(myList);
		StickerCreator stickerCreator = new StickerCreator(myArrayList, logTextArea);
		// Thread t=new Thread(stickerCreator);
		// t.start();
		stickerCreator.setOnSucceeded(e -> {
			sourceButton.setText("Twórz naklejki");
			isCreatingSticker = false;
		});

		if (!isCreatingSticker) {

			executorService.execute(stickerCreator);
			isCreatingSticker = true;
			sourceButton.setText("Przerwij");
		} else {
			executorService.shutdownNow();
			isCreatingSticker = false;
			sourceButton.setText("Twórz naklejki");
			this.executorService = Executors.newSingleThreadExecutor();
		}

	}

	/**
	 * @return
	 */
	private List<Material> observableListToListExtractor(ObservableList<MaterialPropertyWrapper> ol) {
		List<Material> myArrayList = new ArrayList<>();
		for (MaterialPropertyWrapper mpw : ol) {
			myArrayList.add(mpw.getMaterial());

		}
		return myArrayList;
	}

	private ObservableList<MaterialPropertyWrapper> listToObservableListExtractor(List<Material> ml) {

		ObservableList<MaterialPropertyWrapper> ol = this.observableMaterialList;// FXCollections.observableArrayList();
		for (Material m : ml) {
			ol.add(new MaterialPropertyWrapper(m));
			//stickerAmount.set(stickerAmount.get()+m.getIndexAmount().intValue());

		}
		System.out.println(stickerAmount.get());
		return ol;
	}

	@FXML
	protected void onSettingsBtnHandler(ActionEvent evt) {
		Parent root;

		try {
			root = FXMLLoader.load(getClass().getResource("Settings.fxml"));
			// root=new AnchorPane();

			Stage stage = new Stage();
			stage.setTitle("Ustawienia");
			stage.setScene(new Scene(root));
			stage.show();

			// hide this current window (if this is whant you want
			// ((Node)(event.getSource())).getScene().getWindow().hide();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
