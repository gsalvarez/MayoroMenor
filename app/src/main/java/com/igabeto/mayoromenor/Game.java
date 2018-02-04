package com.igabeto.mayoromenor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Game extends Fragment {

    DatabaseHelper db1;
    SQLiteDatabase db;

    public Button btnMayor;
    public Button btnMenor;
    public TextView txtNumero;
    public TextView txtPuntuacion;
    public TextView txtnumPuntuacion;
    public TextView txtVidas;
    public TextView txtnumVidas;
    public int puntuacion;
    public int numeroViejo;
    public int numeroNuevo;
    public int vidas;

    int mode;
    String playerName;
    int newRecord;
    int playerClassicRecord;
    int playerCronoRecord;
    float playerAverage;
    public Random rnd = new Random();
    CountDownTimer timer;
    boolean timerRunning;

    public Game() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_game, container, false);

        db1 = new DatabaseHelper(getActivity().getApplication().getApplicationContext(), DatabaseHelper.DB_NAME, null, DatabaseHelper.db_v);
        db = db1.getWritableDatabase();

        this.mode = getArguments().getInt("mode");
        this.newRecord = 0;
        this.playerName = getArguments().getString("playerName");
        this.playerClassicRecord = getArguments().getInt("playerNormalRecord");
        this.playerCronoRecord = getArguments().getInt("playerAdvancedRecord");
        this.playerAverage = getArguments().getFloat("playerAverage");

        rnd = new Random();
        this.puntuacion = 0;
        this.numeroNuevo = 0;
        this.numeroViejo = 0;
        this.vidas = 5;
        this.timerRunning = false;
        this.btnMayor = (Button) view.findViewById(R.id.btnMayor);
        this.btnMenor = (Button) view.findViewById(R.id.btnMenor);
        this.txtPuntuacion = (TextView) view.findViewById(R.id.txtPuntuacion);
        this.txtnumPuntuacion = (TextView) view.findViewById(R.id.txtnumPuntuacion);
        this.txtVidas = (TextView) view.findViewById(R.id.txtVidas);
        this.txtnumVidas = (TextView) view.findViewById(R.id.txtnumVidas);
        this.txtNumero = (TextView) view.findViewById(R.id.txtNumero);

        loadGameMode();

        btnMayor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNumber();
                if (numeroNuevo >= numeroViejo) {
                    goodPoint();
                }
                else{
                    badPoint();
                }
            }
        });
        btnMenor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNumber();
                if (numeroNuevo <= numeroViejo) {
                    goodPoint();
                }
                else {
                    badPoint();
                }
            }
        });
        return view;
    }

    public void loadGameMode(){
        generateNumber();
        switch(mode){
            case 1:
                txtVidas.setText(getResources().getString(R.string.vidas));
                txtnumVidas.setText(String.valueOf(vidas));
                break;
            case 2:
                txtVidas.setText(String.valueOf(getResources().getString(R.string.time)));
                timer = new CountDownTimer(31000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timerRunning = true;
                        txtnumVidas.setText(String.valueOf(millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        txtnumVidas.setText("0");
                        timerRunning = false;
                        finDelJuego();
                    }
                }.start();
                break;
        }
    }

    public void generateNumber(){
        numeroViejo = Integer.parseInt(String.valueOf(txtNumero.getText()));
        numeroNuevo = rnd.nextInt(10);
        txtNumero.setText(String.valueOf(numeroNuevo));
    }

    public void goodPoint(){
        puntuacion++;
        txtnumPuntuacion.setText(String.valueOf(puntuacion));
        txtNumero.setTextColor(getResources().getColor(android.R.color.holo_green_light));
    }

    public void badPoint(){
        txtNumero.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        switch(mode){
            case 1:
                vidas--;
                txtnumVidas.setText(String.valueOf(vidas));
                checkVidas();
                break;
            case 2:
                if(puntuacion > 0){
                    puntuacion--;
                }
                txtnumPuntuacion.setText(String.valueOf(puntuacion));
                break;
        }
    }

    public void checkVidas(){
        if(vidas==1){
            txtnumVidas.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            txtVidas.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
        else if(vidas==0){
            finDelJuego();
        }
    }

    public void finDelJuego(){
        btnMayor.setClickable(false);
        btnMenor.setClickable(false);
        newRecord = puntuacion;
        checkRecords();
        timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerRunning = true;
            }
            @Override
            public void onFinish() {
                timerRunning = false;
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStackImmediate();
            }
        };
        timer.start();
    }

    public void checkRecords(){
        ContentValues newRecords = new ContentValues();
        switch(mode){
            case 1:
                if(newRecord > playerClassicRecord){
                    newRecords.put("normalRecord", newRecord);
                    newRecords.put("average", (newRecord+playerCronoRecord)/2f);
                    db.update("players", newRecords, "name='"+playerName+"'", null);
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "¡Nuevo récord Clásico!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Tu récord Clásico es "+playerClassicRecord, Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(newRecord > playerCronoRecord){
                    newRecords.put("advancedRecord", newRecord);
                    newRecords.put("average", (playerClassicRecord+newRecord)/2f);
                    db.update("players", newRecords, "name='"+playerName+"'", null);
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "¡Nuevo récord Crono!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity().getApplication().getApplicationContext(), "Tu récord Crono es "+playerCronoRecord, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerRunning){
            timer.cancel();
        }
    }
}