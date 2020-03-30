package battleship.inner;

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

        var oceanState = game.getOceanState();

        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                assertEquals(oceanState[i][j], IBattleshipGame.CellState.EMPTY);
            }
        }

        int hitCount = 0;
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                var res = game.shootAt(i, j);
                var state = game.getOceanState();

                if (res == BattleshipGame.ShotResults.HIT) {
                    hitCount++;
                    assertEquals(hitCount, game.getHitCount());

                    assertTrue(state[i][j].equals(IBattleshipGame.CellState.DAMAGED) || state[i][j].equals(IBattleshipGame.CellState.SUNK));
                }
            }
        }
        assertTrue(game.isGameOver());
        assertEquals(game.getUntouchedShipsCount(), 0);
        assertEquals(game.getDamagedShipsCount(), 0);
        assertEquals(game.getShotsFired(), 100);
        assertEquals(game.getShipsSunk(), 10);
    }
}