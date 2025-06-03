package core.basesyntax.report.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileReaderImplTest {
    private static final String VALID_FILE = "src/test/resources/test_data.csv";
    private static final String EMPTY_FILE = "src/test/resources/empty_file.csv";
    private static final String INVALID_FILE = "src/test/resources/non_existing_file.csv";
    private FileReaderImpl fileReader;

    @BeforeEach
    void setUp() {
        fileReader = new FileReaderImpl();
    }

    @Test
    void readTestFile_ok() {
        List<String> actual = fileReader.read(VALID_FILE);

        List<String> expected = Arrays.asList(
                "operation,fruit,quantity",
                "b,banana,20",
                "b,apple,35",
                "r,apple,10",
                "p,apple,24",
                "p,banana,5",
                "s,banana,50",
                "s,banana,70",
                "p,banana,13",
                "p,apple,22",
                "p,banana,15",
                "s,banana,50"
        );

        assertEquals(expected, actual);
    }

    @Test
    void readEmptyFile_returnsEmptyList() {
        List<String> actual = fileReader.read(EMPTY_FILE);
        assertTrue(actual.isEmpty(), "Expected an empty list when reading an empty file");
    }

    @Test
    void readInvalidFile_throwsRuntimeException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            fileReader.read(INVALID_FILE);
        });

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof java.io.FileNotFoundException);
    }
}
