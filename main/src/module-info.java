open module BattleshipGUI {
    requires javafx.graphics;
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;

    exports battleship.gui;
    exports battleship.guiservices;
    exports battleship.inner;
    exports battleship.network;
    exports battleship.network.dto;
}