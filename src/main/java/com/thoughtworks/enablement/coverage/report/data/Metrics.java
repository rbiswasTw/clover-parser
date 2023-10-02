package com.thoughtworks.enablement.coverage.report.data;

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

    public static class MetricBuilder{
        private int statements = 0;
        private int coveredStatements = 0;
        private int branches = 0;
        private int coveredBranches = 0;

        public void incrementStatements(int increment){
            this.statements += increment;
        }

        public void incrementCoveredStatements(int increment){
            this.coveredStatements += increment;
        }

        public void incrementBranches(int increment){
            this.branches += increment;
        }

        public void incrementCoveredBranches(int increment){
            this.coveredBranches += increment;
        }

        public Metrics build(){
            return new Metrics(statements, coveredStatements, branches, coveredBranches);
        }

    }
}
