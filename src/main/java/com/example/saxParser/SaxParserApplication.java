package com.example.saxParser;

import com.example.saxParser.services.TrademarkHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SpringBootApplication
public class SaxParserApplication implements CommandLineRunner {

    private static TrademarkHandler trademarkHandler;

    @Autowired
    public SaxParserApplication(TrademarkHandler trademarkHandler) {
        SaxParserApplication.trademarkHandler = trademarkHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(SaxParserApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        InputParameters.init(args);

        try (ZipFile zf = InputParameters.getInputDir()) {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            ZipEntry entry = entries.nextElement();
            try (InputStream stream = zf.getInputStream(entry)) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser saxParser = factory.newSAXParser();
                saxParser.parse(stream, trademarkHandler);
            }
        }
    }
}