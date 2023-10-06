package de.microtema.docascode.merge;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MergeTemplatesClient {

    public static void main(String[] args) {

        String docDir = args[0];
        String outputFileName = args[1];

        System.out.println("Merge templates from " + docDir + " to " + outputFileName);

        File inputDir = new File(docDir);

        File[] templateFiles = inputDir.listFiles((File dir, String name) -> name.endsWith(".md"));

        List<File> orderedTemplateFiles = Arrays.asList(Objects.requireNonNull(templateFiles));

        orderedTemplateFiles.sort(Comparator.comparing(o -> getFileIndex(o.getName())));

        StringBuilder stringBuilder = new StringBuilder();

        for (File templateFile : templateFiles) {

            String template = importTemplate(templateFile)
                    .replace("images/", inputDir.getName() + "/images/");

            stringBuilder.append(template).append(lineSeparator(2));
        }

        writeFile(outputFileName, stringBuilder.toString());
    }

    private static String importTemplate(File template) {

        try {
            return FileUtils.readFileToString(template, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeFile(String outputFilePath, String fileContent) {

        File outputFile = new File(outputFilePath);
        try {
            FileUtils.writeStringToFile(outputFile, fileContent, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String lineSeparator(int lines) {

        int index = 0;

        StringBuilder str = new StringBuilder();

        while (index++ < lines) {
            str.append(System.lineSeparator());
        }

        return str.toString();
    }

    private static String getFileIndex(String fileName) {

        return fileName.split("-")[0];
    }
}
