package com.example.saxParser;

    import com.example.saxParser.services.TrademarkHandler;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    import javax.xml.parsers.SAXParser;
    import javax.xml.parsers.SAXParserFactory;
    import java.io.InputStream;
    import java.util.Enumeration;
    import java.util.zip.ZipEntry;
    import java.util.zip.ZipFile;

@SpringBootApplication
public class SaxParserApplication {

    private static final Logger logger = LoggerFactory.getLogger(SaxParserApplication.class);
    private static TrademarkHandler trademarkHandler;

    @Autowired
    public SaxParserApplication(TrademarkHandler trademarkHandler) {
        SaxParserApplication.trademarkHandler = trademarkHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(SaxParserApplication.class, args);

        try {
//            ZipFile zip = new ZipFile("1.zip");
			ZipFile zip = new ZipFile(args[0]);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            ZipEntry entry = entries.nextElement();

            InputStream stream = zip.getInputStream(entry);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(stream, trademarkHandler);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}