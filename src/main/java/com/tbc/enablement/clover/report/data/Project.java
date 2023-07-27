package com.tbc.enablement.clover.report.data;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Project {

    private final Metrics metrics;
    private final List<CodePackage> codePackages;

    public Project(Metrics metrics, List<CodePackage> codePackages) {
        this.metrics = metrics;
        this.codePackages = codePackages;
    }

    public List<CodePackage> getCodePackages() {
        return codePackages;
    }

    public List<SummaryReport> generateReportForPackages(List<String> packagePrefix){

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
        return summaryReports;

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

        public Project build(){
            return new Project(metrics, codePackages);
        }
    }
}
