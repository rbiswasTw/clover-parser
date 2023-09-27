package com.tbc.enablement.clover.report.data;

public class FileSummary {
    private final String fileName;
    private final ProjectSummary projectSummary;

    public FileSummary(String fileName, ProjectSummary projectSummary) {
        this.fileName = fileName;
        this.projectSummary = projectSummary;
    }

    public ProjectSummary getProjectSummary() {
        return projectSummary;
    }

    public String getFileName() {
        return fileName;
    }
}
