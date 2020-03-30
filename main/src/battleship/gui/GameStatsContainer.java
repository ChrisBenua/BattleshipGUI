package battleship.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

/**
 * Container for displaying Game Stats
 */
public class GameStatsContainer extends GridPane {
    private int totalShots = 0;
    private int damagedShips = 0;
    private int untouchedShips = 0;
    private int sunkShips = 0;
    private Label totalShotsLabel;
    private Label damagedShipsLabel;
    private Label untouchedShipsLabel;
    private Label sunkShipsLabel;

    /**
     * Constructs new instance of GameStatsContainer and layout it
     */
    public GameStatsContainer() {
        totalShotsLabel = new Label();
        damagedShipsLabel = new Label();
        untouchedShipsLabel = new Label();
        sunkShipsLabel = new Label();

        this.setHgap(15);
        this.setVgap(10);

        var colConstraint = new ColumnConstraints();
        colConstraint.setHgrow(Priority.ALWAYS);
        var colConstraint2 = new ColumnConstraints();
        colConstraint2.setMinWidth(30);

        this.getColumnConstraints().addAll(colConstraint, colConstraint2);

        this.add(totalShotsLabel, 1, 0);
        this.add(damagedShipsLabel, 1, 1);
        this.add(untouchedShipsLabel, 1, 2);
        this.add(sunkShipsLabel, 1, 3);

        int i = 0;
        for (String el :
                new String[]{"Shots count:",
                        "Damaged ships count:",
                        "Undiscovered ships count:",
                        "Sunk ships count:"}) {
            var label = new Label();
            label.setTextAlignment(TextAlignment.LEFT);
            label.setText(el);
            this.add(label, 0, i++);
        }
        updateUI();
    }

    /**
     * Updates labels with new stats values;
     */
    private void updateUI() {
        this.totalShotsLabel.setText(String.valueOf(totalShots));
        this.sunkShipsLabel.setText(String.valueOf(sunkShips));
        this.untouchedShipsLabel.setText(String.valueOf(untouchedShips));
        this.damagedShipsLabel.setText(String.valueOf(damagedShips));
    }

    /**
     * Sets amount of totalShots done in game
     * @param totalShots totalShots made in game
     */
    public void setTotalShots(int totalShots) {
        this.totalShots = totalShots;
        updateUI();
    }

    /**
     * Sets amount of damaged ships in game
     * @param damagedShips damaged Ships
     */
    public void setDamagedShips(int damagedShips) {
        this.damagedShips = damagedShips;
        updateUI();
    }

    /**
     * Sets amount of undiscovered ships in game
     * @param untouchedShips untouched ships
     */
    public void setUntouchedShips(int untouchedShips) {
        this.untouchedShips = untouchedShips;
        updateUI();
    }

    /**
     * Sets amount of sunk ships in game
     * @param sunkShips sunk ships
     */
    public void setSunkShips(int sunkShips) {
        this.sunkShips = sunkShips;
        updateUI();
    }
}
