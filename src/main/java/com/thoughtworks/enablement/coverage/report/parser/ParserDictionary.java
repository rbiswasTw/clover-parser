package com.thoughtworks.enablement.coverage.report.parser;

import com.thoughtworks.enablement.coverage.report.data.Coverage;
import com.thoughtworks.enablement.coverage.report.parser.clover.CloverParser;
import com.thoughtworks.enablement.coverage.report.parser.cobertura.CoberturaParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;

public enum ParserDictionary {

    Clover{
        @Override
        public Coverage generateCoverageReport(String fileName) {
            CloverParser cloverParser = new CloverParser();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            try (FileInputStream fileInputStream = new FileInputStream(fileName)){
                SAXParser saxParser;
                saxParser = saxParserFactory.newSAXParser();
                saxParser.parse(fileInputStream, cloverParser);
                return cloverParser.getCoverage();
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse XML", e);
            }
        }
    },
    Cobertura {
        @Override
        public Coverage generateCoverageReport(String fileName) {
            CoberturaParser coberturaParser = new CoberturaParser();
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            try (FileInputStream fileInputStream = new FileInputStream(fileName)){
                SAXParser saxParser;
                saxParser = saxParserFactory.newSAXParser();
                saxParser.parse(fileInputStream, coberturaParser);
                return coberturaParser.getCoverage();
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse XML", e);
            }
        }
    };

    public abstract Coverage generateCoverageReport(String fileName);
}
