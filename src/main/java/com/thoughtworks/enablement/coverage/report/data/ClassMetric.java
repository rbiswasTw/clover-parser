package com.thoughtworks.enablement.coverage.report.data;

public class ClassMetric {
    private final String name;
    private final Metrics metrics;

    public ClassMetric(String name, Metrics metrics) {
        this.name = name;
        this.metrics = metrics;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public String getName() {
        return name;
    }

    public static class ClassMetricBuilder{
        private String name;
        private int lines;
        private int coveredLines;
        private int branches;
        private int coveredBranches;

        public void withName(String name){
            this.name = name;
        }

        public void incrementLines(boolean covered){
            ++this.lines;
            if (covered){
                ++this.coveredLines;
            }
        }

        public void incrementBranch(int branches, int coveredBranches){
            this.coveredBranches += coveredBranches;
            this.branches += branches;
        }

        public ClassMetric build(){
            return new ClassMetric(name, new Metrics(lines, coveredLines, branches, coveredBranches));
        }

    }
}
