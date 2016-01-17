package com.google.android.gcm.demo.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainFragment extends Fragment {

    private String currentName;
    private TextView bxb1;
    private TextView bxb2;
    private TextView bxb3;
    private TextView zxb1;
    private TextView zxb2;
    private TextView zxb3;
    private TextView djjtr;
    private TextView dzb;
    private TextView qkb;
    private RadioGroup gbxb1;
    private RadioGroup gbxb2;
    private RadioGroup gbxb3;
    private RadioGroup gzxb1;
    private RadioGroup gzxb2;
    private RadioGroup gzxb3;
    private RadioGroup gdzb;
    private RadioGroup gqkb;
    private RadioGroup gdjjtr;
    private Boolean goingbxb1,goingbxb2,goingbxb3,goingdjjtr,goingdzb,goingqkb,goingzxb1,goingzxb2,goingzxb3;
    private Button button;
    private Spinner dropdown;
    private Context context;
    private SharedPreferences sharedPreferences;
    private List<String> namelist;
    public MainFragment(Context context) {
        this.context=context;
    }
    public static final String PROPERTY_REG_ID="PROPERTY";

    public MainFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getKeys("https://script.google.com/macros/s/AKfycbyWgDSTvO5elKoj4IYkY_2agMAGZwuMENuN_95JUWMKm3IIcpk/exec");
        View root = inflater.inflate(R.layout.main, container, false);
        namelist = new ArrayList<String>();
        sharedPreferences = getActivity().getSharedPreferences("KEY", Context.MODE_PRIVATE);
        dropdown = (Spinner) root.findViewById(R.id.spinner1);
        dropdown.setBackgroundColor(Color.GRAY);
        Set<String> set = sharedPreferences.getStringSet("KEY", null);
        if (set != null && !set.isEmpty()) {
            namelist = new ArrayList<String>(set);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CommonUtilities.context, android.R.layout.simple_spinner_dropdown_item, namelist);
            dropdown.setAdapter(adapter);
        }
        button = (Button) root.findViewById(R.id.button);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentName = namelist.get(position);
                refresh();
                System.out.println(currentName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        bxb1 = (TextView) root.findViewById(R.id.bxb1);
        bxb2 = (TextView) root.findViewById(R.id.bxb2);
        bxb3 = (TextView) root.findViewById(R.id.bxb3);
        zxb1 = (TextView) root.findViewById(R.id.zxb1);
        zxb2 = (TextView) root.findViewById(R.id.zxb2);
        zxb3 = (TextView) root.findViewById(R.id.zxb3);
        djjtr = (TextView) root.findViewById(R.id.djjtr);
        dzb = (TextView) root.findViewById(R.id.dzb);
        qkb = (TextView) root.findViewById(R.id.qkb);
        gbxb1 = (RadioGroup) root.findViewById(R.id.radioGroup1);
        gbxb2 = (RadioGroup) root.findViewById(R.id.radioGroup2);
        gbxb3 = (RadioGroup) root.findViewById(R.id.radioGroup3);
        gzxb1 = (RadioGroup) root.findViewById(R.id.radioGroup4);
        gzxb2 = (RadioGroup) root.findViewById(R.id.radioGroup5);
        gzxb3 = (RadioGroup) root.findViewById(R.id.radioGroup6);
        gdjjtr = (RadioGroup) root.findViewById(R.id.radioGroup7);
        gdzb = (RadioGroup) root.findViewById(R.id.radioGroup8);
        gqkb = (RadioGroup) root.findViewById(R.id.radioGroup9);
        gbxb1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yesbxb1:
                        goingbxb1 = true;
                        break;
                    case R.id.nobxb1:
                        goingbxb1 = false;
                        break;
                }
            }
        });
        gbxb2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yesbxb2:
                        goingbxb2 = true;
                        break;
                    case R.id.nobxb2:
                        goingbxb2 = false;
                        break;
                }
            }
        });
        gbxb3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yesbxb3:
                        goingbxb3 = true;
                        break;
                    case R.id.nobxb3:
                        goingbxb3 = false;
                        break;
                }
            }
        });
        gzxb1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yeszxb1:
                        goingzxb1 = true;
                        break;
                    case R.id.nozxb1:
                        goingzxb1 = false;
                        break;
                }
            }
        });
        gzxb2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yeszxb2:
                        goingzxb2 = true;
                        break;
                    case R.id.nozxb2:
                        goingzxb2 = false;
                        break;
                }
            }
        });
        gzxb3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yeszxb3:
                        goingzxb3 = true;
                        break;
                    case R.id.nozxb3:
                        goingzxb3 = false;
                        break;
                }
            }
        });
        gdjjtr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yesdjjtr:
                        goingdjjtr = true;
                        break;
                    case R.id.nodjjtr:
                        goingdjjtr = false;
                        break;
                }
            }
        });
        gdzb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yesdzb:
                        goingdzb = true;
                        break;
                    case R.id.nodzb:
                        goingdzb = false;
                        break;
                }
            }
        });
        gqkb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yesqkb:
                        goingqkb = true;
                        break;
                    case R.id.noqkb:
                        goingqkb = false;
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://script.google.com/macros/s/AKfycbwdFAsb-fkbdRao-1JPE2Q0Z85RjIozfe5M3vF3XNleF6mWD-jf/exec";
                String bxb1, bxb2, bxb3, zxb1, zxb2, zxb3, djjtr, dzb, qkb;
                bxb1 = bxb2 = bxb3 = zxb1 = zxb2 = zxb3 = djjtr = dzb = qkb = "";
                if (goingbxb1 != null)
                    bxb1 = goingbxb1 ? "1" : "0";
                if (goingbxb2 != null)
                    bxb2 = goingbxb2 ? "1" : "0";
                if (goingbxb3 != null)
                    bxb3 = goingbxb3 ? "1" : "0";
                if (goingzxb1 != null)
                    zxb1 = goingzxb1 ? "1" : "0";
                if (goingzxb2 != null)
                    zxb2 = goingzxb2 ? "1" : "0";
                if (goingzxb3 != null)
                    zxb3 = goingzxb3 ? "1" : "0";
                if (goingdjjtr != null)
                    djjtr = goingdjjtr ? "1" : "0";
                if (goingdzb != null)
                    dzb = goingdzb ? "1" : "0";
                if (goingqkb != null)
                    qkb = goingqkb ? "1" : "0";
                url = url + "?name=" + currentName + "&bxb1=" + bxb1 + "&bxb2=" + bxb2 + "&bxb3=" + bxb3 + "&djjtr=" + djjtr + "&dzb=" + dzb + "&qkb=" + qkb + "&zxb1=" + zxb1 + "&zxb2=" + zxb2 + "&zxb3=" + zxb3;
                System.out.println(url);
                new submitTask().execute(url);
                //submit(url);
            }
        });
        return root;
    }

    private void submit(String s){
        RequestQueue queue = VolleySingleton.getsInstance().getmRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast toast= Toast.makeText(context, "Changes have been submitted successfully", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void getKeys(String s){
        RequestQueue queue = VolleySingleton.getsInstance().getmRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray objects = null;
                        try {
                            objects = new JSONArray(response);
                            JSONObject session = objects.getJSONObject(0);
                            Iterator<String> iterator=  session.keys();
                            CommonUtilities.keys=new ArrayList<>();
                            while (iterator.hasNext())
                                CommonUtilities.keys.add(iterator.next());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<String> result=new ArrayList<String>();
                        result.addAll(CommonUtilities.keys);
                        bxb1.setText(result.get(3));
                        bxb2.setText(result.get(4));
                        bxb3.setText(result.get(5));
                        zxb1.setText(result.get(6));
                        zxb2.setText(result.get(7));
                        zxb3.setText(result.get(8));
                        djjtr.setText(result.get(9));
                        dzb.setText(result.get(10));
                        qkb.setText(result.get(11));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private class submitTask extends AsyncTask<String,Void,Void> {
        boolean succeed;
        @Override
        protected Void doInBackground(String... params) {
            try {
                HttpClient hc = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                HttpResponse rp = hc.execute(get);
                if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    String result = EntityUtils.toString(rp.getEntity());
                    succeed=true;
                }
                final SharedPreferences prefs =
                        getActivity().getSharedPreferences(DemoActivity.class.getSimpleName(), Context.MODE_PRIVATE);
                String registrationId = prefs.getString(PROPERTY_REG_ID, "");
                String u="http://lewspage.hostei.com/scripts/edit.php?link="+registrationId;
                get = new HttpGet(u);
                System.out.println("URLL "+u);
                rp = hc.execute(get);
                if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                }
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (succeed){
                DemoActivity.mAppSectionsPagerAdapter.notifyDataSetChanged();
                Toast toast= Toast.makeText(context, "Changes have been submitted successfully", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                Toast toast= Toast.makeText(context, "Error!Please check your internet connection", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void refresh(){
        final MyProgressDialog pd=MyProgressDialog.show(CommonUtilities.context, "", "");
        gdjjtr.clearCheck();
        gbxb1.clearCheck();
        gbxb2.clearCheck();
        gbxb3.clearCheck();
        gzxb1.clearCheck();
        gzxb2.clearCheck();
        gzxb3.clearCheck();
        gdzb.clearCheck();
        gqkb.clearCheck();
        goingbxb1=goingbxb2=goingbxb3=goingzxb1=goingzxb2=goingzxb3=goingdjjtr=goingdzb=goingqkb=null;
        final List<Integer> ints=new ArrayList<Integer>();
        String URL="https://script.google.com/macros/s/AKfycbxM-vcwreQme8aX8_sWdKcc-KXFPzZ9XQzRm6u_-oIYjNXUhxI/exec";
        RequestQueue queue = VolleySingleton.getsInstance().getmRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response "+response);
                        JSONArray objects = null;
                        try {
                            objects = new JSONArray(response);
                            for (int i = 0; i < objects.length(); i++) {
                                JSONObject session = objects.getJSONObject(i);
                                if (session.has("姓名")){
                                    if (session.getString("姓名").equals(currentName)){
                                        if(session.has(CommonUtilities.keys.get(3)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(3)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(4)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(4)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(5)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(5)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(6)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(6)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(7)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(7)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(8)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(8)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(9)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(9)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(10)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(10)));
                                        else
                                            ints.add(-1);
                                        if(session.has(CommonUtilities.keys.get(11)))
                                            ints.add(session.getInt(CommonUtilities.keys.get(11)));
                                        else
                                            ints.add(-1);
                                        break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (pd.isShowing())
                            pd.dismiss();
                        if (ints==null || ints.isEmpty())
                            return;
                        if (ints.get(0)==1){
                            gbxb1.check(R.id.yesbxb1);
                        }
                        else if (ints.get(0)==0){
                            gbxb1.check(R.id.nobxb1);
                        }
                        if (ints.get(1)==1){
                            gbxb2.check(R.id.yesbxb2);
                        }
                        else if (ints.get(1)==0){
                            gbxb2.check(R.id.nobxb2);
                        }

                        if (ints.get(2)==1){
                            gbxb3.check(R.id.yesbxb3);
                        }

                        else if (ints.get(2)==0){
                            gbxb3.check(R.id.nobxb3);
                        }
                        if (ints.get(3)==1){
                            gzxb1.check(R.id.yeszxb1);
                        }
                        else if (ints.get(3)==0){
                            gzxb1.check(R.id.nozxb1);
                        }

                        if (ints.get(4)==1){
                            gzxb2.check(R.id.yeszxb2);
                        }

                        else if (ints.get(4)==0){
                            gzxb2.check(R.id.nozxb2);
                        }

                        if (ints.get(5)==1){
                            gzxb3.check(R.id.yeszxb3);
                        }

                        else if (ints.get(5)==0){
                            gzxb3.check(R.id.nozxb3);
                        }

                        if (ints.get(6)==1){
                            gdjjtr.check(R.id.yesdjjtr);
                        }
                        else if (ints.get(6)==0){
                            gdjjtr.check(R.id.nodjjtr);
                        }
                        if (ints.get(7)==1){
                            gdzb.check(R.id.yesdzb);
                        }

                        else if (ints.get(7)==0){
                            gdzb.check(R.id.nodzb);
                        }
                        if (ints.get(8)==1){
                            gqkb.check(R.id.yesqkb);
                        }
                        else if (ints.get(8)==0){
                            gqkb.check(R.id.noqkb);
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        });
        queue.add(stringRequest);
    }

}
