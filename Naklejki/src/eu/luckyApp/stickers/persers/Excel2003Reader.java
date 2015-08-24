/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools |
 * Templates and open the template in the editor.
 */
package eu.luckyApp.stickers.persers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.luckyApp.stickers.model.Material;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author Lukasz
 */
public class Excel2003Reader implements AutoCloseable {
//	private File file;

	// private int sheetNumber;
	private Workbook workbook;

	private Sheet sheet;

	public Sheet getSheet() {
		return sheet;
	}

	public Excel2003Reader(File file, int sheetNumber) {
		try {
		//	this.file = file;
			this.workbook = Workbook.getWorkbook(file);
			this.sheet = workbook.getSheet(sheetNumber);
		} catch (IOException | BiffException ex) {
			Logger.getLogger(Excel2003Reader.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * @param additionalDescibeColumn if = -1 then additional columnw with describe isn't use, else it return number of column
	 * with addtidional describe
	 * @return list of index from list excel from row 2 up to next row with empty index data
	 */
	public List<Material> getMaterials(int additionalDescibeColumn) {
		List<Material> list = new ArrayList<>();
		Cell a, b, c, d, e, f;
		int i = 1; //read from second row
		int lastRow = sheet.getRows();
		while (!sheet.getCell(0, i).getContents().equalsIgnoreCase("")) {
			Material material = new Material();
			a = sheet.getCell(0, i);
			b = sheet.getCell(1, i);
			c = sheet.getCell(2, i);
			d = sheet.getCell(3, i);
			e = sheet.getCell(4, i);
			material.setIndexId(a.getContents());
			material.setIndexName(b.getContents());
			material.setIndexUnit(c.getContents());
			if (!d.getContents().isEmpty()) {
				material.setIndexAmount(Double.valueOf(d.getContents().replace(',', '.').replace(" ", "")));
			}
			material.setIndexStore(e.getContents());
			if (additionalDescibeColumn != -1) {
				f = sheet.getCell(additionalDescibeColumn, i);
				material.setAdditionalDescibe(f.getContents());
			}
			list.add(material);
			i++;
			if (i >= lastRow) //test for last row
				break;
		}
		//index.setIndexId(null);
		return list;
	}

	@Override
	public void close() {
		
		this.workbook.close();
	}

	private String[] getRowData(int columnsCount,int rowNumber) {
		String[] rowData = new String[columnsCount];
		for (int i = 0; i < columnsCount; i++) {
			rowData[i]=sheet.getCell(i, rowNumber).getContents();
		}
		return rowData;
	}

	public List<String[]> getRowsData(int columnsCount) {
		List<String[]> rowsData=new ArrayList<>();
		
		int rowNumber=0;
		int lastRow=sheet.getRows();
		while (!sheet.getCell(0, rowNumber).getContents().equalsIgnoreCase("")) {
			rowsData.add(getRowData(columnsCount,rowNumber));
			rowNumber++;
			if (rowNumber >= lastRow) //test for last row
				break;
		}
		
		
		return rowsData;
	}
}
