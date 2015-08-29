package eu.luckyApp.stickers.creators;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

import eu.luckyApp.stickers.controllers.MainController;
import eu.luckyApp.stickers.model.Material;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import jfxtras.util.PlatformUtil;


public class StickerCreator extends Task<Void> {
	private List<Material> materialsList;
	private TextArea logTextArea;

	private static final int WIDTH = 2480 / 1; // SZER A4 PRZY 300DPI 2480x3508
												// przy 300 dpi
	private static final int HEIGHT = 3508 / 1;// WYS A4 PRZY 300 DPI
	private static final int ROZDZIEL = 3;
	private int OGRANICZNIK ;


	private Preferences userPrefs = Preferences.userNodeForPackage(MainController.class);


	public StickerCreator(List<Material> materialsList, TextArea logTextArea) {
		this.logTextArea = logTextArea;
		this.materialsList = materialsList;
		this.OGRANICZNIK=userPrefs.getInt("settings.repeatamount", 20);
	}

	final static Map<Integer, String> brancheMap = new HashMap<Integer, String>();

	void fillBrancheMap() {
		brancheMap.put(1, "LTG 1");
		brancheMap.put(2, "Lakierka 7");
		brancheMap.put(3, "Wellman");
		brancheMap.put(4, "Drukarka 1");
		brancheMap.put(5, "Cevolani 1");
		brancheMap.put(6, "Ró¿ne");
		brancheMap.put(7, "Callahan");
		brancheMap.put(8, "Grace");
		brancheMap.put(9, "Sacmi");
		brancheMap.put(10, "Lakierka 8");
		brancheMap.put(11, "Metalbox");
		brancheMap.put(12, "Mecfond");
		brancheMap.put(13, "£o¿yska");
		brancheMap.put(14, "Elektr.");
		brancheMap.put(15, "LTG 15");
		brancheMap.put(16, "LTG 30");
		brancheMap.put(17, "Lak. 7 Mailan. 2");
		brancheMap.put(18, "Drukarka 2");
		brancheMap.put(11, "Metalbox");
		brancheMap.put(19, "Cevolani");
		brancheMap.put(20, "Soudronic");
		brancheMap.put(21, "Pasy");
		brancheMap.put(22, "Uniwersalne");
		brancheMap.put(23, "Krupp");
		brancheMap.put(24, "Sprê¿arki");
		brancheMap.put(25, "Littell 1");
		brancheMap.put(26, "Uk³ad opin");
		brancheMap.put(27, "");
		brancheMap.put(28, "Plasmatic");
		brancheMap.put(29, "Still");
		brancheMap.put(30, "Espander");
		brancheMap.put(31, "Cut-O-Mat");
		brancheMap.put(32, "LTG 8");
		brancheMap.put(33, "Sacmi");
		brancheMap.put(34, "Technotrans");
		brancheMap.put(35, "Drukarka 5");
		brancheMap.put(36, "Littell 2");
		brancheMap.put(37, "Prasa Red");
		brancheMap.put(38, "Lakierka 9");
		brancheMap.put(39, "Goldco");
		brancheMap.put(40, "Tester");
		brancheMap.put(41, "Kargas-Hammer");
		brancheMap.put(42, "No¿. Ocsam");
		brancheMap.put(43, "Drukarka 5");
		brancheMap.put(44, "CPGL Debica");
		brancheMap.put(45, "Drukarka 6");
		brancheMap.put(46, "Rumunia");
		brancheMap.put(47, "Arab Can");
		brancheMap.put(48, "End-O-Mat");
		brancheMap.put(49, "TIK, Kamoko");
		brancheMap.put(50, "Pakow. NSM");
		brancheMap.put(51, "Lak. Corima");
		brancheMap.put(52, "IGM FZC");
		brancheMap.put(53, "Pakow. Corima");
		// brancheMap.put(53, "Pakow. Corima");

	}

	/**
	 * pobranie Nazwy ga³êzi z indeksu
	 * 
	 * @param index
	 * @return
	 */
	String getIndexBranche(String index) {
		int brancheNummer = Integer.parseInt(index.substring(6, 8));
		String brancheName = brancheMap.get(brancheNummer);
		if (brancheName != null) {
			return brancheName;
		} else {
			return "";
		}
	}

	@Override
	protected Void call() throws Exception {

		fillBrancheMap(); // ³¹duje nazwy ga³êzi

		int iloscDrawing = 0;
		int i = 0;
		int licznik_ilosc = 1;


		while (i < materialsList.size()) {
			BufferedImage image3 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D g3 = createEmptyPage(image3);

			for (int k = 0; k < 10; k++) {
				for (int j = 0; j < 4; j++) {

					if (i >= materialsList.size()) {
						break;
					}
					Material material = materialsList.get(i);
					logTextArea.appendText(i + 1 + ": " + material);

					StickyImage oneStickyImage = new StickyImage(WIDTH / 4, HEIGHT / 10, BufferedImage.TYPE_INT_RGB);
					oneStickyImage.drawMaterialsData(material, brancheMap);

					// -----------------------------------------------------------------------
					if (material.getIndexUnit().equalsIgnoreCase("SZT")
							|| material.getIndexUnit().equalsIgnoreCase("KOM")) {
						int ilosc = material.getIndexAmount().intValue();
						if (ilosc > OGRANICZNIK) {

							ilosc = OGRANICZNIK;

						}

						if (licznik_ilosc < ilosc) {
							i--;
							licznik_ilosc++;

						} else {
							licznik_ilosc = 1;
						}

					}

					g3.drawImage(oneStickyImage, null, j * WIDTH / 4, k * HEIGHT / 10 + k * 5 - 20);
					oneStickyImage.flush();

					
					i++;
					PlatformUtil.waitForPaintPulse();
					// Thread.sleep(30);
				}

			}

			try {
				String defoultPath = Paths.get(".").toAbsolutePath().normalize().toString();

				String filePath = userPrefs.get("file.location", defoultPath);
				String fileName = "naklejka" + " " + (iloscDrawing + 1) + ".png";
				File output = new File(filePath, fileName);
				// System.out.println("utworzono plik ");

				logTextArea.appendText("Utworzono plik: " + fileName + " w folderze " + filePath + "\n\n");
				FileOutputStream fos = new FileOutputStream(output);
				ImageIO.write(image3, "png", fos); // image3
				fos.close();
				fos.flush();
				image3.flush();
				g3.dispose();
			

				iloscDrawing++;
			} catch (Exception ex) {
				System.out.println(ex);
			}
		}

		return null;
	}

	/**
	 * @param image3
	 * @return
	 */
	private Graphics2D createEmptyPage(BufferedImage image3) {
		Graphics2D g3 = image3.createGraphics();
		g3.setColor(new Color(255, 255, 255));
		g3.fillRect(0, 0, WIDTH, HEIGHT);
		return g3;
	}

}
