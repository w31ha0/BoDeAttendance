package com.google.android.gcm.demo.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainFragment extends Fragment {

    private String currentName;
    private Button button;
    private Spinner dropdown;
    private Context context;
    private SharedPreferences sharedPreferences;
    private List<String> namelist;
    private boolean initialising;
    private HashMap<String,String> reason;
    private List<RadioGroup> RGMap;
    private String selected;
    InputMethodManager imm;
    LinearLayout ll;
    LinearLayout.LayoutParams p;

    public MainFragment(Context context) {
        this.context=context;
    }
    public static final String PROPERTY_REG_ID="PROPERTY";

    public MainFragment(){

    }

    private void checkWhiteSpace(){
        Iterator it = reason.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(pair.getValue().toString().contains(" ")){
                String s=pair.getValue().toString().replaceAll(" ","+");
                reason.put(pair.getKey().toString(),s);
            }
        }
    }

    private void createDialog(final String banqi){
        if (initialising)
            return ;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Please input your reason");
        final EditText input = new EditText(this.context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String s = input.getText().toString();
                if (s.trim().length() == 0 || s.equals(" "))
                    reason.put(banqi, "0");
                else
                    reason.put(banqi, s);
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reason.put(banqi, "0");
                imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
                dialog.cancel();
            }
        });
        builder.show();
        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imm = (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
        button=new Button(context);
        button.setText("Submit Changes");
        initialising=true;
        reason=new HashMap<String,String>();
        RGMap=new ArrayList<RadioGroup>();
        getKeys("https://script.google.com/macros/s/AKfycbyWgDSTvO5elKoj4IYkY_2agMAGZwuMENuN_95JUWMKm3IIcpk/exec");
        View root = inflater.inflate(R.layout.main, container, false);
        ll = (LinearLayout)root.findViewById(R.id.layoutmain);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWhiteSpace();
                String url = "https://script.google.com/macros/s/AKfycbxbcEHtXledjlugXBzqOHq2DoXSRzNWKZxjymknj5T2h0ZFB-M/exec?name="+currentName+"&bq=";
                for (int i=0;i<CommonUtilities.keys.size();i++)
                    url=url+reason.get(CommonUtilities.keys.get(i))+",";
                System.out.println(url);
                new submitTask().execute(url);
            }
        });
        if (namelist.isEmpty()) {
            Toast toast= Toast.makeText(context, "Namelist is empty! Please select at least a name from Name List!", Toast.LENGTH_LONG);
            toast.show();
        }
        return root;
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
                            iterator.next();iterator.next();iterator.next();
                            while (iterator.hasNext())
                                CommonUtilities.keys.add(iterator.next());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<String> result=new ArrayList<String>();
                        result.addAll(CommonUtilities.keys);
                        for (int i=0;i<CommonUtilities.keys.size();i++){
                            createBanQi(CommonUtilities.keys.get(i),i+2);
                            reason.put(CommonUtilities.keys.get(i), "");
                        }
                        ll.addView(button);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    private void createBanQi(String title,int id) {
        LinearLayout LL = new LinearLayout(context);
        LL.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LL.setLayoutParams(LLParams);
        TextView tv=new TextView(context);
        tv.setText(title);
        tv.setPadding(10, 10, 10, 10);
        tv.setWidth(CommonUtilities.width/3);
        final RadioButton rb1 = new RadioButton(context);
        final RadioButton rb2 = new RadioButton(context);
        rb1.setWidth(2*CommonUtilities.width/7);
        rb2.setWidth(CommonUtilities.width/3);
        rb1.setGravity(Gravity.CENTER);
        rb2.setGravity(Gravity.CENTER);
        rb1.setId(1);
        rb2.setId(0);
        RadioGroup rg = new RadioGroup(context); //create the RadioGroup
        rg.setOrientation(LinearLayout.HORIZONTAL);//or RadioGroup.VERTICAL
        rg.setId(id);
        rg.setPadding(CommonUtilities.width/12,0,CommonUtilities.width/12,0);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String banqi = CommonUtilities.keys.get(group.getId()-2);
                switch (checkedId) {
                    case 1:
                        reason.put(banqi, "1");
                        break;
                    case 0:
                        createDialog(banqi);
                        break;
                }
            }
        });
        rg.addView(rb1);
        rg.addView(rb2);
        RGMap.add(rg);
        LL.addView(tv);
        LL.addView(rg);
        ll.addView(LL);//you add the whole RadioGroup to the layout
    }
     class submitTask extends AsyncTask<String,Void,Void> {
        boolean succeed;
        @Override
        protected Void doInBackground(String... params) {
            try {
                HttpClient hc = new DefaultHttpClient();
                HttpGet get = new HttpGet(params[0]);
                HttpResponse rp = hc.execute(get);
                if(rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    succeed=true;
                }
                final SharedPreferences prefs =
                        getActivity().getSharedPreferences(DemoActivity.class.getSimpleName(), Context.MODE_PRIVATE);
                String registrationId = prefs.getString(PROPERTY_REG_ID, "");
                String u="http://lewspage.hostei.com/scripts/edit.php?link="+registrationId;
                get = new HttpGet(u);
                System.out.println("URL "+u);
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
            if (succeed) {
                DemoActivity.mAppSectionsPagerAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(context, "Changes have been submitted successfully", Toast.LENGTH_SHORT);
                toast.show();
            }else{
                Toast toast= Toast.makeText(context, "Error!Please check your internet connection", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

        private void refresh(){
        reason=new HashMap<String,String>();
            if (CommonUtilities.keys!=null){
                for (int i=0;i<CommonUtilities.keys.size();i++)
                    reason.put(CommonUtilities.keys.get(i), "");
            }
        initialising=true;
        final MyProgressDialog pd=MyProgressDialog.show(CommonUtilities.context, "", "");
        for (int i=0;i<RGMap.size();i++)
            RGMap.get(i).clearCheck();
        final List<String> ints=new ArrayList<String>();
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
                                        for (int x=0;x<CommonUtilities.keys.size();x++){
                                            if(session.has(CommonUtilities.keys.get(x)))
                                                ints.add(session.getString(CommonUtilities.keys.get(x)));
                                            else
                                                ints.add("-1");
                                        }
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
                        for (int y=0;y<CommonUtilities.keys.size();y++){
                            if (ints.get(y)=="1")
                                RGMap.get(y).check(1);
                            else if (!ints.get(y).equals("-1")){
                                reason.put(CommonUtilities.keys.get(y),ints.get(y));
                                RGMap.get(y).check(0);
                            }
                        }
                        initialising=false;
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
