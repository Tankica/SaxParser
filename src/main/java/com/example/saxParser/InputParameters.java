package com.example.saxParser;

import gnu.getopt.Getopt;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

public class InputParameters {

    private static final String ARGUMENTS = "i:o:l:p:";

    private static ZipFile inputDir;
    private static File outputDir;
    private static int trademarksPerFile;
    private static File outputImg;


    /**
     * constructs a new InputParameters
     *
     * @param args String[]
     * @throws IllegalArgumentException an exception
     */
    static void init(String[] args) throws IllegalArgumentException, IOException {

        if (args.length != 4) {
            throw new IllegalArgumentException(usage("Wrong number of arguments given. Actual " + args.length + ". Expected: 4"));
        }
        Getopt g = new Getopt("Deduplication", args, ARGUMENTS);
        g.setOpterr(false);
        int c = g.getopt();
        while (c != -1) {
            switch (c) {
                case 'i':
                    inputDir = new ZipFile(g.getOptarg());
                    break;
                case 'o':
                    outputDir = new File(g.getOptarg());
                    createFileIfNotExists(outputDir);
                    break;
                case 'l':
                    trademarksPerFile = Integer.parseInt(g.getOptarg());
                    break;
                case 'p':
                    outputImg = new File(g.getOptarg());
                    createFileIfNotExists(outputImg);
                    break;

                default:
                    throw new IllegalArgumentException(usage("Illegal option (-" + (char) c + ") given :"));
            }
            c = g.getopt();
        }
    }

    private static void createFileIfNotExists(File file) {
        if (!file.exists()) {
            file.mkdir();
        } else {
            String[] entries = file.list();
            for (String s : entries) {
                File currentFile = new File(file.getPath(), s);
                currentFile.delete();
            }
        }
    }

    /**
     * Generates an explanation on how to use this conversion program
     *
     * @param message String - the message explaining what went wrong
     * @return String
     */
    private static String usage(String message) {
        return message + "\n\nUsage: <script> -i<inputDir> -o<outputDir> -l<trademarksPerFile>"
                + "\n    <inputDir>		    	    : input directory  [mandatory parameter]"
                + "\n    <outputDir>		    	: output directory [mandatory parameter]"
                + "\n    <trademarksPerFile>		: max number of trademarks per file [mandatory parameter]"
                + "\n    <outputImg>		        : output directory for images[mandatory parameter]";
    }

    public static ZipFile getInputDir() {
        return inputDir;
    }

    public static String getOutputDir() {
        return outputDir.getAbsolutePath();
    }

    public static String getOutputImg() {
        return outputImg.getAbsolutePath();
    }

    public static int getTrademarksPerFile() {
        return trademarksPerFile;
    }
}