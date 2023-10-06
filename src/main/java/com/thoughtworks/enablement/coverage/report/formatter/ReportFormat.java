package com.thoughtworks.enablement.coverage.report.formatter;

import com.thoughtworks.enablement.coverage.report.data.FileSummary;

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
            throw new UnsupportedOperationException("Not yet implemented");
        }
    };

    ReportFormat(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public abstract List<String> create(List<FileSummary> fileSummaries);

    private final String fileExtension;

    public String getFileExtension() {
        return fileExtension;
    }
}
