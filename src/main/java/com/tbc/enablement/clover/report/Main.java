package com.tbc.enablement.clover.report;

import com.tbc.enablement.clover.report.process.ReportGenerator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateComprehensiveReport(args[0]);
    }
}
