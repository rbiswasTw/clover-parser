package com.tbc.enablement.clover.report.parser;

import com.tbc.enablement.clover.report.data.CodePackage;
import com.tbc.enablement.clover.report.data.Coverage;
import com.tbc.enablement.clover.report.data.SummaryReport;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.*;


public class ReportParserTest {

    @Test
    public void shouldReadAndGenerateCoverageFromCloverXml() throws ParserConfigurationException, SAXException, IOException {
        ReportParser reportParser = new ReportParser();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(getClass().getClassLoader().getResourceAsStream("clover2.xml"), reportParser);
        Coverage coverage = reportParser.getCoverage();
        assertNotNull(coverage);
        assertNotNull(coverage.getProject());
        assertEquals(58, coverage.getProject().getCodePackages().size());
        Optional<CodePackage> rolesActionsPackage = coverage.getProject()
                .getCodePackages()
                .stream()
                .filter(codePackage -> codePackage.getName().equals("digital-user-management.src.app.features.user-management.roles.components.roles-actions"))
                .findFirst();
        assertTrue(rolesActionsPackage.isPresent());
        assertEquals(rolesActionsPackage.get().getMetrics().getStatements(), 77);
        assertEquals(rolesActionsPackage.get().getMetrics().getCoveredStatements(), 77);
        assertEquals(rolesActionsPackage.get().getMetrics().getConditionals(), 61);
        assertEquals(rolesActionsPackage.get().getMetrics().getCoveredConditionals(), 61);


        List<SummaryReport> summaryReportForRoles = coverage.getProject().generateReportForPackages(
                new ArrayList<>(List.of(
                        "digital-user-management.src.app.features.user-management.signing-rules.components.rules-list",
                        "document-confirmation.src.app")));

        assertEquals(summaryReportForRoles.get(0).getBranchCoveragePercent(),"0.00 %");
        assertEquals(summaryReportForRoles.get(0).getStatementCoveragePercent(), "96.00 %");

        assertEquals(summaryReportForRoles.get(1).getBranchCoveragePercent(), "64.00 %");
        assertEquals(summaryReportForRoles.get(1).getStatementCoveragePercent(), "77.00 %");
    }

    @Test
    public void generateForPresentDay() throws ParserConfigurationException, SAXException, IOException {
        //clover_Jul27.xml
        ReportParser reportParser = new ReportParser();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(getClass().getClassLoader().getResourceAsStream("clover_Jul27.xml"), reportParser);
        Coverage coverage = reportParser.getCoverage();

        List<SummaryReport> summaryReportForRoles = coverage.getProject().generateReportForPackages(
                new ArrayList<>(List.of(
                        "digital-user-management.src.app.features.user-management.roles",
                        "digital-user-management.src.app.features.user-management.signing-rules",
                        "document-confirmation.src.app"
                )));

        System.out.println(summaryReportForRoles.get(0).getBranchCoveragePercent());
        System.out.println(summaryReportForRoles.get(0).getStatementCoveragePercent());
    }



}