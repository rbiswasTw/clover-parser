package com.tbc.enablement.clover.report.process;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
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

    @BeforeTest
    public void setUp() {
        File workAreaDirectory = new File(WORK_DIR);
        if (workAreaDirectory.exists()) {
            workAreaDirectory.delete();
        }
        workAreaDirectory.mkdir();
    }

    @Test
    public void shouldReadXmlFilesAndGenerateOutput() throws IOException {
        File workAreaDirectory = new File(WORK_DIR);
        List.of("clover1.Xml", "clover2.xml", "clover3.NotXml", "pattern.txt")
                .forEach(new Consumer<>() {
                    @Override
                    public void accept(String s) {
                        try (InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(String.format("%s/%s", "report-generator", s))) {
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
        assertEquals(Files.readAllLines(outputFile.toPath()).size(), 14);
    }

    @AfterClass
    public void cleanUp() {
        File file = new File(WORK_DIR);
        if (file.exists() && file.isDirectory()) {
            Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(File::delete);
            file.delete();
        }
    }


}