package com.example.saxParser.services;

import com.example.saxParser.InputParameters;
import com.example.saxParser.models.ImageInfo;
import com.example.saxParser.models.Trademark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageManager {

    @Value("${app.BaseURL}")
    String baseURL;
    @Value("${app.maxRetries}")
    Integer maxRetries;
    @Value("${app.maxFailReq}")
    Integer maxFailReq;

    private final Logger logger = LoggerFactory.getLogger(ImageManager.class);
    private static int failReqCounter = 0;
    int imageNumber;

    public String manageImageFromTrademark(Trademark trademark) {
        imageNumber = 0;
        for (ImageInfo imageInfo : trademark.getImagesInfo()) {
            String currentImgURL = baseURL + imageInfo.getUrl();
            String newImageName = InputParameters.getOutputImg()    //trademarkImages/0DA80A10-A44A-4EBE-8902-E8CAD757E7AE_0.png
                    + File.separator
                    + trademark.getUi()
                    + "_"
                    + imageNumber
                    + ".png";
            imageNumber++;

            Path path = Paths.get(newImageName);
            if (!Files.exists(path)) {
                downloadImage(currentImgURL, newImageName);
            }
        }
        return "";
    }

    private void downloadImage(String currentImgURL, String newImageName) {

        for (int failRetriesCounter = 0; failRetriesCounter <= maxRetries; failRetriesCounter++) {
            try {
                URL url = new URL(currentImgURL);

                try (InputStream in = new BufferedInputStream(url.openStream());
                     OutputStream out = new FileOutputStream(newImageName)) {
                    byte[] buff = new byte[1024];
                    int length;
                    while ((length = in.read(buff)) != -1) {
                        out.write(buff, 0, length);
                    }
                    logger.info("Image " + url.toString() + " has been downloaded");
                }

                break;

            } catch (Exception e) {
                if (failRetriesCounter < maxRetries) {
                    int sleepTime = 2;
                    logger.info("Failed to retrieve image " + currentImgURL + " (attempt " + (failRetriesCounter + 1) + "/"
                            + maxRetries + "), retrying in " + sleepTime + "s...");
                    try {
                        Thread.sleep(1000 * sleepTime);
                    } catch (InterruptedException e2) {
                        throw new RuntimeException("Interrupted");
                    }
                } else {
                    failReqCounter++;
                    logger.error("Failed to retrieve the image " + currentImgURL + " " + e);
                    if (failReqCounter >= maxFailReq) {
                        logger.error("maxFailedRequests reached ");
                        throw new RuntimeException("Internet connection problem");
                    }
                }
            }
        }
    }
}