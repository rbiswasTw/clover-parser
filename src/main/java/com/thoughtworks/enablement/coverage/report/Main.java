package com.thoughtworks.enablement.coverage.report;

import com.thoughtworks.enablement.coverage.report.process.ReportGenerator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReportGenerator reportGenerator = new ReportGenerator();
        String workspaceDir = args.length > 0 ? args[0] : String.format("%s/%s", System.getProperty("user.dir"),"workspace");
        reportGenerator.generateComprehensiveReport(workspaceDir);
    }
}
