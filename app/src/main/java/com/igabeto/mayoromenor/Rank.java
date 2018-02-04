package com.igabeto.mayoromenor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Rank extends Fragment {
    ListView listViewRecords;
    TextView tvName;
    TextView tvNormalRecord;
    TextView tvAdvancedRecord;
    TextView tvAverage;

    ItemAdapter adapter;
    List<Player> itemsRecords = new ArrayList<>();
    Rank records = this;

    DatabaseHelper db1;
    SQLiteDatabase db;

    public Rank() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rank, container, false);

        db1 = new DatabaseHelper(getActivity().getApplication().getApplicationContext(), DatabaseHelper.DB_NAME, null, DatabaseHelper.db_v);
        db = db1.getWritableDatabase();

        listViewRecords = (ListView) view.findViewById(R.id.listViewRank);
        tvName = (TextView) view.findViewById(R.id.name);
        tvNormalRecord = (TextView) view.findViewById(R.id.classic);
        tvAdvancedRecord = (TextView) view.findViewById(R.id.crono);
        tvAverage = (TextView) view.findViewById(R.id.average);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tvNormalRecord.setTextColor(getResources().getColor(android.R.color.white));
                tvAdvancedRecord.setTextColor(getResources().getColor(android.R.color.white));
                tvAverage.setTextColor(getResources().getColor(android.R.color.white));
                updateListView(1);
            }
        });
        tvNormalRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setTextColor(getResources().getColor(android.R.color.white));
                tvNormalRecord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tvAdvancedRecord.setTextColor(getResources().getColor(android.R.color.white));
                tvAverage.setTextColor(getResources().getColor(android.R.color.white));
                updateListView(2);
            }
        });
        tvAdvancedRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setTextColor(getResources().getColor(android.R.color.white));
                tvNormalRecord.setTextColor(getResources().getColor(android.R.color.white));
                tvAdvancedRecord.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tvAverage.setTextColor(getResources().getColor(android.R.color.white));
                updateListView(3);
            }
        });
        tvAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvName.setTextColor(getResources().getColor(android.R.color.white));
                tvNormalRecord.setTextColor(getResources().getColor(android.R.color.white));
                tvAdvancedRecord.setTextColor(getResources().getColor(android.R.color.white));
                tvAverage.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                updateListView(4);
            }
        });

        listViewRecords.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("El jugador será eliminado");
                final View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.delete_dialog, (ViewGroup) getView(), false);
                TextView txtDelete = (TextView) viewInflated.findViewById(R.id.txtDelete);
                String delete = getResources().getString(R.string.deleteText)+" "+view.getTag().toString()+"?";
                txtDelete.setText(delete);
                alertDialog.setView(viewInflated);
                alertDialog.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete("players", "name='"+view.getTag().toString()+"'", null);
                        updateListView(4);
                        dialog.dismiss();
                    }
                });
                alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

                return true;
            }
        });

        updateListView(4);

        return view;
    }

    public void updateListView(int x){
        itemsRecords.clear();
        Cursor cursor = db.rawQuery("SELECT name, normalRecord, advancedRecord, average FROM players", null);
        switch(x){
            case 1:
                cursor = db.rawQuery("SELECT name, normalRecord, advancedRecord, average FROM players ORDER BY name", null);
                break;
            case 2:
                cursor = db.rawQuery("SELECT name, normalRecord, advancedRecord, average FROM players ORDER BY normalRecord DESC", null);
                break;
            case 3:
                cursor = db.rawQuery("SELECT name, normalRecord, advancedRecord, average FROM players ORDER BY advancedRecord DESC", null);
                break;
            case 4:
                cursor = db.rawQuery("SELECT name, normalRecord, advancedRecord, average FROM players ORDER BY average DESC", null);
                break;
        }

        while(cursor.moveToNext()){
            itemsRecords.add(new Player (cursor.getString(0), cursor.getInt(1), cursor.getInt(2), cursor.getFloat(3)));
        }

        adapter = new ItemAdapter(records, itemsRecords);
        listViewRecords.setAdapter(adapter);
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }
}