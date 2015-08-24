package eu.luckyApp.stickers.controllers;

import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import eu.luckyApp.stickers.model.Material;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

public class StickerCreator extends Task<Void> {
	private List<Material> materialsList;
	private TextArea logTextArea;
	
	
	
	private static final int WIDTH = 2480 / 1; // SZER A4 PRZY 300DPI 2480x3508
	// przy 300 dpi
	private static final int HEIGHT = 3508 / 1;// WYS A4 PRZY 300 DPI
	// private static final int ROZDZIELCZOSC = 300; // ROZDZIELCZOSC 300 DPI
	// private static final String NAZWA_PLIKU_XLS = "bx_bx2_ex_ck";
	// private static String NAZWA_PLIKU_XLS = "ob-";
	// private String nazwa_pliku_xls;

	private static final int ROZDZIEL = 3;
	private int OGRANICZNIK = 20;
	private static int DLUGOSC_TEKSTU_W_LINII = 30; // by³o 16
	private boolean pokazuj_pozycje = false;
	private DefaultCaret caret;
	
	private Preferences userPrefs=Preferences.userNodeForPackage(MainController.class);

	public StickerCreator(List<Material> materialsList) {
		this.materialsList = materialsList;
	}



//	}

	// ---------------------- pole opisu z rozdzielaniem
	// wyrazów----------------------------
	private BufferedImage createImageFromTextPane(String string, int width, int height, Font font) {
		StyledDocument document = new DefaultStyledDocument();
		Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);

		// document.se
		// System.out.println("d³ tekstu:=" + document.getLength());
		// JTextPane jpa = new JTextPane(document);

