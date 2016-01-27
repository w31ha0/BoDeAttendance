package com.google.android.gcm.demo.app;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.commonsware.cwac.merge.MergeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Wei Hao on 1/6/2016.
 */
public class ViewFragment extends Fragment {
    private ListView listview;
    private Context context;
    MergeAdapter myMergeAdapter;
    HashMap<Integer,List<String>> banqiMap;
    List<Integer> att;
    List<Integer> att2;
    View root;
    public ViewFragment(Context context){
        this.context=context;
    }

    public ViewFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.viewlayout, container, false);
        att=new ArrayList<Integer>();
        att2=new ArrayList<Integer>();
        banqiMap=new HashMap<Integer,List<String>>();
        listview= (ListView) root.findViewById(R.id.listview);
        myMergeAdapter = new MergeAdapter();
        grab();
        return root;
    }

    private void grab(){
       final MyProgressDialog pd=MyProgressDialog.show(CommonUtilities.context, "", "");
        String URL;
        System.out.println(System.currentTimeMillis());
        URL="https://script.google.com/macros/s/AKfycbxM-vcwreQme8aX8_sWdKcc-KXFPzZ9XQzRm6u_-oIYjNXUhxI/exec";
        RequestQueue queue = VolleySingleton.getsInstance().getmRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray objects = null;
                        try {
                            objects = new JSONArray(response);
                            for (int i=0;i<CommonUtilities.keys.size();i++)
                                banqiMap.put(i,new ArrayList<String>());
                            for (int i = 0; i < objects.length(); i++) {
                                HashMap<String,String> map=new HashMap<String,String>();
                                JSONObject session = objects.getJSONObject(i);
                                for (int x=0;x<CommonUtilities.keys.size();x++){
                                    String key=CommonUtilities.keys.get(x);
                                    String result;
                                    if (session.has(key)){
                                        result=session.getString(key);
                                        if(result=="1")
                                            banqiMap.get(x).add(session.getString("姓名"));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i<CommonUtilities.keys.size();i++){
                            myMergeAdapter.addView(buildLabel(CommonUtilities.keys.get(i),0,22));
                            myMergeAdapter.addView(buildLabel("C 组",10,12));
                            Iterator<String> iterator=banqiMap.get(i).iterator();
                            List<String> tmplist=new ArrayList<String>();
                            while (iterator.hasNext()){
                                String s=iterator.next();
                                if (CommonUtilities.ClassMap.get("C").contains(s))
                                    tmplist.add(s);
                            }
                            ListAdapter adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_activated_1,tmplist);
                            myMergeAdapter.addAdapter(adapter);
                            myMergeAdapter.addView(buildLabel("B 组",10,12));
                            iterator=banqiMap.get(i).iterator();
                            tmplist=new ArrayList<String>();
                            while (iterator.hasNext()){
                                String s=iterator.next();
                                if (CommonUtilities.ClassMap.get("B").contains(s))
                                    tmplist.add(s);
                            }
                            adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_activated_1,tmplist);
                            myMergeAdapter.addAdapter(adapter);
                            myMergeAdapter.addView(buildLabel("A 组",10,12));
                            iterator=banqiMap.get(i).iterator();
                            tmplist=new ArrayList<String>();
                            while (iterator.hasNext()){
                                String s=iterator.next();
                                if (CommonUtilities.ClassMap.get("A").contains(s))
                                    tmplist.add(s);
                            }
                            adapter=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_activated_1,tmplist);
                            myMergeAdapter.addAdapter(adapter);
                        }
                        if (pd.isShowing())
                            pd.dismiss();
                        listview.setAdapter(myMergeAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        });
        queue.add(stringRequest);
    }

    private View buildLabel(String s,int pad,float size) {
        TextView result=new TextView(context);
        result.setPadding(pad,0,10,10);
        result.setTextSize(size);
        result.setText(s);
        return (result);
    }

}
