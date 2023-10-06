package com.thoughtworks.enablement.coverage.report.formatter;

import com.thoughtworks.enablement.coverage.report.data.FileSummary;
import com.thoughtworks.enablement.coverage.report.data.ProjectSummary;
import com.thoughtworks.enablement.coverage.report.data.SummaryReport;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

public class ReportFormatTest {

    @Test
    public void shouldGenerateTextReportFromFileSummary() throws IOException {
        List<FileSummary> fileSummaryList = generateFileSummaries();
        List<String> reportLines = ReportFormat.text.create(fileSummaryList);
        List<String> expectedLines = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("report-format/expectedTextFormat.txt")
        ) {
            assert inputStream != null;
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 Stream<String> lines = bufferedReader.lines()){
                lines.forEach(expectedLines::add);
            }
        }
        assertEquals(reportLines.size(), expectedLines.size());
        for (int i=0;i<reportLines.size();i++){
            assertEquals(reportLines.get(i),
                    expectedLines.get(i),
                    String.format("Line %d does not match", i));
        }
    }


    @Test
    public void shouldGenerateHtmlReportFromFileSummary() throws IOException {
        List<FileSummary> fileSummaryList = generateFileSummaries();
        List<String> reportLines = ReportFormat.html.create(fileSummaryList);
        List<String> expectedLines = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("report-format/expectedHtmlFormat.txt")
        ) {
            assert inputStream != null;
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                 Stream<String> lines = bufferedReader.lines()){
                lines.forEach(expectedLines::add);
            }
        }
        assertEquals(reportLines.size(), expectedLines.size());
        for (int i=0;i<reportLines.size();i++){
            assertEquals(reportLines.get(i),
                    expectedLines.get(i),
                    String.format("Line %d does not match", i));
        }
    }

    private List<FileSummary> generateFileSummaries() {
        List<FileSummary> fileSummaryList = Arrays.asList(
                new FileSummary("File1.txt", new ProjectSummary(
                        new SummaryReport(10, 10, 5, 10, "Project overall"),
                        Arrays.asList(new SummaryReport(1,2,1,2,"pkg-abc"),
                                new SummaryReport(1,2,1,2,"pkg-def")),
                        Arrays.asList(new SummaryReport(1,2,1,2,"nsp-abc"),
                                new SummaryReport(1,2,1,2,"nsp-def"))
                )),
                new FileSummary("File2.txt", new ProjectSummary(
                        new SummaryReport(10, 20, 5, 10, "Project overall"),
                        Arrays.asList(new SummaryReport(1,2,1,2,"pkg-abc"),
                                new SummaryReport(1,2,1,2,"pkg-def")),
                        Arrays.asList(new SummaryReport(1,2,1,2,"nsp-abc"),
                                new SummaryReport(1,2,1,2,"nsp-def"))
                ))
        );
        return fileSummaryList;
    }
}
