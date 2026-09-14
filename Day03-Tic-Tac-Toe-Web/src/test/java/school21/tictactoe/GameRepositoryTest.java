package school21.tictactoe;

import org.junit.jupiter.api.Test;
import school21.tictactoe.datasource.mapper.DatasourceMapperImpl;
import school21.tictactoe.datasource.repository.GameRepository;
import school21.tictactoe.datasource.repository.GameRepositoryImpl;
import school21.tictactoe.datasource.repository.GameStorage;
import school21.tictactoe.domain.model.Board;
import school21.tictactoe.domain.model.Game;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class GameRepositoryTest {

    @Test
    public void testSaveAndRetrieveGame() {
        GameStorage storage = new GameStorage();
        DatasourceMapperImpl mapper = new DatasourceMapperImpl();
        GameRepository repository = new GameRepositoryImpl(storage, mapper);

        UUID id = UUID.randomUUID();
        int[][] matrix = {
                {1, 0, 2},
                {0, 1, 0},
                {0, 0, 2}
        };
        Game domainGame = new Game(id, new Board(matrix));
        repository.save(domainGame);

        Game retrieved = repository.getDomain(id);
        assertNotNull(retrieved);
        assertEquals(id, retrieved.getId());
        assertArrayEquals(matrix, retrieved.getBoard().getBoard());
    }

    @Test
    public void testConcurrentStorageAccess() throws InterruptedException {
        GameStorage storage = new GameStorage();
        DatasourceMapperImpl mapper = new DatasourceMapperImpl();
        GameRepository repository = new GameRepositoryImpl(storage, mapper);

        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    UUID id = UUID.randomUUID();
                    int[][] m = new int[3][3];
                    m[0][0] = (index % 2) + 1;
                    Game g = new Game(id, new Board(m));
                    repository.save(g);
                    Game fetched = repository.getDomain(id);
                    assertNotNull(fetched);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(threadCount, storage.size());
    }
}
