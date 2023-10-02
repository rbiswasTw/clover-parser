package com.thoughtworks.enablement.coverage.report.data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
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

    public ProjectSummary generateReportForPackages(List<String> packagePrefix){

        List<SummaryReport> summaryReports = packagePrefix.stream().map(prefix -> {
            List<Metrics> metrices1 = codePackages
                    .stream()
                    .filter(codePackage -> codePackage.getName().startsWith(prefix)).map(CodePackage::getMetrics)
                    .collect(Collectors.toList());
            int statements1 = metrices1.stream().mapToInt(Metrics::getStatements).sum();
            int coveredStatements1 = metrices1.stream().mapToInt(Metrics::getCoveredStatements).sum();
            int conditions1 = metrices1.stream().mapToInt(Metrics::getConditionals).sum();
            int coveredConditions1 = metrices1.stream().mapToInt(Metrics::getCoveredConditionals).sum();
            return new SummaryReport(statements1, coveredStatements1, conditions1, coveredConditions1, prefix);
        }).collect(Collectors.toList());

        SummaryReport projectReport = new SummaryReport(this.metrics.getStatements(), this.metrics.getCoveredStatements(), this.metrics.getConditionals(), this.metrics.getCoveredConditionals(), "Project overall");

        StringBuilder stringBuilder = new StringBuilder();
        summaryReports.forEach(summaryReport -> stringBuilder.append(String.format("Package::%s:: Branch::%s Statements::%s \n",summaryReport.getTopic(), summaryReport.getBranchCoveragePercent(), summaryReport.getStatementCoveragePercent())));
        System.out.printf("Project coverage::Branch::%s Statements::%s\n%s%n", projectReport.getBranchCoveragePercent(), projectReport.getStatementCoveragePercent(), stringBuilder);

        return new ProjectSummary(projectReport, summaryReports);

    }

    public ProjectSummary generateReportForNamespaces(List<String> classNamespacePrefix){
        final Metrics.MetricBuilder metricBuilder = new Metrics.MetricBuilder();
        List<SummaryReport> summaryReports = classNamespacePrefix.stream().map(prefix -> {
            codePackages
                    .stream()
                    .map(CodePackage::getClassMetrics)
                    .filter(Objects::nonNull)
                    .flatMap((Function<List<ClassMetric>, Stream<ClassMetric>>) Collection::stream)
                    .forEach(classMetric -> {
                        metricBuilder.incrementStatements(classMetric.getMetrics().getStatements());
                        metricBuilder.incrementCoveredStatements(classMetric.getMetrics().getCoveredStatements());
                        metricBuilder.incrementBranches(classMetric.getMetrics().getConditionals());
                        metricBuilder.incrementCoveredBranches(classMetric.getMetrics().getCoveredConditionals());
                    });
            Metrics metrics = metricBuilder.build();
            return new SummaryReport(metrics.getStatements(), metrics.getCoveredStatements(), metrics.getConditionals(), metrics.getCoveredConditionals(), prefix);
        }).collect(Collectors.toList());

        SummaryReport projectReport = new SummaryReport(this.metrics.getStatements(), this.metrics.getCoveredStatements(), this.metrics.getConditionals(), this.metrics.getCoveredConditionals(), "Project overall");

        StringBuilder stringBuilder = new StringBuilder();
        summaryReports.forEach(summaryReport -> stringBuilder.append(String.format("Package::%s:: Branch::%s Statements::%s \n",summaryReport.getTopic(), summaryReport.getBranchCoveragePercent(), summaryReport.getStatementCoveragePercent())));
        System.out.printf("Project coverage::Branch::%s Statements::%s\n%s%n", projectReport.getBranchCoveragePercent(), projectReport.getStatementCoveragePercent(), stringBuilder);

        return new ProjectSummary(projectReport, summaryReports);

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