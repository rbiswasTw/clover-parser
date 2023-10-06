package com.thoughtworks.enablement.coverage.report.formatter;

import com.thoughtworks.enablement.coverage.report.data.FileSummary;
import com.thoughtworks.enablement.coverage.report.data.ProjectSummary;
import com.thoughtworks.enablement.coverage.report.data.SummaryReport;
import com.thoughtworks.enablement.coverage.report.formatter.html.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ReportFormat {
    text("txt") {
        @Override
        public List<String> create(List<FileSummary> fileSummaries) {
            return fileSummaries.stream().map(fileSummary -> {
                        List<String> reports = new LinkedList<>();
                        reports.add(String.format("------Report start for file::%s---", fileSummary.getFileName()));
                        reports.add(String.format("Repository coverage -> Branch coverage:%s Statement coverage:%s",
                                fileSummary.getProjectSummary().getProjectMetrics().getBranchCoveragePercent(),
                                fileSummary.getProjectSummary().getProjectMetrics().getStatementCoveragePercent()));
                        if (fileSummary.getProjectSummary().getPackageReports() != null && fileSummary.getProjectSummary().getPackageReports().size() > 0) {
                            reports.add("Coverage by packages");
                            reports.addAll(fileSummary.getProjectSummary().getPackageReports()
                                    .stream()
                                    .map(summaryReport -> String.format("%s Branch coverage:%s Statement coverage:%s",
                                            summaryReport.getTopic(), summaryReport.getBranchCoveragePercent(),
                                            summaryReport.getStatementCoveragePercent())).collect(Collectors.toList()));
                        }
                        if (fileSummary.getProjectSummary().getNamespaceReports() != null && fileSummary.getProjectSummary().getNamespaceReports().size() > 0) {
                            reports.add("Coverages by namespaces");
                            reports.addAll(fileSummary.getProjectSummary().getNamespaceReports()
                                    .stream()
                                    .map(summaryReport -> String.format("%s Branch coverage:%s Statement coverage:%s",
                                            summaryReport.getTopic(), summaryReport.getBranchCoveragePercent(),
                                            summaryReport.getStatementCoveragePercent())).collect(Collectors.toList()));
                        }
                        reports.add(String.format("------Report end for file::%s---", fileSummary.getFileName()));
                        return reports;
                    })
                    .flatMap((Function<List<String>, Stream<String>>) Collection::stream)
                    .collect(Collectors.toList());
        }
    },
    html("html") {
        @Override
        public List<String> create(List<FileSummary> fileSummaries) {

            BodyTag bodyTag = new BodyTag();
            bodyTag.withChild(new HeaderTag(2, "Coverage report !!!"));
            fileSummaries.stream().map(fileSummary -> {
                ProjectSummary projectSummary = fileSummary.getProjectSummary();
                TableTag tableTag = new TableTag();
                TableRowTag fileNameRow = new TableRowTag();
                fileNameRow.withAttribute(new Attribute("class","fileName"));
                TableCellTag fileNameCell = new TableCellTag(fileSummary.getFileName());
                fileNameCell.withAttribute(new Attribute("colspan","3"));
                fileNameRow.withCell(fileNameCell);
                tableTag.withRow(fileNameRow);
                TableRowTag genericHeader = new TableRowTag();
                genericHeader
                        .withCell(new TableCellTag("Area of intereset"))
                        .withCell(new TableCellTag("Branch coverage %"))
                        .withCell(new TableCellTag("Statement coverage %"));
                tableTag.withRow(genericHeader);
                tableTag.withRow(new TableRowTag().withCell(generateTopicHeader("Entire repository")));
                tableTag.withRow(createTableRowForProjectMetric(projectSummary.getProjectMetrics()));
                if (projectSummary.getNamespaceReports().size() > 0) {
                    tableTag.withRow(new TableRowTag().withCell(generateTopicHeader("By package or namespaces")));
                    projectSummary.getNamespaceReports().forEach(summaryReport -> tableTag.withRow(ReportFormat.createTableRowForProjectMetric(summaryReport)));
                }
                if (projectSummary.getPackageReports().size() > 0) {
                    tableTag.withRow(new TableRowTag().withCell(generateTopicHeader("By source code directory")));
                    projectSummary.getPackageReports().forEach(summaryReport -> tableTag.withRow(ReportFormat.createTableRowForProjectMetric(summaryReport)));
                }
                return tableTag;
            }).forEach(bodyTag::withChild);
            Html html = new Html(bodyTag, DEFAULT_STYLE);

            return List.of(html.render());
        }
    };

    private static TableCellTag generateTopicHeader(String cellContent) {
        TableCellTag topicHeader = new TableCellTag(cellContent);
        topicHeader.withAttribute(new Attribute("class", "topic"));
        topicHeader.withAttribute(new Attribute("colspan", "3"));
        return topicHeader;
    }

    private static TableRowTag createTableRowForProjectMetric(SummaryReport summaryReport) {
        TableRowTag summaryRow = new TableRowTag();
        summaryRow.withCell(new TableCellTag(summaryReport.getTopic()))
                .withCell(new TableCellTag(summaryReport.getBranchCoveragePercent()))
                .withCell(new TableCellTag(summaryReport.getStatementCoveragePercent()));
        return summaryRow;
    }

    ReportFormat(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public abstract List<String> create(List<FileSummary> fileSummaries);

    private final String fileExtension;

    public String getFileExtension() {
        return fileExtension;
    }

    private static final String DEFAULT_STYLE = "table {font-family: arial, sans-serif; border-collapse: collapse; width: 100%;}td, th {  border: 1px solid #dddddd;  text-align: left;  padding: 8px;}tr:nth-child(even) {  background-color: #dddddd;} .fileName{   background-color:yellow;  td{text-align: center; }} .topic{text-align: center;background-color:#d8f2ec;} ";
}
