package com.thoughtworks.enablement.coverage.report.parser.cobertura;

import com.thoughtworks.enablement.coverage.report.data.ClassMetric;
import com.thoughtworks.enablement.coverage.report.data.CodePackage;
import com.thoughtworks.enablement.coverage.report.data.Coverage;
import com.thoughtworks.enablement.coverage.report.data.Metrics;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CoberturaParserTest {

    @Test
    public void shouldParseCoberturaReport() throws IOException, SAXException, ParserConfigurationException {
        CoberturaParser coberturaParser = new CoberturaParser();
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        saxParser.parse(getClass().getClassLoader().getResourceAsStream("report-generator/cobertura/Cobertura.xml"), coberturaParser);
        Coverage coverage = coberturaParser.getCoverage();
        assertNotNull(coverage);
        assertEquals(coverage.getProject().getMetrics().getStatements(), 3833);
        assertEquals(coverage.getProject().getMetrics().getCoveredStatements(), 145);
        assertEquals(coverage.getProject().getMetrics().getConditionals(), 195);
        assertEquals(coverage.getProject().getMetrics().getCoveredConditionals(), 14);
        assertEquals(coverage.getProject().getCodePackages().size(), 5);
        MatcherAssert
                .assertThat(coverage
                        .getProject()
                        .getCodePackages()
                        .stream()
                        .filter(codePackage -> "XYZ.SomeApp.UserManagementModule.Application".equals(codePackage.getName()))
                        .findFirst().orElseThrow().getMetrics(), new BaseMatcher<>() {
                    @Override
                    public boolean matches(Object o) {
                        Metrics metrics = (Metrics) o;
                        assertEquals(metrics.getStatements(), 1304);
                        assertEquals(metrics.getCoveredStatements(), 53);
                        assertEquals(metrics.getConditionals(), 153);
                        assertEquals(metrics.getCoveredConditionals(), 0);
                        return true;
                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                });
        Metrics.MetricBuilder metricBuilder = new Metrics.MetricBuilder();
        coverage.getProject()
                .getCodePackages()
                .stream()
                .map(CodePackage::getClassMetrics)
                .flatMap((Function<List<ClassMetric>, Stream<ClassMetric>>) Collection::stream)
                .filter(classMetric -> classMetric.getName().startsWith("XYZ.SomeApp.UserManagementModule.Application.ExternalServices"))
                .map(ClassMetric::getMetrics)
                .forEach(metrics -> {
                    metricBuilder.incrementStatements(metrics.getStatements());
                    metricBuilder.incrementCoveredStatements(metrics.getCoveredStatements());
                    metricBuilder.incrementBranches(metrics.getConditionals());
                    metricBuilder.incrementCoveredBranches(metrics.getCoveredConditionals());
                });
        Metrics externalServicesNamespaceMetrics = metricBuilder.build();
        assertEquals(externalServicesNamespaceMetrics.getStatements(), 205);
        assertEquals(externalServicesNamespaceMetrics.getCoveredStatements(), 0);
        assertEquals(externalServicesNamespaceMetrics.getConditionals(), 4);
        assertEquals(externalServicesNamespaceMetrics.getCoveredConditionals(), 0);
    }


}