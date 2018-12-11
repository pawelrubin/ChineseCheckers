package kitajce;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.paint.Color;
import layout.Board;
import layout.Field;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class MainController {
    @FXML public Board board;

    private static List<Field> fields = new ArrayList<>();

    private FXMLLoader loader;
    
    public void exitButton() {
        System.exit(0);
    }

    @FXML
    public void drawBoard() {
        double posX = 0;

        for (int i = 0; i < board.height; i++) {
            double posY = ((i * 40) * sqrt(3)/2 + 50);
            int offset = 0;
            for (int j = 0; j < board.widths[i]; j++) {
                if (j == 0) {
                    for (int k = 0; k < board.offset[i]; k++) {
                        offset = k * 40;
                    }
                }
                posX = j * 40 + 50;
                if ((i % 2) == 1) {
                    fields.add(new Field(posX + offset + 20, posY, 10, Color.GRAY));
                } else {
                    fields.add(new Field(posX + offset, posY, 10, Color.GRAY));
                }
                board.getChildren().addAll(fields.get(fields.size()-1));
            }
        }
    }
}
