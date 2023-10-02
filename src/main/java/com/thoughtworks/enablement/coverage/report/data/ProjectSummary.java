package com.thoughtworks.enablement.coverage.report.data;

import java.util.List;

public class ProjectSummary {

    private final SummaryReport projectMetrics;
    private final List<SummaryReport> pacakgeReports;

    public ProjectSummary(SummaryReport projectMetrics, List<SummaryReport> pacakgeReports) {
        this.projectMetrics = projectMetrics;
        this.pacakgeReports = pacakgeReports;
    }

    public List<SummaryReport> getPacakgeReports() {
        return pacakgeReports;
    }

    public SummaryReport getProjectMetrics() {
        return projectMetrics;
    }
}
