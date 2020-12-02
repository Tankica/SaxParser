package com.example.saxParser.services;

import com.example.saxParser.InputParameters;
import com.example.saxParser.models.ImageInfo;
import com.example.saxParser.models.Trademark;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class FileManager {

    public static int numOfTrademarksInFile;
    public static int fileNumber;

    public FileManager() {
        numOfTrademarksInFile = 0;
        fileNumber = 0;
    }

    public void writeTrademarkIntoTxtFile(Trademark trademark) {

        if (numOfTrademarksInFile == 0) {
            try (FileWriter myWriter = new FileWriter(InputParameters.getOutputDir() + "/OO_" + fileNumber + ".txt", StandardCharsets.UTF_8)) {
                myWriter.write(" ");
                numOfTrademarksInFile++;
                writeTrademarkIntoFile(trademark);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } else if (numOfTrademarksInFile == InputParameters.getTrademarksPerFile() - 1) {
            numOfTrademarksInFile = 0;
            fileNumber++;
            writeTrademarkIntoFile(trademark);
        } else {
            numOfTrademarksInFile++;
            writeTrademarkIntoFile(trademark);
        }
    }

    private void writeTrademarkIntoFile(Trademark trademark) {
        try (FileWriter myWriter = new FileWriter(InputParameters.getOutputDir() + "/OO_" + fileNumber + ".txt", StandardCharsets.UTF_8, true)) {
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
            e.printStackTrace();
        }
    }
}