		JTextArea jpa = new JTextArea(document);
		jpa.setBounds(0, 0, width, height);
		// System.out.println(jpa.getWidth());
		jpa.setFont(font);
		// System.out.println(font.getSize()*string.length());
		// jpa.setBackground(Color.red);
		jpa.setWrapStyleWord(true);
		jpa.setLineWrap(true);
		// jpa.set
		jpa.setText(string);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		// jpa.p
		jpa.paint(g);
		return bi;
	}

	// ---------------------Nazwy
	// ga³êzi-----------------------------------------------------------------

	/*
	 * private BufferedImage returnQrCodeImage(String data) { try { Hashtable
	 * hints = new Hashtable(); hints.put(EncodeHintType.CHARACTER_SET,
	 * "UTF-8"); // get a byte matrix for the data ByteMatrix matrix;
	 * com.google.zxing.qrcode.QRCodeWriter writer = new QRCodeWriter();
	 * 
	 * matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, 60 *
	 * ROZDZIEL, 60 * ROZDZIEL, hints);
	 * 
	 * // generate an image from the byte matrix int width = matrix.getWidth();
	 * int height = matrix.getHeight();
	 * 
	 * byte[][] array = matrix.getArray();
	 * 
	 * // create buffered image to draw to BufferedImage image = new
	 * BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	 * 
	 * // iterate through the matrix and draw the pixels to the // image for
	 * (int y = 0; y < height; y++) { for (int x = 0; x < width; x++) { int
	 * grayValue = array[y][x] & 0xff; image.setRGB(x, y, (grayValue == 0 ? 0 :
	 * 0xFFFFFF)); } } return image; } catch (WriterException ex) {
	 * System.out.println(ex); //
	 * Logger.getLogger(NaklejkiMale_szufladkiView.class.getName()).log(Level.
	 * SEVERE, null, ex); } return null; }
	 */

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
	// -----------------------------pobranie Nazwy ga³êzi z
	// indeksu-----------------------------------------------------

	String getIndexBranche(String index) {
		int brancheNummer = Integer.parseInt(index.substring(6, 8));
		String brancheName = brancheMap.get(brancheNummer);
		if (brancheName != null) {
			return brancheName;
		} else {
			return "";
		}
	}

	public TextArea getLogTextArea() {
		return logTextArea;
	}

	public void setLogTextArea(TextArea logTextArea) {
		this.logTextArea = logTextArea;
	}

	@Override
	protected Void call() throws Exception {

		fillBrancheMap(); // ³¹duje nazwy ga³êzi

		int iloscDrawing = 0;
		int i = 0;
		int licznik_ilosc = 1;

	//	for (Material material : materialsList) {

		//	System.out.println(material);
		
			while(i<materialsList.size()){
			BufferedImage image3 = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D g3 = image3.createGraphics();
			g3.setColor(new Color(255, 255, 255));
			g3.fillRect(0, 0, WIDTH, HEIGHT);

			for (int k = 0; k < 10; k++) {
				for (int j = 0; j < 4; j++) {
					
					if(i>=materialsList.size()){
						break;
					}
					Material material=materialsList.get(i);
					//System.out.println(i+1+": "+material);
				//	logTextArea.setText();
					logTextArea.appendText(i+1+": "+material);
					
					
					BufferedImage imageOfQrCode = material.returnQrCodeImage(60 * ROZDZIEL);
					int frameHeight = HEIGHT / 10; // podzielona strona a4 na 10
													// wierszy
					int frameWidth = WIDTH / 2; // podzielona strona A4 na 2
												// kolumny

					BufferedImage oneStickyImage = new BufferedImage(frameWidth, frameHeight,
							BufferedImage.TYPE_INT_RGB);

					Graphics2D g = oneStickyImage.createGraphics();
					g.setColor(new Color(255, 255, 255));
					g.fillRect(0, 0, frameWidth, frameHeight);

					g.drawImage(imageOfQrCode, null, frameWidth / 2 - 60 * ROZDZIEL, 20); // (frameHeight-imageOfQrCode.getHeight())/2

					BufferedImage imageTextPane;
					// g.setColor(new Color(255, 255, 255));
					imageTextPane = createImageFromTextPane(material.getIndexName(),
							frameWidth / 2 - 60 * ROZDZIEL - 40, 150, new Font("Times New Roman", // SansSerif
																									// Times
																									// New
																									// Roman
									Font.TRUETYPE_FONT, 13 * ROZDZIEL));
					g.drawImage(imageTextPane, null, 40, 40);

					g.setColor(Color.BLACK);
					g.setFont(new Font("Times New Roman", Font.BOLD, 30 * ROZDZIEL));
					// g.drawString(symbolSegera, frameWidth / 4 -
					// g.getFontMetrics().stringWidth(symbolSegera) / 2, 65 *
					// ROZDZIEL + 10);

					// ------pokazuj pozycje------
					String pozycja;
					if (material.getIndexId().substring(0, 5).equals("CZ-AI")) {
						pozycja = "Poz. " + material.getIndexId().substring(14, 18) + "    ga³. "
								+ material.getIndexId().substring(10, 14);
						g.setFont(new Font("Times New Roman", Font.ITALIC, 12 * ROZDZIEL));
						g.drawString(pozycja, frameWidth / 4 - g.getFontMetrics().stringWidth(pozycja) / 2, 224);
					}
					if (material.getIndexId().substring(0, 6).equals("079989")) {

						pozycja = "Poz. " + material.getIndexId().substring(8, 11) + "/"
								+ material.getIndexId().substring(11, 13) + "  ga³. "
								+ material.getIndexId().substring(6, 8) + " \"" + getIndexBranche(material.getIndexId())
								+ "\"";
						BufferedImage positionImage = createImageFromTextPane(pozycja, frameWidth / 2 - 80, 50,
								new Font("Times New Roman", // SansSerif Times
															// New Roman
										Font.ITALIC, 10 * ROZDZIEL));
						// g.setFont(new Font("Times New Roman", Font.ITALIC, 12
						// * ROZDZIEL));
						// g.drawString(pozycja, frameWidth / 4 -
						// g.getFontMetrics().stringWidth(pozycja) / 2, 224);
						g.drawImage(positionImage, null, 40, 200);
					}

					g.setFont(new Font("Times New Roman", Font.PLAIN, 17 * ROZDZIEL));
					String index;
					if (material.getIndexId().substring(0, 4).equalsIgnoreCase("CZ-A")) {
						index = material.getIndexId().substring(0, 5) + " " + material.getIndexId().substring(5, 10)
								+ " " + material.getIndexId().substring(10, 14) + " "
								+ material.getIndexId().substring(14, 18);
					} else if ((material.getIndexId().length() == 16)
							&& ((material.getIndexId().substring(0, 1).equals("0")
									|| material.getIndexId().substring(0, 1).equals("1")
									|| material.getIndexId().substring(0, 1).equals("2")))) {

						index = material.getIndexId().substring(0, 4) + "-" + material.getIndexId().substring(4, 8)
								+ "-" + material.getIndexId().substring(8, 12) + "-"
								+ material.getIndexId().substring(12, 16);
					} else {
						index = material.getIndexId();
					}

					g.drawString(index, 24 * ROZDZIEL, 100 * ROZDZIEL);
					
					
                    //-----------------------------------------------------------------------
                    if (material.getIndexUnit().equalsIgnoreCase("SZT")
                            || material.getIndexUnit().equalsIgnoreCase("KOM")) {
                      int   ilosc = material.getIndexAmount().intValue();
                        if (ilosc > OGRANICZNIK) {

                            ilosc = OGRANICZNIK;
                            // }
                        }
                        //System.out.println("uwaga"+licznik_ilosc+"   "+ilosc+"\n");
                        if (licznik_ilosc < ilosc) {
                            i--;
                            licznik_ilosc++;

                        } else {
                            licznik_ilosc = 1;
                        }

                    }
					
					g3.drawImage(oneStickyImage, null, j * WIDTH / 4, k * HEIGHT / 10 + k * 5 - 20);
					 i++;
				
					 Thread.sleep(30);
				}
				
			}

			try {
				String defoultPath= Paths.get(".").toAbsolutePath().normalize().toString();

				String filePath=userPrefs.get("file.location", defoultPath);
				String fileName="naklejka" + " " + (iloscDrawing + 1) + ".png";
				File output = new File(filePath,fileName);
				//System.out.println("utworzono plik ");
			
				logTextArea.appendText("Utworzono plik: "+fileName+ " w folderze "+filePath+"\n\n");
				//logTextArea.appendText();
				FileOutputStream fos = new FileOutputStream(output);
				ImageIO.write(image3, "png", fos); // image3
				fos.close();

				iloscDrawing++;
			} catch (Exception ex) {
				System.out.println(ex);
			}}
		
		return null;
	}

}
