package com.thoughtworks.enablement.coverage.report.process;

import com.thoughtworks.enablement.coverage.report.data.Coverage;
import com.thoughtworks.enablement.coverage.report.data.FileSummary;
import com.thoughtworks.enablement.coverage.report.formatter.ReportFormat;
import com.thoughtworks.enablement.coverage.report.parser.ParserDictionary;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {

    public void generateComprehensiveReport(String filesLocation) throws IOException {
        File file = new File(filesLocation);
        if (!file.exists()) {
            throw new RuntimeException("files location does not exist");
        }
        if (!file.isDirectory()) {
            throw new RuntimeException("location is not a directory");
        }
        File configurationFile = new File(String.format("%s/%s", filesLocation, "report.properties"));
        if (!configurationFile.exists()) {
            throw new RuntimeException("Patterns not found");
        }
        Properties configuration = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(configurationFile)) {
            configuration.load(fileInputStream);
        }
        @SuppressWarnings("unchecked") List<String> packagePatterns = configuration.containsKey("packagePrefixes") ? Arrays.stream(configuration.getProperty("packagePrefixes").split(",")).collect(Collectors.toList()) : Collections.EMPTY_LIST;
        @SuppressWarnings("unchecked") List<String> namespacePrefixes = configuration.containsKey("namespacePrefixes") ? Arrays.stream(configuration.getProperty("namespacePrefixes").split(",")).map(String::trim).collect(Collectors.toList()) : Collections.EMPTY_LIST;
        String coverageTool = configuration.getProperty("coverageTool");
        String outputFormat = configuration.getProperty("outputFormat","text");

        List<FileSummary> fileSummaries = Arrays.stream(Objects.requireNonNull(
                        file.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"))))
                .map(file1 -> {
                    Coverage coverage = ParserDictionary.valueOf(coverageTool).generateCoverageReport(file1.toPath());
                    return new FileSummary(file1.getName(),
                            coverage.getProject().generateReport(packagePatterns, namespacePrefixes));
                })
                .collect(Collectors.toList());

        List<String> projectSummaryReport = formatReport(fileSummaries, outputFormat);

        File outputFile = new File(String.format("%s/output.%s", filesLocation, ReportFormat.valueOf(outputFormat).getFileExtension()));
        if (outputFile.exists()) { //noinspection ResultOfMethodCallIgnored
            outputFile.delete();
        }
        //noinspection ResultOfMethodCallIgnored
        outputFile.createNewFile();
        try (FileWriter fileWriter = new FileWriter(outputFile); PrintWriter printWriter = new PrintWriter(fileWriter)) {
            projectSummaryReport.forEach(printWriter::println);
            printWriter.flush();
            fileWriter.flush();
        }
    }

    private List<String> formatReport(List<FileSummary> fileSummaries, String outputFormat){
        return ReportFormat.valueOf(outputFormat).create(fileSummaries);
    }

}
