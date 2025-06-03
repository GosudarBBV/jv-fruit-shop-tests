package core.basesyntax.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import core.basesyntax.dao.FruitOperationDao;
import core.basesyntax.model.FruitOperation;
import core.basesyntax.model.FruitOperation.Operation;
import core.basesyntax.strategy.OperationStrategy;
import core.basesyntax.strategy.operation.OperationHandler;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShopServiceImplTest {
    private FruitOperationDao fruitOperationDao;
    private OperationStrategy operationStrategy;
    private OperationHandler operationHandler;
    private ShopService shopService;

    @BeforeEach
    void setUp() {
        fruitOperationDao = mock(FruitOperationDao.class);
        operationStrategy = mock(OperationStrategy.class);
        operationHandler = mock(OperationHandler.class);
        shopService = new ShopServiceImpl(fruitOperationDao, operationStrategy);
    }

    @Test
    void changeQuantityStore_addsNewFruit_correctly() {
        FruitOperation input = new FruitOperation(Operation.BALANCE, "apple", 10);

        when(fruitOperationDao.get("apple")).thenReturn(Optional.empty());
        when(operationStrategy.get(Operation.BALANCE)).thenReturn(operationHandler);
        when(operationHandler.getQuantityFromStore(input, 10)).thenReturn(10);

        shopService.changeQuantityStore(List.of(input));

        verify(fruitOperationDao).add(input);
        verify(operationHandler).getQuantityFromStore(input, 10);
    }

    @Test
    void changeQuantityStore_updatesExistingFruit_correctly() {
        final FruitOperation existing = new FruitOperation(Operation.BALANCE,
                "banana", 15);
        final FruitOperation input = new FruitOperation(Operation.PURCHASE,
                "banana", 5);

        when(fruitOperationDao.get("banana")).thenReturn(Optional.of(existing));
        when(operationStrategy.get(Operation.PURCHASE)).thenReturn(operationHandler);
        when(operationHandler.getQuantityFromStore(existing, 5)).thenReturn(10); // 15 - 5 = 10

        shopService.changeQuantityStore(List.of(input));

        verify(fruitOperationDao).update(existing);
        verify(operationHandler).getQuantityFromStore(existing, 5);
    }

    @Test
    void changeQuantityStore_multipleFruits_allHandled() {
        final FruitOperation newFruitOperation = new FruitOperation(Operation.BALANCE, "apple", 10);
        final FruitOperation existingFruitOperation = new FruitOperation(Operation.PURCHASE,
                "banana", 5);

        when(fruitOperationDao.get("apple")).thenReturn(Optional.empty());
        when(fruitOperationDao.get("banana")).thenReturn(Optional.of(
                new FruitOperation(Operation.BALANCE, "banana", 20)));

        when(operationStrategy.get(Operation.BALANCE)).thenReturn(operationHandler);
        when(operationStrategy.get(Operation.PURCHASE)).thenReturn(operationHandler);

        when(operationHandler.getQuantityFromStore(any(), anyInt())).thenReturn(10).thenReturn(15);

        shopService.changeQuantityStore(List.of(newFruitOperation, existingFruitOperation));

        verify(fruitOperationDao).add(any(FruitOperation.class));
        verify(fruitOperationDao).update(any(FruitOperation.class));
    }
}
