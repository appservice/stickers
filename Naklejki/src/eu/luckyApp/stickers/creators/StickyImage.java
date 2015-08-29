package eu.luckyApp.stickers.creators;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JTextArea;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import eu.luckyApp.stickers.model.Material;

public class StickyImage extends BufferedImage {
	
	private int frameHeight;
	private int frameWidth;
	private static final int ROZDZIEL = 3;
private Map<Integer,String>brancheMap=null;

	

	public StickyImage(int width, int height, int imageType) {
	
		super(width, height, imageType);
	//	this.brancheMap=brancheMap;
		this.frameHeight= height;// / 10; // podzielona strona a4 na 10
		// wierszy
		this.frameWidth= width;// / 2;

	}

	public void drawMaterialsData(Material material,Map<Integer,String>brancheMap) {
		this.brancheMap=brancheMap;

		createGraphic(material, frameHeight, frameWidth, this);
		
		
	}

	private void createGraphic(Material material, int frameHeight, int frameWidth, StickyImage stickyImage) {
		Graphics2D g = this.createGraphics();
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, frameWidth, frameHeight);

		drawQrCode(material, frameWidth, g);

		drawMaterialName(material, frameWidth, g);

		drawPosition(material, frameWidth, g);

		drawIndex(material, g);
		g.dispose();

		
	}

	private void drawIndex(Material material, Graphics2D g) {
		g.setColor(Color.BLACK);
		String index = material.getSplittedIndexId();
		g.setFont(new Font("Times New Roman", Font.PLAIN, 17 * ROZDZIEL));
		g.drawString(index, 24 * ROZDZIEL, 100 * ROZDZIEL);
		
		
	}

	private void drawPosition(Material material, int frameWidth, Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times New Roman", Font.BOLD, 30 * ROZDZIEL));

		// ------pokazuj pozycje------
		String pozycja;
		String []positionTable=material.getPositionAccordingCPGLregule();
		
		
		if (material.getIndexId().length()>4&&material.getIndexId().substring(0, 5).equals("CZ-AI")) {
			pozycja = "Poz. " + material.getIndexId().substring(14, 18) + "    ga³. "
					+ material.getIndexId().substring(10, 14);
			g.setFont(new Font("Times New Roman", Font.ITALIC, 12 * ROZDZIEL));
			g.drawString(pozycja, frameWidth / 2 - g.getFontMetrics().stringWidth(pozycja) / 2, 224);
		}
		if (material.getIndexId().substring(0, 6).equals("079989")) {

			pozycja = "Poz. " + material.getIndexId().substring(8, 11) + "/" + material.getIndexId().substring(11, 13)
					+ "  ga³. " + material.getIndexId().substring(6, 8) + " \"" + getIndexBranche(material.getIndexId())
					+ "\"";
			BufferedImage positionImage = createImageFromTextPane(pozycja, frameWidth  - 80, 50,
					new Font("Times New Roman", // New Roman
							Font.ITALIC, 10 * ROZDZIEL));

			g.drawImage(positionImage, null, 40, 200);
			positionImage.flush();
		} 		
	}


	private String getIndexBranche(String indexId) {
		int brancheNummer = Integer.parseInt(indexId.substring(6, 8));
		String brancheName = brancheMap.get(brancheNummer);
		if (brancheName != null) {
			return brancheName;
		} else {
			return "";
		}
	}

	private void drawMaterialName(Material material, int frameWidth, Graphics2D g) {
		// g.setColor(Color.red);

		Font font = new Font("Times New Roman", Font.TRUETYPE_FONT, 13 * ROZDZIEL);
		BufferedImage imageTextPane = createImageFromTextPane(material.getIndexName(),
				frameWidth  - 60 * ROZDZIEL - 40, 150, font);
		g.drawImage(imageTextPane, null, 40, 40);
		imageTextPane.flush();
		
	}

	private void drawQrCode(Material material, int frameWidth, Graphics2D g) {
		BufferedImage imageOfQrCode = material.returnQrCodeImage(60 * ROZDZIEL);
		g.drawImage(imageOfQrCode, null, frameWidth  - 60 * ROZDZIEL, 20); // (frameHeight-imageOfQrCode.getHeight())/2
		imageOfQrCode.flush();
		
	}
	
	
	private BufferedImage createImageFromTextPane(String text, int width, int height, Font font) {
		StyledDocument document = new DefaultStyledDocument();
		Style defaultStyle = document.getStyle(StyleContext.DEFAULT_STYLE);
		StyleConstants.setAlignment(defaultStyle, StyleConstants.ALIGN_CENTER);

		JTextArea jpa = new JTextArea(document);
		jpa.setBounds(0, 0, width, height);
		jpa.setFont(font);

		jpa.setWrapStyleWord(true);
		jpa.setLineWrap(true);
		jpa.setText(text);
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		// jpa.p
		jpa.paint(g);
		return bi;
	}


}
