package com.thoughtworks.enablement.coverage.report.process;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ReportGeneratorTest {

    private final String WORK_DIR = String.format("%s/%s", System.getProperty("user.dir"), "reportWorkArea");

    @BeforeMethod
    public void setUp() {
        cleanWorkDirectory();
        File workAreaDirectory = new File(WORK_DIR);
        workAreaDirectory.mkdir();
    }

    @Test
    public void shouldReadXmlFilesAndGenerateOutputForCloverCoverage() throws IOException {
        File workAreaDirectory = new File(WORK_DIR);
        List.of("clover1.Xml", "clover2.xml", "clover3.NotXml", "report.properties")
                .forEach(new Consumer<>() {
                    @Override
                    public void accept(String s) {
                        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(String.format("%s/%s/%s", "report-generator","clover", s))) {
                            assert resourceAsStream != null;
                            Files.copy(resourceAsStream, Path.of(workAreaDirectory.getPath(), s), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateComprehensiveReport(workAreaDirectory.getName());
        File outputFile = new File(String.format("%s/%s", WORK_DIR, "output.txt"));
        assertTrue(outputFile.exists());
        assertEquals(Files.readAllLines(outputFile.toPath()).size(), 18);
    }

    @Test
    public void shouldReadXmlFilesAndGenerateOutputForCoberturaCoverage() throws IOException {
        File workAreaDirectory = new File(WORK_DIR);
        List.of("Cobertura.xml","Cobertura-2.xml", "report.properties")
                .forEach(new Consumer<>() {
                    @Override
                    public void accept(String s) {
                        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(String.format("%s/%s/%s", "report-generator","cobertura", s))) {
                            assert resourceAsStream != null;
                            Files.copy(resourceAsStream, Path.of(workAreaDirectory.getPath(), s), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

        ReportGenerator reportGenerator = new ReportGenerator();
        reportGenerator.generateComprehensiveReport(workAreaDirectory.getName());
        File outputFile = new File(String.format("%s/%s", WORK_DIR, "output.txt"));
        assertTrue(outputFile.exists());
        assertEquals(Files.readAllLines(outputFile.toPath()).size(), 18);
    }

    @AfterClass
    public void cleanUp() {
        cleanWorkDirectory();
    }

    private void cleanWorkDirectory() {
        File file = new File(WORK_DIR);
        if (file.exists() && file.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(File::delete);
            file.delete();
        }
    }


}