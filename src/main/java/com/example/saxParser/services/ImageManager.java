package com.example.saxParser.services;

import com.example.saxParser.InputParameters;
import com.example.saxParser.models.ImageInfo;
import com.example.saxParser.models.Trademark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;

@Service
public class ImageManager {

    private final Logger logger = LoggerFactory.getLogger(TrademarkHandler.class);
    private static final String BASE_URL = "https://image.ipsensus.com//";
    int imageNumber;

    public void manageImageFromTrademark(Trademark trademark) {
        imageNumber = 0;
        for (ImageInfo imageInfo : trademark.getImagesInfo()) {
            String currentImgURL = BASE_URL + imageInfo.getUrl();
            String newImageName = InputParameters.getOutputImg()    //trademarkImages/0DA80A10-A44A-4EBE-8902-E8CAD757E7AE_0.png
                    + File.separator
                    + trademark.getUi()
                    + "_"
                    + imageNumber
                    + ".png";
            imageNumber++;

            try (InputStream in = new BufferedInputStream(new URL(currentImgURL).openStream());
                 OutputStream out = new FileOutputStream(newImageName)) {
                byte[] buff = new byte[1024];
                int length;
                while ((length = in.read(buff)) != -1) {
                    out.write(buff, 0, length);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }

}
