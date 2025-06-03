package core.basesyntax.report.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileWriterImplTest {
    private static final String TEST_OUTPUT_FILE = "src/test/resources/test_output.csv";
    private final Path outputPath = Path.of(TEST_OUTPUT_FILE);
    private FileWriterImpl fileWriter;

    @BeforeEach
    void setUp() {
        fileWriter = new FileWriterImpl();
    }

    @AfterEach
    void cleanUp() throws IOException {
        Files.deleteIfExists(outputPath);
    }

    @Test
    void write_validInput_writesFileCorrectly() throws IOException {
        String expected = "fruit,quantity\napple,10\nbanana,20";

        fileWriter.write(expected, TEST_OUTPUT_FILE);

        String actual = Files.readString(outputPath);
        assertEquals(expected, actual);
    }

    @Test
    void write_invalidPath_throwsRuntimeExceptionWithCause() {
        String invalidPath = "/invalid_path/test_output.csv";
        String content = "fruit,quantity\napple,10";

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                fileWriter.write(content, invalidPath)
        );

        assertEquals(FileNotFoundException.class, exception.getCause().getClass());
    }
}
