open module BattleshipGUI {
    requires javafx.graphics;
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    exports battleship.gui;
    exports battleship.guiservices;
    exports battleship.inner;
    exports battleship.network;
    exports battleship.network.dto;
}