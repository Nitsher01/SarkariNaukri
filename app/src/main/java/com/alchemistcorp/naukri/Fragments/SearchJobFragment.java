package com.alchemistcorp.naukri.Fragments;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.alchemistcorp.naukri.Adapter.JazzyListViewAdapter;
import com.alchemistcorp.naukri.AppController;
import com.alchemistcorp.naukri.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Nitin on 2/19/2017.
 */

public class SearchJobFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {
    private static final String VOLLEY_REQ_TAG = "LOAD_JOBS_FROM_JOBFRAGMENT";
    String[] titles , urls , descriptions ;
    JSONArray jobsArray;
    String searchString = "";
    String SEARCH_URL = "";
    @Bind(R.id.jazzyListSearchJob)
    JazzyListView jazzyListView;
    ProgressDialog progressDialog;
    public SearchJobFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search_jobs, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ButterKnife.bind(this,view);
        Bundle bundle = this.getArguments();
        if (bundle != null){
            searchString = bundle.getString("URL");
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Job Result");
        SEARCH_URL = searchString ;
        progressDialog = new ProgressDialog(getContext());
        //progressDialog.onStart();
        progressDialog.setMessage("Loading !!!");
        progressDialog.show();
        loadData();
        jazzyListView.setTransitionEffect(JazzyHelper.SLIDE_IN);
        return view;
    }

        private void loadData() {
            StringRequest request = new StringRequest(Request.Method.GET,
                    SEARCH_URL, this, this){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> headers= new HashMap<String, String>();
                    headers.put("Auth","alchemy");
                    return headers;
                }
            };
            request.setShouldCache(Boolean.FALSE);

            // AppController.getInstance().cancelPendingRequests(VOLLEY_REQ_TAG);
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(request,VOLLEY_REQ_TAG );
        }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.RIGHT, enter, 600);
        } else {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 600);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {

    }

    @Override
    public void onResponse(String s) {
        progressDialog.dismiss();
        if(s != ""){
            JSONObject jobData;
            try {
                //Log.d("jobs ",s);
                //jobData = XML.toJSONObject(jobs);
                jobData = new JSONObject(s);
                Log.d("jobData " , jobData.toString());
                //jobData = jobData.getJSONObject("rss");
                jobData = jobData.getJSONObject("result");
                jobsArray = jobData.getJSONArray("data");
                // this.titles = jobsArray.get
                Log.d("from searchJob ",jobsArray.toString());

            }catch(JSONException e){
                Log.d("error " ,"error aa rha hai ");
                e.printStackTrace();
            }
        }
        try {
            titles = generateArray("title");
            urls = generateArray("url");
            descriptions = generateArray("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (titles != null){
            jazzyListView.setAdapter(new JazzyListViewAdapter(getActivity(),titles ,urls ,descriptions));
        }
    }

    private String[] generateArray(String value) throws JSONException {
        String[] values = new String[jobsArray.length()];
        for (int i = 0; i < jobsArray.length(); i++) {
            JSONObject jsonObject = jobsArray.getJSONObject(i);
            if (value == "description")
                values[i] = jsonObject.getString(value).replace("Read more" , "").replace("\r\n","");
            else
                values[i] = jsonObject.getString(value);
        }
        return values;
    }
}
