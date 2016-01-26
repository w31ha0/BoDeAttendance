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
    private static List<String> listbxb1,listbxb2,listbxb3,listdjjtr,listdzb,listqkb,listzxb1,listzxb2,listzxb3;
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
        listbxb1=new ArrayList<String>();
        listbxb2=new ArrayList<String>();
        listbxb3=new ArrayList<String>();
        listzxb1=new ArrayList<String>();
        listzxb2=new ArrayList<String>();
        listzxb3=new ArrayList<String>();
        listdjjtr=new ArrayList<String>();
        listdzb=new ArrayList<String>();
        listqkb=new ArrayList<String>();
        banqiMap.put(3,listbxb1);
        banqiMap.put(4,listbxb2);
        banqiMap.put(5,listbxb3);
        banqiMap.put(6,listzxb1);
        banqiMap.put(7,listzxb2);
        banqiMap.put(8,listzxb3);
        banqiMap.put(9,listdjjtr);
        banqiMap.put(10,listdzb);
        banqiMap.put(11,listqkb);
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
                            for (int i = 0; i < objects.length(); i++) {
                                String bxb1="";String bxb2="";String bxb3="";String zxb1="";String zxb2="";String zxb3="";String djjtr="";String dzb="";String qkb="";
                                JSONObject session = objects.getJSONObject(i);
                                if (session.has(CommonUtilities.keys.get(3))){
                                    if (session.get(CommonUtilities.keys.get(3)) instanceof String)
                                        continue;
                                    bxb1=session.getString(CommonUtilities.keys.get(3));
                                }
                                if (session.has(CommonUtilities.keys.get(4)))
                                    bxb2=session.getString(CommonUtilities.keys.get(4));
                                if (session.has(CommonUtilities.keys.get(5)))
                                    bxb3=session.getString(CommonUtilities.keys.get(5));
                                if (session.has(CommonUtilities.keys.get(6)))
                                    zxb1=session.getString(CommonUtilities.keys.get(6));
                                if (session.has(CommonUtilities.keys.get(7)))
                                    zxb2=session.getString(CommonUtilities.keys.get(7));
                                if (session.has(CommonUtilities.keys.get(8)))
                                    zxb3=session.getString(CommonUtilities.keys.get(8));
                                if (session.has(CommonUtilities.keys.get(9)))
                                    djjtr=session.getString(CommonUtilities.keys.get(9));
                                if (session.has(CommonUtilities.keys.get(10)))
                                    dzb=session.getString(CommonUtilities.keys.get(10));
                                if (session.has(CommonUtilities.keys.get(11)))
                                    qkb=session.getString(CommonUtilities.keys.get(11));

                                if (bxb1=="1")
                                    listbxb1.add(session.getString("姓名"));
                                if (bxb2=="1")
                                    listbxb2.add(session.getString("姓名"));
                                if (bxb3=="1")
                                    listbxb3.add(session.getString("姓名"));
                                if (zxb1=="1")
                                    listzxb1.add(session.getString("姓名"));
                                if (zxb2=="1")
                                    listzxb2.add(session.getString("姓名"));
                                if (zxb3=="1")
                                    listzxb3.add(session.getString("姓名"));
                                if (djjtr=="1")
                                    listdjjtr.add(session.getString("姓名"));
                                if (dzb=="1")
                                    listdzb.add(session.getString("姓名"));
                                if (qkb=="1")
                                    listqkb.add(session.getString("姓名"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i=3;i<12;i++){
                            myMergeAdapter.addView(buildLabel(CommonUtilities.keys.get(i),0,15));
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
