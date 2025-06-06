package core.basesyntax.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitOperation;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FruitOperationDaoImplTest {
    private FruitOperationDao fruitOperationDao;

    @AfterEach
    void tearDown() {
        Storage.SHOP_STORE.clear();
    }

    @BeforeEach
    void setUp() {
        fruitOperationDao = new FruitOperationDaoImpl();
    }

    @Test
    void testAddAndGetFruitWithCorrectQuantity() {
        FruitOperation apple = new FruitOperation(FruitOperation.Operation.BALANCE, "apple", 10);
        fruitOperationDao.add(apple);

        Optional<FruitOperation> result = fruitOperationDao.get("apple");
        assertTrue(result.isPresent(), "Expected fruit 'apple' to be present in store");
        assertEquals(10, result.get().getQuantity(), "Expected quantity for 'apple' to be 10");
    }

    @Test
    void testAddNotNullFruit() {
        FruitOperation orange = new FruitOperation(FruitOperation.Operation.SUPPLY,"orange",15);
        fruitOperationDao.add(orange);

        Optional<FruitOperation> result = fruitOperationDao.get("orange");
        assertNotNull(result, "Expected Optional result for 'orange' not to be null");
    }

    @Test
    void testGetNonExistingFruit() {
        Optional<FruitOperation> result = fruitOperationDao.get("banana");
        assertFalse(result.isPresent(), "Expected fruit 'banana' to be absent in store");
    }

    @Test
    void testUpdateExistingFruit() {
        FruitOperation apple = new FruitOperation(FruitOperation.Operation.BALANCE,
                "apple", 10);
        fruitOperationDao.add(apple);

        FruitOperation updatedApple = new FruitOperation(FruitOperation.Operation.BALANCE,
                "apple", 20);
        fruitOperationDao.update(updatedApple);

        Optional<FruitOperation> result = fruitOperationDao.get("apple");
        assertTrue(result.isPresent(),
                "Expected fruit 'apple' to be present after update");
        assertEquals(20, result.get().getQuantity(),
                "Expected updated quantity for 'apple' to be 20");
    }

    @Test
    void testUpdateNonExistingFruit() {
        FruitOperation banana = new FruitOperation(FruitOperation.Operation.BALANCE,
                "banana", 15);
        fruitOperationDao.update(banana);

        Optional<FruitOperation> result = fruitOperationDao.get("banana");
        assertTrue(result.isPresent(), "Expected fruit 'banana' to be present after update");
        assertEquals(15, result.get().getQuantity(),
                "Expected quantity for 'banana' to be 15 after update");
    }
}
