package eu.luckyApp.stickers.persers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import eu.luckyApp.stickers.model.Material;

public class PdfParser implements Parser {
	private File documentPath;

	public PdfParser(File documentPath) {
		this.documentPath = documentPath;
	}

	@Override
	public List<Material> parseData() {
		PDDocument document;
		try {
			document = PDDocument.load(documentPath);

			PDFTextStripper stripper = new PDFTextStripper();

			// System.out.println(document.getNumberOfPages());

			List<Material> materialsList = new ArrayList<>();

			for (int pageNum = 0; pageNum <= document.getNumberOfPages(); pageNum++) {
				stripper.setStartPage(pageNum);
				stripper.setEndPage(pageNum);

				String[] text = stripper.getText(document).split("\\(\\+\\)");

				Stream<String> stream = Arrays.stream(text);
				List<String> linie = stream.limit(text.length - 1).map(s -> s.replace(" 101 ", ""))
						.collect(Collectors.toList());

				for (String l : linie) {
					materialsList.add(parseMaterialFromString(l));
				}

			}

			document.close();
			return materialsList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Material parseMaterialFromString(String text) {
		Material material = new Material();

		// String myText = text.substring(2, text.length());
		String[] lines = text.trim().split("\n"); // split for 2 lines

		// if(text.trim())
		String[] firstLineArray;
		String secondLine;

		// ------when pz is made from 2 or more different orders
		if (lines[0].contains("Do zamówienia nr:")) {
			firstLineArray = lines[1].split(" ");
			secondLine = lines[2];

		} else {
			// -------when pz is made only from one order-------
			firstLineArray = lines[0].split(" ");
			secondLine = lines[1];
		}
		// /* id */
		// material.setId(Long.parseLong(firstLineArray[0].replace(" ", "")));

		/* index */
		material.setIndexId(firstLineArray[1]);

		/* receivedAmount */
		// material.setReceivedUnit(Double.parseDouble(firstLineArray[2].replace(",",
		// ".")));

		// /* receivedUnit */
		// material.setReceivedUnit(firstLineArray[3]);

		/* stockAmount */
		// material.setStockAmount(Double.parseDouble(firstLineArray[4].replace(",",
		// ".")));
		material.setIndexAmount(Double.parseDouble(firstLineArray[4].replace(",", ".")));

		/* stockUnit */
		// material.setStockUnit(firstLineArray[5].trim());
		material.setIndexUnit(firstLineArray[5].trim());

		/* materialName */
		//material.setMaterialName(secondLine.substring(0, secondLine.length() - 9));
		material.setIndexName(secondLine.substring(0, secondLine.length() - 9));
		
		/* companyNumber */
		//material.setCompanyNumber(secondLine.substring(secondLine.length() - 9, secondLine.length() - 5));

		/* stockNumber */
		//material.setStockNumber(secondLine.substring(secondLine.length() - 4, secondLine.length()));
		material.setIndexStore(secondLine.substring(secondLine.length() - 4, secondLine.length()));
		
		return material;
	}
}
