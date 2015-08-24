/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.luckyApp.stickers.persers;

import java.io.File;

import java.util.List;

import eu.luckyApp.stickers.model.Material;

/**
 *
 * @author Lukasz
 */
public class Excel2003Parser implements Parser {
    private File file;
    private static final int SHEET_NUMBER=0;
    

    public Excel2003Parser(File file){
        this.file=file;
        
    }
    public void setFile(File file) {
        this.file = file;
    }
    
    

    @Override
    public List<Material> parseData() {
      //  if(file.isFile()&&file.canRead()){
       Excel2003Reader er=new Excel2003Reader(file, SHEET_NUMBER);
       List<Material>materials=er.getMaterials(SHEET_NUMBER);
       er.close();
       return materials;
        
      //  }
     
        
      //  return null;
    }
    
}
