package com.igabeto.mayoromenor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<Player> items;

    public ItemAdapter(Rank context, List<Player> items) {
        this.context = context.getActivity().getApplication().getApplicationContext();
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.record_player, parent, false);
        }

        // Set data into the view.
        TextView txtUser = (TextView) rowView.findViewById(R.id.txtUser);
        TextView txtNormalRecord = (TextView) rowView.findViewById(R.id.txtNormalRecord);
        TextView txtAdvancedRecord = (TextView) rowView.findViewById(R.id.txtAdvancedRecord);
        TextView txtAverage = (TextView) rowView.findViewById(R.id.txtAverage);

        Player item = this.items.get(position);
        txtUser.setText(item.getName());
        txtNormalRecord.setText(String.valueOf(item.getNormalRecord()));
        txtAdvancedRecord.setText(String.valueOf(item.getAdvancedRecord()));
        txtAverage.setText(String.valueOf(item.getAverage()));

        rowView.setTag(item.getName());
        return rowView;
    }
}