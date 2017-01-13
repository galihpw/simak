package com.galihpw.simak.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galihpw.simak.Komentar;
import com.galihpw.simak.R;

/**
 * Created by Nada on 1/2/2017.
 */

public class AdapterKom extends BaseAdapter {

    private Context mContextKom;

    private Komentar[] mKomentar;

    public AdapterKom(Context ContextKom, Komentar[] Komentar){
        mContextKom = ContextKom;
        mKomentar = Komentar;
    }

    @Override
    public int getCount() {
        return mKomentar.length;
    }

    @Override
    public Object getItem(int position) {
        return mKomentar[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AdapterKom.ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContextKom).inflate(R.layout.listkomentar, null);
            viewHolder = new AdapterKom.ViewHolder();
            viewHolder.namakom = (TextView)convertView.findViewById(R.id.namakom);
            viewHolder.isikom = (TextView)convertView.findViewById(R.id.isikom);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdapterKom.ViewHolder)convertView.getTag();
        }

        Komentar mhsKom = mKomentar[position];
        viewHolder.namakom.setText(mhsKom.getNamaMhsKom() + "");
        viewHolder.isikom.setText(mhsKom.getIsiKomentar() + "");

        return convertView;
    }

    static class ViewHolder{
        TextView namakom;
        TextView isikom;
    }

}
