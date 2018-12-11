package kitajce;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import layout.Board;
import layout.Field;
import layout.Player;
import layout.Pone;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class MainController {
    @FXML public Board board;

    private static List<Player> players = new ArrayList<>();
    private static Field fieldsTab[][] = new Field[17][17];

    private FXMLLoader loader;

    @FXML
    public void drawBoard() {
        for (int i = 0; i < board.height; i++) {
            double posY = ((i * 40) * sqrt(3)/2 + 50);
            int offset = 0;
            for (int j = 0; j < board.widths[i]; j++) {
                if (j == 0) {
                    for (int k = 0; k < board.offsetDraw[i]; k++) {
                        offset = k * 40;
                    }
                }
                int posX = j * 40 + 50;
                if ((i % 2) == 1) {
                    fieldsTab[i][j + board.offset[i]] = new Field(posX + offset + 20, posY, 10, Color.GRAY);
                } else {
                    fieldsTab[i][j + board.offset[i]] = new Field(posX + offset, posY, 10, Color.GRAY);
                }
                board.getChildren().addAll(fieldsTab[i][j + board.offset[i]]);
            }
        }
        addPones("GREEN");
        addPones("BLUE");
        addPones("WHITE");
        addPones("RED");
        addPones("YELLOW");
        addPones("BLACK");
    }

    public void addPones(String color) {
        switch(color) {
            //top triangle
            case "GREEN": {
                Player greenPlayer = new Player();
                players.add(greenPlayer);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < board.widths[i]; j++) {
                        double x = fieldsTab[i][j + board.offset[i]].getCenterX();
                        double y = fieldsTab[i][j + board.offset[i]].getCenterY();
                        greenPlayer.pones[i][j + board.offset[i]] = new Pone(x, y, 9, Color.GREEN);
                        board.getChildren().addAll(greenPlayer.pones[i][j + board.offset[i]]);
                    }
                }
                break;
            }
            //top left triangle
            case "BLUE": {
                Player bluePlayer = new Player();
                players.add(bluePlayer);
                for (int i = 4; i < 8; i++) {
                    for (int j = 0; j < 8 - i; j++) {
                        double x = fieldsTab[i][j + board.offset[i]].getCenterX();
                        double y = fieldsTab[i][j + board.offset[i]].getCenterY();
                        bluePlayer.pones[i][j + board.offset[i]] = new Pone(x, y, 9, Color.BLUE);
                        board.getChildren().addAll(bluePlayer.pones[i][j + board.offset[i]]);
                    }
                }
                break;
            }
            //top right triangle
            case "WHITE": {
                Player whitePlayer = new Player();
                players.add(whitePlayer);
                for (int i = 4; i < 8; i++) {
                    for (int j = 9; j <= 16 - i; j++) {
                        double x = fieldsTab[i][j + board.offset[i]].getCenterX();
                        double y = fieldsTab[i][j + board.offset[i]].getCenterY();
                        whitePlayer.pones[i][j + board.offset[i]] = new Pone(x, y, 9, Color.WHITE);
                        board.getChildren().addAll(whitePlayer.pones[i][j + board.offset[i]]);
                    }
                }
                break;
            }
            //bottom right triangle
            case "RED": {
                Player redPlayer = new Player();
                players.add(redPlayer);
                for (int i = 9; i < 13; i++) {
                    for (int j = 9; j < board.widths[i]; j++) {
                        double x = fieldsTab[i][j + board.offset[i]].getCenterX();
                        double y = fieldsTab[i][j + board.offset[i]].getCenterY();
                        redPlayer.pones[i][j + board.offset[i]] = new Pone(x, y, 9, Color.RED);
                        board.getChildren().addAll(redPlayer.pones[i][j + board.offset[i]]);
                    }
                }
                break;
            }
            //bottom triangle
            case "YELLOW": {
                Player yellowPlayer = new Player();
                players.add(yellowPlayer);
                for (int i = 13; i < 17; i++) {
                    for (int j = 0; j < board.widths[i]; j++) {
                        double x = fieldsTab[i][j + board.offset[i]].getCenterX();
                        double y = fieldsTab[i][j + board.offset[i]].getCenterY();
                        yellowPlayer.pones[i][j + board.offset[i]] = new Pone(x, y, 9, Color.YELLOW);
                        board.getChildren().addAll(yellowPlayer.pones[i][j + board.offset[i]]);
                    }
                }
                break;
            }
            //bottom left triangle
            case "BLACK": {
                Player blackPlayer = new Player();
                players.add(blackPlayer);
                for (int i = 9; i < 13; i++) {
                    for (int j = 0; j < board.widths[i - 9]; j++) {
                        double x = fieldsTab[i][j + board.offset[i]].getCenterX();
                        double y = fieldsTab[i][j + board.offset[i]].getCenterY();
                        blackPlayer.pones[i][j + board.offset[i]] = new Pone(x, y, 9, Color.BLACK);
                        board.getChildren().addAll(blackPlayer.pones[i][j + board.offset[i]]);
                    }
                }
                break;
            }
        }
    }
}
