package com.example.luciodery.tictactoe;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.*;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Button> buttonList = new ArrayList<Button>();
    private static final int NUM_ROWS = 3;
    private static final int HUMAN_PLAYER_NUM = 1;
    private static final int COMP_PLAYER_NUM = 2;
    boolean thereIsWinner = false;
    private int[][] grid = new int[NUM_ROWS][NUM_ROWS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initbuttnList();
        Toast.makeText(this, "As a courtesy, you start!", Toast.LENGTH_SHORT).show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void putMark(View view) {
        if(thereIsWinner)return;
        if(!positionAvailable())return;
        Button this_button = (Button)view;
        if(this_button.getText().equals("X"))return;
        this_button.setText("X");
        int index = buttonList.indexOf(this_button);
        grid[index/NUM_ROWS][index%NUM_ROWS] = HUMAN_PLAYER_NUM;
        try{
            Thread.sleep(100);
        }catch (InterruptedException ex){
            Thread.currentThread().interrupt();
        }
        if(!checkWinner()){
            computerPlay();
            checkWinner();
        }
    }

    boolean positionAvailable(){
        for(int i = 0; i < NUM_ROWS*NUM_ROWS; i++){
            if(grid[i/NUM_ROWS][i%NUM_ROWS] == 0) return true;
        }
        return false;
    }

    private boolean checkWinner(){
        if(boolHasWon(HUMAN_PLAYER_NUM)){
            Toast.makeText(this, "You WON, You freaking Genius!!", Toast.LENGTH_SHORT).show();
            thereIsWinner = true;
            return  true;
        }else if(boolHasWon(COMP_PLAYER_NUM)){
            Toast.makeText(this, "You LOST, HAHAHA", Toast.LENGTH_SHORT).show();
            thereIsWinner = true;
            return  true;
        }else{
            if(!positionAvailable()){
                Toast.makeText(this, "A tie :/ !", Toast.LENGTH_SHORT).show();
                thereIsWinner = true;
                return true;
            }
        }
        return false;
    }


    private void computerPlay() {
        int bestPosition = getBestPosition(0);
        if(bestPosition == -1) chooseRandomPosition();
        choosePosition(bestPosition);
    }

    private int getBestPosition(int start){
        int singlePos = checkSinglePosition();
        if(singlePos != -1) return singlePos;
        for(int i = start; i < NUM_ROWS*NUM_ROWS; i++){
            if(grid[i/NUM_ROWS][i%NUM_ROWS] == 0){
                grid[i/NUM_ROWS][i%NUM_ROWS] = HUMAN_PLAYER_NUM;
                if(boolHasWon(HUMAN_PLAYER_NUM)){
                    grid[i/NUM_ROWS][i%NUM_ROWS] = 0;
                    return i;
                }else{
                    if(getBestPosition(i + 1) != -1){
                        grid[i/NUM_ROWS][i%NUM_ROWS] = 0;
                        return i;
                    }
                    if(!positionAvailable()){
                        grid[i/NUM_ROWS][i%NUM_ROWS] = 0;
                        break;
                    }
                    grid[i/NUM_ROWS][i%NUM_ROWS] = 0;
                }
            }
        }
        return -1;
    }

    private int checkSinglePosition(){
        for(int i = 0; i < NUM_ROWS*NUM_ROWS; i++) {
            if (grid[i / NUM_ROWS][i % NUM_ROWS] == 0) {
                grid[i/NUM_ROWS][i%NUM_ROWS] = COMP_PLAYER_NUM;
                if(boolHasWon(COMP_PLAYER_NUM)) {
                    grid[i / NUM_ROWS][i % NUM_ROWS] = 0;
                    return i;
                }
                grid[i/NUM_ROWS][i%NUM_ROWS] = HUMAN_PLAYER_NUM;
                if(boolHasWon(HUMAN_PLAYER_NUM)) {
                    grid[i / NUM_ROWS][i % NUM_ROWS] = 0;
                    return i;
                }
                grid[i / NUM_ROWS][i % NUM_ROWS] = 0;
            }
        }
        return -1;
    }

    private void chooseRandomPosition() {
        Random r = new Random();
        while(true){
            int rand = r.nextInt(NUM_ROWS*NUM_ROWS);
            if(choosePosition(rand)) break;
        }
    }

    boolean choosePosition(int pos){
        Button aButton = buttonList.get(pos);
        if(!aButton.getText().equals("X") && !aButton.getText().equals("O")){
            aButton.setText("O");
            grid[pos/NUM_ROWS][pos%NUM_ROWS] = COMP_PLAYER_NUM;
            return true;
        }
        return false;
    }



    public void clearAll(View view) {
        for (int i = 0; i <NUM_ROWS*NUM_ROWS; i++){
            Button aButton = buttonList.get(i);
            aButton.setText("");
            grid[i/NUM_ROWS][i%NUM_ROWS] = 0;
        }
        thereIsWinner = false;
    }

    public boolean boolHasWon(int playerNum){
        for(int i = 0; i < NUM_ROWS; i++){
            int count = 0;
            for(int j = 0; j < NUM_ROWS; j++){
                if(grid[i][j] == playerNum) count++;
            }
            if(count == NUM_ROWS) return true;
        }
        for(int i = 0; i < NUM_ROWS; i++){
            int count = 0;
            for(int j = 0; j < NUM_ROWS; j++){
                if(grid[j][i] == playerNum) count++;
            }
            if(count == NUM_ROWS) return true;
        }
        int count = 0;
        for(int i = 0; i < NUM_ROWS; i++){
            if(grid[i][i] == playerNum) count++;
            if(count == NUM_ROWS) return true;
        }
        count = 0;
        for(int i = 0; i < NUM_ROWS; i++){
            if(grid[i][NUM_ROWS - i - 1] == playerNum) count++;
            if(count == NUM_ROWS) return true;
        }
        return false;
    }

    private void initbuttnList() {
        buttonList.add((Button)findViewById(R.id.b0));
        buttonList.add((Button)findViewById(R.id.b1));
        buttonList.add((Button)findViewById(R.id.b2));
        buttonList.add((Button)findViewById(R.id.b3));
        buttonList.add((Button)findViewById(R.id.b4));
        buttonList.add((Button)findViewById(R.id.b5));
        buttonList.add((Button)findViewById(R.id.b6));
        buttonList.add((Button)findViewById(R.id.b7));
        buttonList.add((Button)findViewById(R.id.b8));
    }

}
