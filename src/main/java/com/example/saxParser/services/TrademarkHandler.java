package com.example.saxParser.services;

import com.example.saxParser.models.ImageInfo;
import com.example.saxParser.models.Trademark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@Service
public class TrademarkHandler extends DefaultHandler {

    private final Logger logger = LoggerFactory.getLogger(TrademarkHandler.class);

    Trademark trademark;
    List<ImageInfo> list;

    FileManager fileManager;
    ImageManager imageManager;


    @Autowired
    public TrademarkHandler(FileManager fileManager, ImageManager imageManager) {
        this.fileManager = fileManager;
        this.imageManager = imageManager;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "DS":
                trademark = new Trademark();
                trademark.setUi(attributes.getValue("UI"));
                break;
            case "Is":
                list = new ArrayList<>();
                break;
            case "I":
                list.add(new ImageInfo(attributes.getValue("U"), attributes.getValue("C")));
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("Is")) {
            trademark.setImagesInfo(list);
            logger.info(String.valueOf(trademark));
            fileManager.manageTrademark(trademark);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Callable<String> task = () -> imageManager.manageImageFromTrademark(trademark);
            Future<String> future = executorService.submit(task);
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.exit(1);
            }finally {
                executorService.shutdown();
            }
        }
    }
}