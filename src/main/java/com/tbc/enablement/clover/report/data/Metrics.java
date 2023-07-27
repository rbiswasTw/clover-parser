package com.tbc.enablement.clover.report.data;

public class Metrics {

    private final int statements;
    private final int coveredStatements;
    private final int conditionals;
    private final int coveredConditionals;


    public Metrics(int statements, int coveredStatements, int conditionals, int coveredConditionals) {
        this.statements = statements;
        this.coveredStatements = coveredStatements;
        this.conditionals = conditionals;
        this.coveredConditionals = coveredConditionals;
    }

    public int getStatements() {
        return statements;
    }

    public int getCoveredStatements() {
        return coveredStatements;
    }

    public int getConditionals() {
        return conditionals;
    }

    public int getCoveredConditionals() {
        return coveredConditionals;
    }
}
