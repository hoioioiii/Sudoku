package sudoku.userInterface;

import java.io.IOException;

import sudoku.constants.GameState;
import sudoku.problemDomain.IStorage;
import sudoku.problemDomain.SudokuGame;
// import sudoku.userInterface.IUserInterfaceContract;
import sudoku.constants.Messeges;
import sudoku.computationalLogic.GameLogic;


/**
 * Whenever we're dealting with something like a controller, presenter or view model, strongly rec to communicate w the backend
 * through an interface.
 */
public class ControlLogic implements IUserInterfaceContract.EventListener{

    private IStorage storage;
    private IUserInterfaceContract.View view;

    public ControlLogic(IStorage storage, IUserInterfaceContract.View view) {
        this.storage = storage;
        this.view = view;
    }

    @Override
    public void onSudokuInput(int x, int y, int input) {
        try{

            SudokuGame gameData = storage.getGameData();
            int[][] newGridState = gameData.getCopyofGridState();
            newGridState[x][y] = input;

            gameData = new SudokuGame(GameLogic.checkForCompletion(newGridState),newGridState);
            storage.updateGameData(gameData);

            view.updateSquare(x,y,input);

            if (gameData.getGameState() == GameState.COMPLETE){
                view.showDialog(Messeges.GAME_COMPLETE);
            }


        }catch(IOException e){
            e.printStackTrace();
            view.showError(Messeges.ERROR);
        }
    }

    @Override
    public void onDialogClick() {
        try{
            storage.updateGameData(GameLogic.getNewGame());

            view.updateBoard(storage.getGameData());

        }catch(IOException e){

            view.showError(Messeges.ERROR);

        }
    }
    
}
