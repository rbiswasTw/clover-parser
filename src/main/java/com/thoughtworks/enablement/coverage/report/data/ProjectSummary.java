package com.thoughtworks.enablement.coverage.report.data;

import java.util.List;

public class ProjectSummary {

    private final SummaryReport projectMetrics;
    private final List<SummaryReport> packageReports;
    private final List<SummaryReport> namespaceReports;

    public ProjectSummary(SummaryReport projectMetrics, List<SummaryReport> packageReports, List<SummaryReport> namespaceReports) {
        this.projectMetrics = projectMetrics;
        this.packageReports = packageReports;
        this.namespaceReports = namespaceReports;
    }

    public List<SummaryReport> getPackageReports() {
        return packageReports;
    }

    public SummaryReport getProjectMetrics() {
        return projectMetrics;
    }

}
