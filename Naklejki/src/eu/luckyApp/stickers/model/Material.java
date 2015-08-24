/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.luckyApp.stickers.model;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author mochelek
 */
public class Material implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7421203810088457471L;
	private String indexId;
    private String indexName;
    private String indexUnit;
    private String indexStore;
    private Double indexAmount;
    private String additionalDescribe;

    public Material() {
        this.indexId = "";
        this.indexName = "";
        this.indexUnit = "";
        this.indexStore = "";
        this.indexAmount = 0.0;
        this.additionalDescribe = "";
    }

    public Material(String indexId, String indexName, String indexUnit, String indexStore) {
        this.indexId = indexId;
        this.indexName = indexName.replace(";", ":");
        this.indexUnit = indexUnit;
        this.indexStore = indexStore;
        this.indexAmount = 0.0;
    }

    public Material(String indexId, String indexName, String indexUnit, String indexStore, Double indexAmount) {
        this.indexId =indexId;
        this.indexName = indexName.replace(";", ":");
        this.indexUnit = indexUnit;
        this.indexStore = indexStore;
        this.indexAmount = indexAmount;
    }

    public String getAdditionalDescibe() {
        return additionalDescribe;
    }

    public void setAdditionalDescibe(String additionalDescibe) {
        this.additionalDescribe = additionalDescibe;
    }

    public Double getIndexAmount() {
        return indexAmount;
    }

    public void setIndexAmount(Double indexAmount) {
        this.indexAmount = indexAmount;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getIndexName() {
        return indexName;
    }

  
    public void setIndexName(String indexName) {
        this.indexName = indexName.replace(";", ":");
    }

    public String getIndexStore() {
        return indexStore;
    }

    public void setIndexStore(String indexStore) {
        this.indexStore = indexStore;
    }

    public String getIndexUnit() {
        return indexUnit;
    }

    public void setIndexUnit(String indexUnit) {
        this.indexUnit = indexUnit;
    }

    /**
     *
     * @return splitted by "-" (when is 16 digits index as is used in CPGL)</br>
     * or by " " index (wnen is used on CPGL when starts CZ-AI...-18 chars)
     */
    public String getSplittedIndexId() {
        String index_id = this.getIndexId();
        switch (index_id.length()) {
            case 16:

                // String[] indexIdTab;
                //  indexIdTab = index_id.split(".{4}");
                return index_id.substring(0, 4) + "-" + index_id.substring(4, 8) + "-" + index_id.substring(8, 12) + "-" + index_id.substring(12, 16);//indexIdTab[1];

            case 18:
                if (index_id.substring(0, 4).equals("CZ-A")) {
                StringBuilder sb = new StringBuilder();
                sb.append(index_id.substring(0, 5));
                sb.append(' ');
                sb.append(index_id.substring(5, 10));
                sb.append(' ');
                sb.append(index_id.substring(10, 14));
                sb.append(' ');
                sb.append(index_id.substring(14, 18));
                return sb.toString();

            } else {
                break;
            }
            default:
                break;
        }
        //indexId.
        return index_id;
    }

    public String[] getPositionAccordingCPGLregule() {
        String[] table = new String[]{"", "", ""};

        String index_id = this.getIndexId();
        switch (index_id.length()) {
            case 16:
                if (index_id.substring(0, 6).equals("079989")) {
                table[0] = index_id.substring(6, 8);
                table[1] = index_id.substring(8, 11).replaceAll("^(00)|^0", "");
                table[2] = index_id.substring(11, 13);
                return table;
            }
                break;
            case 18:
                if (index_id.substring(0, 4).equalsIgnoreCase("CZ-A")) {
                table[0] = index_id.substring(10, 14);
                table[1] = index_id.substring(14, 18);
                table[2] = "";//
                return table;

            }
        }
        return table;
    }

    private String getIndexDataForQrcode() {
        return getIndexId() + ";" + getIndexName() + ";" + getIndexUnit() + ";" + getIndexStore();
        //String dane = indexZexcel + ";" + nazwa + ";" + jednostka + ";" + sklad;
    }

    /**
     *
     * @param size -size of Image (side of square)
     * @return image QRCode representation of index
     */
    @SuppressWarnings("unchecked")
	public BufferedImage returnQrCodeImage(int size) {
        String data = getIndexDataForQrcode();

        try {
            @SuppressWarnings("rawtypes")
			Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // get a byte matrix for the data
            // ByteMatrix matrix;
            com.google.zxing.qrcode.QRCodeWriter writer = new QRCodeWriter();
            //MatrixToImageWriter
            // matrix=writer.
            BitMatrix matrix = writer.encode(data,
                    com.google.zxing.BarcodeFormat.QR_CODE,
                    size, size, hints);//60 * ROZDZIEL  60 * ROZDZIEL

            // generate an image from the  matrix
            int width = matrix.getWidth();
            int height = matrix.getHeight();

            // create buffered image to draw to
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);

            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.BLACK);

            // iterate through the matrix and draw the pixels to the
            // image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(y, x)) {
                        graphics.fillRect(y, x, 1, 1);
                    }
                }
            }
            return image;
        } catch (WriterException ex) {
            Logger.getLogger(Material.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Index{" + "indexId=" + indexId + ", indexName=" + indexName + ", indexUnit=" + indexUnit + ", indexStore=" + indexStore + ", indexAmount=" + indexAmount + ", additionalDescibe=" + additionalDescribe + "}\n";
    }

}
