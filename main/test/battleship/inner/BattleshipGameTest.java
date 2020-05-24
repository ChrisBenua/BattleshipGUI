package battleship.inner;

import battleship.network.dto.ShotInfoDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleshipGameTest {
    @Test
    void test() {
        var game = new BattleshipGame(new HitAdapterFactory(), new PlacementAdapter(), new ShipFactory(),
                new HitAdapterCollection(), new EventsLogger());
        game.placeShipsRandomly();

        assertEquals(game.getShipsSunk(), 0);
        assertEquals(game.getDamagedShipsCount(), 0);
        assertEquals(game.getShotsFired(), 0);
        assertEquals(game.getHitCount(), 0);
        assertEquals(game.getUntouchedShipsCount(), 10);
        assertFalse(game.isGameOver());

        var oceanState = game.getPlayerOceanState();

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                assertEquals(oceanState[i][j], IBattleshipGame.CellState.EMPTY);
            }
        }

        int hitCount = 0;
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                game.processShotInfo(new ShotInfoDto(i, j));
                var state = game.getPlayerOceanState();
            }
        }
        assertTrue(game.isGameOver());
        assertEquals(game.getUntouchedShipsCount(), 0);
        assertEquals(game.getDamagedShipsCount(), 0);
        assertEquals(game.getShotsFired(), 100);
        assertEquals(game.getShipsSunk(), 10);
    }
}