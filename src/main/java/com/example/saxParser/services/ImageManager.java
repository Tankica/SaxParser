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


    public String manageImageFromTrademark(Trademark trademark) throws MalformedURLException {
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
            if (Files.exists(path)) {
                return "";
            }
            final URL url = new URL(currentImgURL);
            if (netIsAvailable(url)) {
                saveImage(url, newImageName);
            } else {
                failReqCounter++;
                if (failReqCounter == maxFailReq) {
                    throw new RuntimeException("Internet connection error");
                }
            }
        }
        return "";
    }

    private void saveImage(URL url, String newImageName) {
        try (InputStream in = new BufferedInputStream(url.openStream());
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

    private boolean netIsAvailable(URL url) {
        int failRetriesCounter = 0;
        while (failRetriesCounter != maxRetries) {
            failRetriesCounter++;
            try {
                URLConnection connection = url.openConnection();
                connection.connect();
                return true;
            } catch (IOException ignored) {}
        }
        return false;
    }
}
