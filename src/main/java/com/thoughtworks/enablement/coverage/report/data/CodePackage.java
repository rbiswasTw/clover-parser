package com.thoughtworks.enablement.coverage.report.data;

import java.util.LinkedList;
import java.util.List;

public class CodePackage {
    private final Metrics metrics;
    private final String name;
    private final List<ClassMetric> classMetrics;

    public CodePackage(Metrics metrics, String name, List<ClassMetric> classMetrics) {
        this.metrics = metrics;
        this.name = name;
        this.classMetrics = classMetrics;
    }

    public String getName() {
        return name;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public List<ClassMetric> getClassMetrics() {
        return classMetrics;
    }

    public static class CodePackageBuilder{
        private Metrics metrics;
        private String name;
        private final List<ClassMetric> classMetrics = new LinkedList<>();

        public void withMetrics(Metrics metrics) {
            this.metrics = metrics;
        }

        public void withName(String name) {
            this.name = name;
        }

        public void withClassMetric(ClassMetric classMetric) {
            this.classMetrics.add(classMetric);
        }

        public CodePackage build(boolean calculateMetricFromChildren) {
            if (calculateMetricFromChildren) {
                final Metrics.MetricBuilder metricBuilder = new Metrics.MetricBuilder();
                this.classMetrics.forEach(classMetric -> {
                    metricBuilder.incrementStatements(classMetric.getMetrics().getStatements());
                    metricBuilder.incrementCoveredStatements(classMetric.getMetrics().getCoveredStatements());
                    metricBuilder.incrementBranches(classMetric.getMetrics().getConditionals());
                    metricBuilder.incrementCoveredBranches(classMetric.getMetrics().getCoveredConditionals());
                });
                this.metrics = metricBuilder.build();
            }
            return new CodePackage(metrics, name, classMetrics);
        }
    }
}


