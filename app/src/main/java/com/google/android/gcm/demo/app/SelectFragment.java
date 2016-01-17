package com.google.android.gcm.demo.app;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.merge.MergeAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Wei Hao on 1/6/2016.
 */
public class SelectFragment extends Fragment {

    private ListView listView;
    private Context context;
    private ListAdapter adapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Button save;
    MergeAdapter myMergeAdapter;
    View root;

    public SelectFragment(Context context){
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.selector, container, false);
        myMergeAdapter=new MergeAdapter();
        sharedPreferences=getActivity().getSharedPreferences("KEY",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        listView= (ListView) root.findViewById(R.id.listView);
        save=(Button)root.findViewById(R.id.button2);
        CommonUtilities.namelist=new ArrayList<String>();
        Set<String> set = sharedPreferences.getStringSet("KEY", null);
        if (set!= null && !set.isEmpty())
            CommonUtilities.namelist = new ArrayList<String>(set);
        myMergeAdapter.addView(buildLabel("C 组", 0));
        ListAdapter adapter=new CustomAdapter(context,CommonUtilities.ClassMap.get("C"));
        myMergeAdapter.addAdapter(adapter);
        myMergeAdapter.addView(buildLabel("B 组", 0));
        adapter=new CustomAdapter(context,CommonUtilities.ClassMap.get("B"));
        myMergeAdapter.addAdapter(adapter);
        myMergeAdapter.addView(buildLabel("A 组", 0));
        adapter=new CustomAdapter(context,CommonUtilities.ClassMap.get("A"));
        myMergeAdapter.addAdapter(adapter);
        listView.setAdapter(myMergeAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setSelected(true);
                String data=(String)adapterView.getItemAtPosition(position);
                if (CommonUtilities.ClassMap.get("C").contains(data))
                    position--;
                else if(CommonUtilities.ClassMap.get("B").contains(data))
                    position-=2;
                else if (CommonUtilities.ClassMap.get("A").contains(data))
                    position-=3;

                if (view.getBackground() == null) {
                    view.setBackgroundResource(R.color.material_blue_grey_800);
                    CommonUtilities.namelist.add(CommonUtilities.names.get(position));

                } else {
                    view.setBackgroundResource(0);
                    CommonUtilities.namelist.remove(CommonUtilities.names.get(position));
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> set = new HashSet<String>();
                set.addAll(CommonUtilities.namelist);
                editor.putStringSet("KEY", set);
                editor.commit();
                Toast toast= Toast.makeText(context, "Name list has been saved successfully", Toast.LENGTH_SHORT);
                toast.show();
                DemoActivity.mAppSectionsPagerAdapter.notifyDataSetChanged();
            }
        });
        return root;
    }

    private View buildLabel(String s,int pad) {
        TextView result=new TextView(context);
        result.setPadding(pad, 0, 0, 0);
        result.setText(s);
        return (result);
    }

}
