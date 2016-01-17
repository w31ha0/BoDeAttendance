package com.google.android.gcm.demo.app;

/**
 * Created by Wei Hao on 1/6/2016.
 */

        import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wei Hao on 12/25/2015.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    private int mSelection = 0;
    private View lastSelectedRow;
    private List<String> namelist;
    private SharedPreferences sharedPreferences;

    public CustomAdapter(Context context, List<String> namelist) {
        super(context, R.layout.custom_row2, namelist);
        this.namelist = new ArrayList<String>();
        this.namelist = namelist;
    }

    public void setSelection(int mSelection, View selectedItemView) {
        this.mSelection = mSelection;

        if (selectedItemView!= null && lastSelectedRow != null
                && selectedItemView!= lastSelectedRow) {
            lastSelectedRow
                    .setBackgroundResource(R.color.wallet_highlighted_text_holo_light);
            selectedItemView
                    .setBackgroundResource(R.color.accent_material_dark);
        }

        this.lastSelectedRow = selectedItemView;

    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        LayoutInflater layoutInflater=LayoutInflater.from(getContext());
        String name=namelist.get(position);
        System.out.println(name);
        View customView =layoutInflater.inflate(R.layout.custom_row2, parent, false);
        TextView textView=(TextView)customView.findViewById(R.id.BY_name);
        textView.setText(name);
        if (CommonUtilities.namelist!=null){
            if(CommonUtilities.namelist.contains(name))
                customView.setBackgroundResource(R.color.material_blue_grey_800);
        }
        return customView;
    }


}

