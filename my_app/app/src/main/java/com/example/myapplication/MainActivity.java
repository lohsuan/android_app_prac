package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void random(){
        TextView computer = (TextView) findViewById(R.id.computer);
        Random ran = new Random();
        int num = ran.nextInt( 3);
        switch (num) {
            case 0:
                computer.setText("stone");
                break;
            case 1:
                computer.setText("papper");
                break;
            case 2:
                computer.setText("scissor");
                break;
        }
    }


    public void papperClickEvent(View view) {
        TextView player = (TextView) findViewById(R.id.player);
        TextView computer_res = (TextView) findViewById(R.id.computer);
        TextView result = (TextView) findViewById(R.id.result);
        player.setText("papper");
        random();

        String res_toString = computer_res.getText().toString();
        switch(res_toString){
            case "stone":
                result.setText("You win !");
                break;
            case "scissor":
                result.setText("You lose !");
                break;
            case "papper":
                result.setText("End in draw !");
                break;
        }

    }

    public void scissorClickEvent(View view) {
        TextView player = (TextView) findViewById(R.id.player);
        TextView computer_res = (TextView) findViewById(R.id.computer);
        TextView result = (TextView) findViewById(R.id.result);
        player.setText("sissor");
        random();

        String res_toString = computer_res.getText().toString();
        switch(res_toString){
            case "stone":
                result.setText("You lose !");
                break;
            case "scissor":
                result.setText("End in draw !");
                break;
            case "papper":
                result.setText("You win !");
                break;
        }

    }

    public void stoneClickEvent(View view) {
        TextView player = (TextView) findViewById(R.id.player);
        TextView computer_res = (TextView) findViewById(R.id.computer);
        TextView result = (TextView) findViewById(R.id.result);
        player.setText("stone");
        random();

        String res_toString = computer_res.getText().toString();
        switch(res_toString){
            case "stone":
                result.setText("End in draw !");
                break;
            case "scissor":
                result.setText("You win !");
                break;
            case "papper":
                result.setText("You lose !");
                break;
        }

    }
}