package eu.luckyApp.stickers.creators;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.javafx.iio.ImageStorage.ImageType;

import eu.luckyApp.stickers.model.Material;

public class StickyImageTest {
	StickyImage si;
	private static final int WIDTH = 2480 / 4; // SZER A4 PRZY 300DPI 2480x3508
	// przy 300 dpi
private static final int HEIGHT = 3508 / 10;// WYS A4 PRZY 300 DPI
private static final int ROZDZIEL = 3;
private int OGRANICZNIK = 20;



private Material material;


	@Before
	public void setUp() throws Exception {
		si=new StickyImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		material=new Material("CZ-AI0791310501433","£o¿ysko 6300 to jest ³o¿ysko i bardzo d³ugi tekst","SZT","0410",13.0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDrawMaterialsData() {
		//fail("Not yet implemented");
		si.drawMaterialsData(material,null);
		
		
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("d:/my.png");
	
		ImageIO.write(si, "png", fos); // image3
		fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
