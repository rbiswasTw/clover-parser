package com.thoughtworks.enablement.coverage.report.data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Project {

    private final Metrics metrics;
    private final List<CodePackage> codePackages;

    public Project(Metrics metrics, List<CodePackage> codePackages) {
        this.metrics = metrics;
        this.codePackages = codePackages;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public List<CodePackage> getCodePackages() {
        return codePackages;
    }


    public ProjectSummary generateReport(List<String> packageNamePrefixes, List<String> classNamespacePrefixes){

        List<ClassMetric> classMetrices = this.codePackages.stream()
                .map(CodePackage::getClassMetrics)
                .filter(Objects::nonNull)
                .flatMap((Function<List<ClassMetric>, Stream<ClassMetric>>) Collection::stream)
                .collect(Collectors.toList());

        List<SummaryReport> namespaceReport = classNamespacePrefixes
                .stream()
                .map(namespace -> {
                    final Metrics.MetricBuilder metricBuilder = new Metrics.MetricBuilder();
                    classMetrices.stream()
                            .filter(classMetric -> classMetric.getName().startsWith(namespace))
                            .forEach(classMetric -> {
                                metricBuilder.incrementStatements(classMetric.getMetrics().getStatements());
                                metricBuilder.incrementCoveredStatements(classMetric.getMetrics().getCoveredStatements());
                                metricBuilder.incrementBranches(classMetric.getMetrics().getConditionals());
                                metricBuilder.incrementCoveredBranches(classMetric.getMetrics().getCoveredConditionals());
                            });
                    Metrics metrics = metricBuilder.build();
                    return new SummaryReport(metrics.getStatements(), metrics.getCoveredStatements(), metrics.getConditionals(), metrics.getCoveredConditionals(), namespace);
                })
                .collect(Collectors.toList());

        List<SummaryReport> packageReport = packageNamePrefixes
                .stream()
                .map(packageName -> {
                    Metrics.MetricBuilder metricBuilder = new Metrics.MetricBuilder();
                    codePackages.stream()
                            .filter(codePackage -> codePackage.getName().startsWith(packageName))
                            .forEach(codePackage -> {
                                metricBuilder.incrementStatements(codePackage.getMetrics().getStatements());
                                metricBuilder.incrementCoveredStatements(codePackage.getMetrics().getCoveredStatements());
                                metricBuilder.incrementBranches(codePackage.getMetrics().getConditionals());
                                metricBuilder.incrementCoveredBranches(codePackage.getMetrics().getCoveredConditionals());
                            });
                    Metrics metrics = metricBuilder.build();
                    return new SummaryReport(metrics.getStatements(), metrics.getCoveredStatements(), metrics.getConditionals(), metrics.getCoveredConditionals(), packageName);
                })
                .collect(Collectors.toList());

        SummaryReport projectReport = new SummaryReport(this.metrics.getStatements(), this.metrics.getCoveredStatements(), this.metrics.getConditionals(), this.metrics.getCoveredConditionals(), "Repository overall");

        StringBuilder stringBuilder = new StringBuilder();
        namespaceReport.forEach(summaryReport -> stringBuilder.append(String.format("Namespace with prefix: %s:: Branch::%s Statements::%s \n",summaryReport.getTopic(), summaryReport.getBranchCoveragePercent(), summaryReport.getStatementCoveragePercent())));
        packageReport.forEach(summaryReport -> stringBuilder.append(String.format("Packages with prefix:%s:: Branch::%s Statements::%s \n",summaryReport.getTopic(), summaryReport.getBranchCoveragePercent(), summaryReport.getStatementCoveragePercent())));
        System.out.printf("Project coverage::Branch::%s Statements::%s\n%s%n", projectReport.getBranchCoveragePercent(), projectReport.getStatementCoveragePercent(), stringBuilder);



        return new ProjectSummary(projectReport, packageReport, namespaceReport);
    }


    public static class ProjectBuilder {
        private Metrics metrics;
        private final List<CodePackage> codePackages = new LinkedList<>();

        public void withMetrics(Metrics metrics){
            this.metrics = metrics;
        }

        public void withCodePackage(CodePackage codePackage){
            this.codePackages.add(codePackage);
        }

        public Project build(boolean deriveMetricsFromChildren){
            if (deriveMetricsFromChildren){
                Metrics.MetricBuilder metricBuilder = new Metrics.MetricBuilder();
                this.codePackages.forEach(codePackage -> {
                    metricBuilder.incrementStatements(codePackage.getMetrics().getStatements());
                    metricBuilder.incrementCoveredStatements(codePackage.getMetrics().getCoveredStatements());
                    metricBuilder.incrementBranches(codePackage.getMetrics().getConditionals());
                    metricBuilder.incrementCoveredBranches(codePackage.getMetrics().getCoveredConditionals());
                });
                this.metrics = metricBuilder.build();
            }
            return new Project(metrics, codePackages);
        }
    }
}
