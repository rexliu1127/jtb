package io.grx.common.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtils {
    private static Logger logger = LoggerFactory.getLogger(QRCodeUtils.class);

    private static final String DEFAULT_FILE_TYPE = "png";
    private static final int DEFAULT_SIZE = 600;

    /**
     * @param args
     * @throws WriterException
     * @throws IOException
     */
    public static void main(final String[] args) throws WriterException, IOException {
        final String qrCodeText = "https://www.journaldev.com";
        createQRImage(qrCodeText, new FileOutputStream("D:/jd.png"));
        System.out.println("DONE");
    }

    public static void createQRImage(final String qrCodeText, final OutputStream os) {
        createQRImage(qrCodeText, -1, null, os);
    }

    public static void createQRImage(final String qrCodeText, final int size,
                                     final String fileType, final OutputStream os) {
        try {
            // Create the ByteMatrix for the QR-Code that encodes the given String
            final Hashtable<EncodeHintType, Object> hintMap = new Hashtable<EncodeHintType, Object>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            final QRCodeWriter qrCodeWriter = new QRCodeWriter();

            int genWith = size > 0 ? size : DEFAULT_SIZE;
            String genFileType = StringUtils.defaultIfBlank(fileType, DEFAULT_FILE_TYPE);
            final BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
                    BarcodeFormat.QR_CODE, genWith, genWith, hintMap);
            // Make the BufferedImage that are to hold the QRCode
            final BufferedImage image = new BufferedImage(byteMatrix.getWidth(), byteMatrix.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            final Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, byteMatrix.getWidth(), byteMatrix.getHeight());
            // Paint and save the image using the ByteMatrix
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < byteMatrix.getWidth(); i++) {
                for (int j = 0; j < byteMatrix.getHeight(); j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, genFileType, os);
        } catch (Exception e) {
            logger.error("QRCodeUtils ERROR", e);
        }
    }
}
