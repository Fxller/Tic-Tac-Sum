package com.example.guidaluigi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    int mosseCounter = 0;
    // True = R, False = C
    boolean checkRC = true;
    boolean alreadyWon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridLayout grid = findViewById(R.id.gridLayout);
        TextView mosse = findViewById(R.id.mosseCounter);
        Button restart = findViewById(R.id.restartButton);
        Button bonus = findViewById(R.id.bonusButton);
        Switch switchButton = findViewById(R.id.switchButton);
        // Listener per lo switch, se è attivo checkRC = false, altrimenti checkRC = true
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkRC = false;
                } else {
                    checkRC = true;
                }
            }
        });
        // Creazione della griglia
        createGrid(grid);

        // Listener per il bottone restart, se premuto ricomincia la partita
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mosseCounter = 0;
                updateMosseCounter(mosse);
                createGrid(grid);
                Toast.makeText(MainActivity.this, "Partita ricominciata!", Toast.LENGTH_SHORT).show();
            }
        });

        // Listener per il bottone bonus, se premuto aggiunge 15 mosse e azzera una riga o una colonna
        bonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int random = (int) (Math.random() * 3);
                if (checkRC) {
                    for (int i = 0; i < 3; i++) {
                        TextView cella = (TextView) grid.getChildAt(random * 3 + i);
                        cella.setText("0");
                    }
                } else {
                    for (int i = 0; i < 3; i++) {
                        TextView cella = (TextView) grid.getChildAt(i * 3 + random);
                        cella.setText("0");
                    }
                }
                Toast.makeText(MainActivity.this, "Bonus utilizzato!", Toast.LENGTH_SHORT).show();
                mosseCounter += 15;
                updateMosseCounter(mosse);
                checkWin();
            }
        });
    }

    // Metodo per aggiornare il contatore delle mosse
    private void updateMosseCounter(TextView mosse) {
        mosse.setText("Mosse effettuate: " + mosseCounter);
    }

    // Metodo per creare la griglia
    private void createGrid(GridLayout grid) {
        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }

        // Metodo che serve per disporre in ordine casuale gli elementi dell'array
        Collections.shuffle(numbers);

        for (int i = 0; i < grid.getChildCount(); i++) {
            TextView cella = (TextView) grid.getChildAt(i);
            cella.setText(String.valueOf(numbers.get(i)));
        }
    }

    // Metodo per gestire il click su una cella
    public void onCellClick(View view) {
        TextView cella = (TextView) view;
        GridLayout grid = findViewById(R.id.gridLayout);
        int cellaValue = Integer.parseInt(cella.getText().toString());
        int cellaIndex = grid.indexOfChild(cella);
        int row = cellaIndex / 3;
        int col = cellaIndex % 3;

        if (checkRC) {
            for (int i = 0; i < 3; i++) {
                int index = row * 3 + i;
                TextView cellaR = (TextView) grid.getChildAt(index);
                int cellaValueR = Integer.parseInt(cellaR.getText().toString());
                cellaR.setText(String.valueOf((cellaValue + cellaValueR) % 10));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                int index = i * 3 + col;
                TextView cellaC = (TextView) grid.getChildAt(index);
                int cellaValueC = Integer.parseInt(cellaC.getText().toString());
                cellaC.setText(String.valueOf((cellaValue + cellaValueC) % 10));
            }
        }
        mosseCounter++;
        updateMosseCounter(findViewById(R.id.mosseCounter));
        checkWin();
    }

    // Metodo per controllare se il giocatore ha vinto, ovvero se tutte le celle sono 0
    public void checkWin() {
        GridLayout grid = findViewById(R.id.gridLayout);
        boolean win = true;
        for (int i = 0; i < grid.getChildCount(); i++) {
            TextView cella = (TextView) grid.getChildAt(i);
            if (!cella.getText().toString().equals("0")) {
                win = false;
                break;
            }
        }
        if (win && !alreadyWon) {
            Toast.makeText(MainActivity.this, "Hai vinto!", Toast.LENGTH_SHORT).show();
            alreadyWon = true;
        } else if (win) {
            Toast.makeText(MainActivity.this, "Hai già vinto! Ricomincia la partita!", Toast.LENGTH_SHORT).show();
        }
    }
}