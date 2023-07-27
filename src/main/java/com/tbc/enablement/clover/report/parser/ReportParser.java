package com.tbc.enablement.clover.report.parser;

import com.tbc.enablement.clover.report.data.CodePackage;
import com.tbc.enablement.clover.report.data.Coverage;
import com.tbc.enablement.clover.report.data.Metrics;
import com.tbc.enablement.clover.report.data.Project;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ReportParser extends DefaultHandler {

    private final Coverage.CoverageBuilder coverageBuilder = new Coverage.CoverageBuilder();
    private final Project.ProjectBuilder projectBuilder = new Project.ProjectBuilder();
    private CodePackage.CodePackageBuilder codePackageBuilder = new CodePackage.CodePackageBuilder();
    private Coverage coverage;
    private Tags lastTag = Tags.Coverage;


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        switch (qName) {
            case "project":
                lastTag = Tags.Project;
                break;
            case "metrics":
                Metrics metrics = buildMetrics(attributes.getValue("statements"), attributes.getValue("coveredstatements"),
                        attributes.getValue("conditionals"), attributes.getValue("coveredconditionals"));
                if (lastTag == Tags.Project) {
                    projectBuilder.withMetrics(metrics);
                } else if (lastTag == Tags.Package) {
                    codePackageBuilder.withMetrics(metrics);
                }
                break;
            case "package":
                lastTag = Tags.Package;
                codePackageBuilder = new CodePackage.CodePackageBuilder();
                codePackageBuilder.withName(attributes.getValue("name"));
                break;
            case "file":
                lastTag = Tags.File;
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case "coverage":
                this.coverage = coverageBuilder.build();
                break;
            case "project":
                coverageBuilder.withProject(projectBuilder.build());
                break;
            case "package":
                projectBuilder.withCodePackage(codePackageBuilder.build());
                break;
        }
    }

    private Metrics buildMetrics(String statements, String coveredStatements, String conditions, String coveredConditions){
        return new Metrics(Integer.parseInt(statements),
                Integer.parseInt(coveredStatements),
                Integer.parseInt(conditions),
                Integer.parseInt(coveredConditions));
    }

    public Coverage getCoverage() {
        return coverage;
    }
}
