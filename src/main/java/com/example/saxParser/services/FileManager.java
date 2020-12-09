package com.example.saxParser.services;

import com.example.saxParser.InputParameters;
import com.example.saxParser.models.ImageInfo;
import com.example.saxParser.models.Trademark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class FileManager {

    private final Logger logger = LoggerFactory.getLogger(TrademarkHandler.class);
    public static int numOfTrademarksInFile;
    public static int fileNumber;

    public FileManager() {
        numOfTrademarksInFile = 0;
        fileNumber = 0;
    }

    public void manageTrademark(Trademark trademark) {

        if (numOfTrademarksInFile == 0) {
            try (FileWriter myWriter = new FileWriter(InputParameters.getOutputDir() + File.separator + "OO_" + fileNumber + ".txt", StandardCharsets.UTF_8)) {
                myWriter.write(" ");
                numOfTrademarksInFile++;
                writeTrademarkIntoTxtFile(trademark);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        } else if (numOfTrademarksInFile == InputParameters.getTrademarksPerFile() - 1) {
            numOfTrademarksInFile = 0;
            fileNumber++;
            writeTrademarkIntoTxtFile(trademark);
        } else {
            numOfTrademarksInFile++;
            writeTrademarkIntoTxtFile(trademark);
        }
    }

    private void writeTrademarkIntoTxtFile(Trademark trademark) {
        try (FileWriter myWriter = new FileWriter(InputParameters.getOutputDir() + File.separator + "OO_" + fileNumber + ".txt", StandardCharsets.UTF_8, true)) {
            myWriter.append(System.lineSeparator())
                    .append("****new trademark****")
                    .append(System.lineSeparator())
                    .append("UI ")
                    .append(trademark.getUi());

            for (ImageInfo info : trademark.getImagesInfo()) {
                myWriter.append(System.lineSeparator())
                        .append("URL ")
                        .append(info.getUrl())
                        .append(System.lineSeparator())
                        .append("HASHCODE ")
                        .append(info.getHashcode());
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
