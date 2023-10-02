package com.thoughtworks.enablement.coverage.report.process;

import com.thoughtworks.enablement.coverage.report.data.FileSummary;
import com.thoughtworks.enablement.coverage.report.data.SummaryReport;
import com.thoughtworks.enablement.coverage.report.parser.clover.CloverParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportGenerator {

    public void generateComprehensiveReport(String filesLocation) throws IOException {
        File file = new File(filesLocation);
        if (!file.exists()) {
            throw new RuntimeException("files location does not exist");
        }
        if (!file.isDirectory()) {
            throw new RuntimeException("location is not a directory");
        }
        File patternFile = new File(String.format("%s/%s", filesLocation, "pattern.txt"));
        if (!patternFile.exists()) {
            throw new RuntimeException("Patterns not found");
        }
        List<String> patterns = Files.readAllLines(patternFile.toPath())
                .stream().filter(s -> !s.isEmpty()).map(String::trim).collect(Collectors.toList());
        List<String> projectSummaryReport = Arrays.stream(Objects.requireNonNull(
                        file.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"))))
                .map(file1 -> {
                    CloverParser cloverParser = new CloverParser();
                    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                    try (FileInputStream fileInputStream = new FileInputStream(file1)){
                        SAXParser saxParser;
                        saxParser = saxParserFactory.newSAXParser();
                        saxParser.parse(fileInputStream, cloverParser);
                        return new FileSummary(file1.getName(),
                                cloverParser.getCoverage().getProject().generateReportForPackages(patterns));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse XML", e);
                    }
                })
                .map(fileSummary -> {
                    List<String> reports = new LinkedList<>();
                    reports.add(String.format("------Report start for file::%s---", fileSummary.getFileName()));
                    reports.add(fileSummary.getProjectSummary().getProjectMetrics().generateReport());
                    reports.addAll(fileSummary.getProjectSummary().getPacakgeReports().stream().map(SummaryReport::generateReport).collect(Collectors.toList()));
                    return reports;
                })
                .flatMap((Function<List<String>, Stream<String>>) Collection::stream)
                .collect(Collectors.toList());

        File outputFile = new File(String.format("%s/%s", filesLocation, "output.txt"));
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

}
