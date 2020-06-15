package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import java.util.*;

public class Game2048 extends Game {
    private static final int SIDE = 4;
    private int[][] gameField = new int[SIDE][SIDE];

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        createNewNumber();
        createNewNumber();

    }

    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(y, x, gameField[x][y]);
            }

        }

    }

    private void createNewNumber() {
        int x = 0;
        int y = 0;

        int cellValue = 1;

        while (cellValue != 0) {
            x = getRandomNumber(SIDE);
            y = getRandomNumber(SIDE);
            if (gameField[x][y] == 0) {
                cellValue = 0;
            }
        }

        if (getRandomNumber(10) == 9) {
            gameField[x][y] = 4;
        } else {
            gameField[x][y] = 2;
        }
        if (getMaxTileValue() == 2048) {
            win();
        }
    }

    private Color getColorByValue(int value) {
        Color color = null;
        switch (value) {
            case 0:
                color = Color.WHITE;
                break;
            case 2:
                color = Color.PINK;
                break;
            case 4:
                color = Color.PURPLE;
                break;
            case 8:
                color = Color.BLUE;
                break;
            case 16:
                color = Color.CYAN;
                break;
            case 32:
                color = Color.YELLOW;
                break;
            case 64:
                color = Color.GREEN;
                break;
            case 128:
                color = Color.ORANGE;
                break;
            case 256:
                color = Color.MAGENTA;
                break;
            case 512:
                color = Color.RED;
                break;
            case 1024:
                color = Color.FUCHSIA;
                break;
            case 2048:
                color = Color.GRAY;
                break;
        }

        return color;
    }

    private void setCellColoredNumber(int x, int y, int value) {

        if (value != 0) {
            setCellValueEx(x, y, getColorByValue(value), Integer.toString(value));
        } else {
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }

    private boolean compressRow(int[] row) {
        boolean isChanged = false;
        int temp = 0;
        int[] tempArr = row.clone();
        for (int i = 0; i < tempArr.length; i++) {
            for (int j = 0; j < tempArr.length - i - 1; j++) {
                if (tempArr[j] == 0) {
                    temp = tempArr[j];
                    tempArr[j] = tempArr[j + 1];
                    tempArr[j + 1] = temp;

                }
            }
        }
        for (int i = 0; i < row.length; i++) {
            if (tempArr[i] != row[i]) {
                row[i] = tempArr[i];
                isChanged = true;
            }
        }

        return isChanged;

    }

    private boolean mergeRow(int[] row) {
        boolean moved = false;
        for (int i = 0; i < row.length - 1; i++)
            if ((row[i] == row[i + 1]) && (row[i] != 0)) {
                row[i] = 2 * row[i];
                row[i + 1] = 0;
                moved = true;
                score += row[i];
                setScore(score);
            }
        return moved;
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped == true) {
            if (key == Key.SPACE) {
                setScore(0);
                restart();

            }
        } else if (canUserMove() == true) {
            if (key == Key.LEFT) {
                moveLeft();
                drawScene();
            } else if (key == Key.RIGHT) {
                moveRight();
                drawScene();
            } else if (key == Key.UP) {
                moveUp();
                drawScene();
            } else if (key == Key.DOWN) {
                moveDown();
                drawScene();
            }

        } else {
            gameOver();
        }

    }

    private void rotateClockwise() {
        int[][] rotateField = new int[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++) {
                rotateField[j][SIDE - 1 - i] = gameField[i][j];
            }
        for (int i = 0; i < SIDE; i++)
            for (int j = 0; j < SIDE; j++) {
                gameField[i][j] = rotateField[i][j];
            }
    }

    private void moveLeft() {
        boolean compress;
        boolean merge;
        int move = 0;
        for (int i = 0; i < SIDE; i++) {
            compress = compressRow(gameField[i]);
            merge = mergeRow(gameField[i]);
            compressRow(gameField[i]);
            if (merge || compress)
                move++;
        }
        if (move > 0)
            createNewNumber();
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();

    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private int getMaxTileValue() {
        int maxValue = 0;
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] > maxValue) {
                    maxValue = gameField[i][j];
                }
            }
        }

        return maxValue;
    }

    private boolean isGameStopped = false;

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "YOU WON", Color.BLACK, 75);
    }

    private boolean canUserMove() {
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0) {
                    return true;
                }
            }
        }

        for (int i = 0; i < SIDE - 1; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == gameField[i + 1][j]) {
                    return true;
                }
            }
        }

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE - 1; j++) {
                if (gameField[i][j] == gameField[i][j + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.RED, "YOU LOST", Color.BLACK, 75);
    }

    private int score = 0;

    private void restart() {
        isGameStopped = false;
        createGame();
        score = 0;
        drawScene();

    }

}