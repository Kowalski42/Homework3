import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class JSONReader implements Reader {
    private ArrayList<Step> steps = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private String[][] gameField = {{"-", "-", "-"},
            {"-", "-", "-"},
            {"-", "-", "-"}};
    private String status = "Drawn game!";

    private Game read(String file) {
        ObjectMapper mapper = new ObjectMapper();
        Game result = null;
        try {
            result = mapper.readValue(new File(file), Game.class);
        } catch (Exception e) {
            ConsoleHelper.printMessage("The file with game steps wasn't found!" + System.lineSeparator(), true);
        }
        return result;
    }

    @Override
    public void readAndPlay(String file) {
        Game game = read(file);
        if (game != null) {
            players.addAll(Arrays.stream(game.getGameplay().getPlayers()).toList());
            players.add(game.getGameplay().getResult().getWinner());
            steps.addAll(game.getGameplay().getSteps().getSteps());
            String symbol = "-";
            int playerId;
            Step currentStep;
            int currentCell;
            if (players.size() >= 2) {
                System.out.println();
                System.out.printf("Player 1 -> %s as '%s'" +
                        System.lineSeparator(), players.get(0).getName(), players.get(0).getSymbol());
                System.out.printf("Player 2 -> %s as '%s'" +
                        System.lineSeparator(), players.get(1).getName(), players.get(1).getSymbol());
            } else {
                ConsoleHelper.printMessage("The file with game result doesn't include all players!");
            }
            for (int i = 0; i < steps.size(); i++) {
                currentStep = steps.get(i);
                playerId = currentStep.getPlayerId();
                if (playerId == players.get(0).getId()) {
                    symbol = players.get(0).getSymbol();
                }
                if (playerId == players.get(1).getId()) {
                    symbol = players.get(1).getSymbol();
                }
                currentCell = (currentStep.getCell() - 1);
                gameField[currentCell / 3][currentCell % 3] = symbol;
                formatAndPrint(800);
                System.out.println();
            }
            Player winner = players.get(2);
            if (winner != null) {
                System.out.printf("Player %d -> %s is winner as '%s'!\n", winner.getId(), winner.getName(), winner.getSymbol());
            } else {
                System.out.println(status);
            }
        }
    }

    private void formatAndPrint(int pause) {
        StringBuilder resultString;
        for (int i = 0; i < 3; i++) {
            resultString = new StringBuilder("|");
            for (int j = 0; j < 3; j++) {
                resultString.append(gameField[i][j] + "|");
            }
            System.out.println(resultString);
        }
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}