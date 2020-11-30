package com.example.saxParser.services;

import com.example.saxParser.models.ImageInfo;
import com.example.saxParser.models.Trademark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrademarkHandler extends DefaultHandler {

    private final Logger logger = LoggerFactory.getLogger(TrademarkHandler.class);

    Trademark trademark;
    List<ImageInfo> list;
    FileWriter myWriter;

    public TrademarkHandler() throws IOException {
        myWriter = new FileWriter("trademark.txt");
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
            writeTrademarkIntoTxtFile();
        }
    }

    private void writeTrademarkIntoTxtFile() {
        try {
            myWriter.append("\n****new trademark****")
                    .append("\nUI ")
                    .append(trademark.getUi());

            for (ImageInfo info : trademark.getImagesInfo()) {
                myWriter.append("\nURL ")
                        .append(info.getUrl())
                        .append("\nHASHCODE ")
                        .append(info.getHashcode());
            }
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}
