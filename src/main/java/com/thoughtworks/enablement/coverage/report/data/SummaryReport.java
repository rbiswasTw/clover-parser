package com.thoughtworks.enablement.coverage.report.data;

public class SummaryReport {
    private final int statements;
    private final int coveredStatements;
    private final int conditions;
    private final int coveredConditions;
    private final String topic;

    public SummaryReport(int statements, int coveredStatements, int conditions, int coveredConditions, String topic) {
        this.statements = statements;
        this.coveredStatements = coveredStatements;
        this.conditions = conditions;
        this.coveredConditions = coveredConditions;
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public String getStatementCoveragePercent(){
        double coveragePercent = statements > 0 ? (double)(coveredStatements*100/statements) : 0d;
        return String.format("%.2f %%", coveragePercent);
    }

    public String getBranchCoveragePercent(){
        double coveragePercent = conditions > 0 ?  (double)(coveredConditions*100/conditions) : 0d;
        return String.format("%.2f %%", coveragePercent);
    }

    public String generateReport(){
        return String.format("topic#%s branch#%s statements#%s", topic, getBranchCoveragePercent(), getStatementCoveragePercent());
    }

}
