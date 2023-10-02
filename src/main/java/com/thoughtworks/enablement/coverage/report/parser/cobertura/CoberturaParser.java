package com.thoughtworks.enablement.coverage.report.parser.cobertura;

import com.thoughtworks.enablement.coverage.report.data.ClassMetric;
import com.thoughtworks.enablement.coverage.report.data.CodePackage;
import com.thoughtworks.enablement.coverage.report.data.Coverage;
import com.thoughtworks.enablement.coverage.report.data.Project;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Stack;

public class CoberturaParser extends DefaultHandler {

    private final Stack<Tags> lastKnownTags = new Stack<>();
    private final Coverage.CoverageBuilder coverageBuilder = new Coverage.CoverageBuilder();
    private final Project.ProjectBuilder projectBuilder = new Project.ProjectBuilder();
    private CodePackage.CodePackageBuilder codePackageBuilder = new CodePackage.CodePackageBuilder();
    private ClassMetric.ClassMetricBuilder classMetricBuilder = new ClassMetric.ClassMetricBuilder();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "package":
                lastKnownTags.push(Tags.CodePackage);
                codePackageBuilder = new CodePackage.CodePackageBuilder();
                codePackageBuilder.withName(attributes.getValue("name"));
                break;
            case "line":
                if (lastKnownTags.peek()==Tags.CodeClass){
                    boolean lineCovered = Integer.parseInt(attributes.getValue("hits")) > 0;
                    classMetricBuilder.incrementLines(lineCovered);
                    if ("true".equals(attributes.getValue("branch"))){
                        String conditionCoverage = attributes.getValue("condition-coverage");
                        if (conditionCoverage!=null && !conditionCoverage.isEmpty()){
                            int[] branchCoverage = splitBranchCoverage(conditionCoverage);
                            classMetricBuilder.incrementBranch(branchCoverage[1], branchCoverage[0]);
                        }
                    }
                }
                break;
            case "class":
                lastKnownTags.push(Tags.CodeClass);
                classMetricBuilder = new ClassMetric.ClassMetricBuilder();
                classMetricBuilder.withName(attributes.getValue("name"));
                break;
            case "method":
                lastKnownTags.push(Tags.CodeMethod);
                break;

        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "coverage":
                coverageBuilder.withProject(projectBuilder.build(true));
                break;
            case "package":
                projectBuilder.withCodePackage(codePackageBuilder.build(true));
                lastKnownTags.pop();
                break;
            case "class":
                codePackageBuilder.withClassMetric(classMetricBuilder.build());
                lastKnownTags.pop();
                break;
            case "method":
                lastKnownTags.pop();
                break;
        }
    }

    private int[] splitBranchCoverage(String coverageStatement){
        int[] branchCoverage = new int[2];
        int parenthesesEnd = coverageStatement.lastIndexOf(")");
        int parenthesesStart = coverageStatement.lastIndexOf("(");
        if (parenthesesEnd > parenthesesStart){
            String count = coverageStatement.substring(parenthesesStart+1, parenthesesEnd).trim();
            String[] counts = count.split("/");
            if (counts.length == 2){
                branchCoverage[0] = Integer.parseInt(counts[0].trim());
                branchCoverage[1] = Integer.parseInt(counts[1].trim());
            }
        }
        return branchCoverage;
    }

    public Coverage getCoverage() {
        return coverageBuilder.build();
    }
}
