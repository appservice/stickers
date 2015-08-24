/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.luckyApp.stickers.persers;

import java.util.List;

import eu.luckyApp.stickers.model.Material;

/**
 *
 * @author Lukasz
 */
public interface Parser {
    public List<Material> parseData();
    
    
}
