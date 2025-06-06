package core.basesyntax.report.convertdata;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.model.FruitOperation;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataConvertorImplTest {
    private DataConvertorImpl dataConvertor;

    @BeforeEach
    void setUp() {
        dataConvertor = new DataConvertorImpl();
    }

    @Test
    void testConvertToTransactionValidData() {
        List<String> fruitInfoList = Arrays.asList(
                "operation,fruit,quantity",
                "b,apple,10",
                "s,banana,20",
                "p,orange,5"
        );

        List<FruitOperation> result = dataConvertor.convertToTransaction(fruitInfoList);

        assertEquals(3, result.size());

        assertEquals(new FruitOperation(FruitOperation.Operation.BALANCE, "apple", 10),
                result.get(0));
        assertEquals(new FruitOperation(FruitOperation.Operation.SUPPLY, "banana", 20),
                result.get(1));
        assertEquals(new FruitOperation(FruitOperation.Operation.PURCHASE, "orange", 5),
                result.get(2));
    }

    @Test
    void testConvertToTransactionWithInvalidOperation_shouldThrowException() {
        List<String> fruitInfoList = Arrays.asList(
                "operation,fruit,quantity",
                "x,banana,10"
        );

        assertThrows(IllegalArgumentException.class, () -> {
            dataConvertor.convertToTransaction(fruitInfoList);
        });
    }

    @Test
    void testConvertToTransactionWithEmptyFruitName_shouldSkipLine() {
        List<String> fruitInfoList = Arrays.asList(
                "operation,fruit,quantity",
                "b,,10"
        );

        List<FruitOperation> result = dataConvertor.convertToTransaction(fruitInfoList);

        assertEquals(0, result.size());
    }

    @Test
    void testConvertToTransactionWithInvalidQuantityFormat_shouldSetQuantityToZero() {
        List<String> fruitInfoList = Arrays.asList(
                "operation,fruit,quantity",
                "r,kiwi,abc"
        );

        List<FruitOperation> result = dataConvertor.convertToTransaction(fruitInfoList);

        assertEquals(1, result.size());
        assertEquals(FruitOperation.Operation.RETURN, result.get(0).getOperation());
        assertEquals("kiwi", result.get(0).getFruit());
        assertEquals(0, result.get(0).getQuantity());
    }

    @Test
    void testConvertToTransactionWithNegativeQuantity_shouldSkipLine() {
        List<String> fruitInfoList = Arrays.asList(
                "operation,fruit,quantity",
                "b,apple,-5"
        );

        List<FruitOperation> result = dataConvertor.convertToTransaction(fruitInfoList);

        assertEquals(0, result.size());
    }

    @Test
    void testConvertToTransactionWithWrongColumnCount_shouldSkipLine() {
        List<String> fruitInfoList = Arrays.asList(
                "operation,fruit,quantity",
                "b,apple",
                "s,banana,20",
                "p"
        );

        List<FruitOperation> result = dataConvertor.convertToTransaction(fruitInfoList);

        assertEquals(1, result.size());
        assertEquals(FruitOperation.Operation.SUPPLY, result.get(0).getOperation());
        assertEquals("banana", result.get(0).getFruit());
        assertEquals(20, result.get(0).getQuantity());
    }
}
