package com.example.project_mini_g5;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SeekBar horse1, horse2, horse3;
    private Button startButton, addButton, clearButton;
    private boolean isRacing = false;
    private ValueAnimator animator1, animator2, animator3;
    TextView money, winnerText, rate1, rate2, rate3;
    EditText text1, text2, text3;
    double cash1, cash2, cash3;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        money = findViewById(R.id.money);
        money.setText("100");

        horse1 = findViewById(R.id.horse1);
        horse2 = findViewById(R.id.horse2);
        horse3 = findViewById(R.id.horse3);
        horse1.setEnabled(false);
        horse2.setEnabled(false);
        horse3.setEnabled(false);

        text1 = findViewById(R.id.horse1_check);
        text2 = findViewById(R.id.horse2_check);
        text3 = findViewById(R.id.horse3_check);

        rate1 = findViewById(R.id.horse1_rate);
        rate2 = findViewById(R.id.horse2_rate);
        rate3 = findViewById(R.id.horse3_rate);
        randomizeRates(1, 2);

        startButton = findViewById(R.id.startButton);
        addButton = findViewById(R.id.addButton);
        clearButton = findViewById(R.id.clearButton);
        winnerText = findViewById(R.id.winnerTextView);

        startButton.setOnClickListener(this::startRace);
        addButton.setOnClickListener(v -> {
            if (!isRacing) {
                money.setText(String.valueOf(getMoney() + 100));
            }
        });
        clearButton.setOnClickListener(v -> {

            text1.setText("");
            text2.setText("");
            text3.setText("");

            winnerText.setVisibility(View.INVISIBLE);

            Toast.makeText(MainActivity.this, "Bet Cleared!", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean checkCash(){
        cash1 = TextUtils.isEmpty(text1.getText().toString()) ?
                0 :
                Double.parseDouble(text1.getText().toString());
        cash2 = TextUtils.isEmpty(text2.getText().toString()) ?
                0 :
                Double.parseDouble(text2.getText().toString());
        cash3 = TextUtils.isEmpty(text3.getText().toString()) ?
                0 :
                Double.parseDouble(text3.getText().toString());
        double betTotal = cash1 + cash2 + cash3;

        if (betTotal > getMoney()){
            winnerText.setText("Not enough money!");
            winnerText.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, " Not enough money!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (betTotal == 0) {
            winnerText.setText("You must choose at least 1 Car!");
            winnerText.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "You must choose at least 1 Car!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            money.setText(String.valueOf(getMoney() - betTotal));
            return true;
        }
    }

    private void startRace(View view) {
        if (isRacing || !checkCash()) {
            return;
        }
        isRacing = true;
        startButton.setEnabled(false);
        horse1.setProgress(0);
        horse2.setProgress(0);
        horse3.setProgress(0);
        horse1.setEnabled(true);
        horse2.setEnabled(true);
        horse3.setEnabled(true);

        Random random = new Random();
        animator1 = createAnimator(horse1, random.nextInt(2000) + 3000);
        animator2 = createAnimator(horse2, random.nextInt(2000) + 3000);
        animator3 = createAnimator(horse3, random.nextInt(2000) + 3000);

        animator1.start();
        animator2.start();
        animator3.start();
    }

    private ValueAnimator createAnimator(final SeekBar horse, int duration) {
        ValueAnimator animator = ValueAnimator.ofInt(0, horse.getMax());
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            horse.setProgress(progress);
            winnerText.setVisibility(View.INVISIBLE);
            if (progress == horse.getMax()) {
                checkWinner(horse);
            }
        });
        return animator;
    }



    private double getMoney() {
        return Double.parseDouble(money.getText().toString());
    }

    private void randomizeRates(double min, double max) {
        Random random = new Random();
        rate1.setText(String.format(Locale.ENGLISH, "%.2f", (random.nextDouble() * (max - min) + min)));
        rate2.setText(String.format(Locale.ENGLISH, "%.2f", (random.nextDouble() * (max - min) + min)));
        rate3.setText(String.format(Locale.ENGLISH, "%.2f", (random.nextDouble() * (max - min) + min)));
    }

    public void showOddsHelp(View view) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_help_odds, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        AppCompatButton btnOk = popupView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> popupWindow.dismiss());
    }

    public void showRoundResult(double netGain, String winner) {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_result, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(this.getCurrentFocus(), Gravity.CENTER, 0, 0);

        TextView tvWinner = popupView.findViewById(R.id.tvWinner);
        tvWinner.setText(winner + " wins!");

        TextView tvMoney = popupView.findViewById(R.id.tvMoney);
        if (netGain < 0) {
            tvMoney.setText("You have lost $" + -netGain + " this round");
        } else {
            tvMoney.setText("You have won $" + netGain + " this round");
        }

        AppCompatButton btnOk = popupView.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> popupWindow.dismiss());
    }
}