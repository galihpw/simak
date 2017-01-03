package com.galihpw.simak.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galihpw.simak.Topik;
import com.galihpw.simak.R;

import java.util.List;

/**
 * Created by GalihPW on 07/11/2016.
 */

public class TpkAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Topik> items;

    public TpkAdapter(Activity activity, List<Topik> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.listtopik, null);

        //TextView judul = (TextView) convertView.findViewById(R.id.judul);
        //TextView deskripsi = (TextView) convertView.findViewById(R.id.deskripsi);
        //TextView penanya = (TextView) convertView.findViewById(R.id.penanya);

        Topik data = items.get(position);

        /*judul.setText(data.getJudul());
        deskripsi.setText(data.getDeskripsi());
        penanya.setText(data.getPenanya());*/

        return convertView;
    }
}
