package com.alchemistcorp.naukri.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alchemistcorp.naukri.AppController;
import com.alchemistcorp.naukri.R;
import com.alchemistcorp.naukri.Utility;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.alchemistcorp.naukri.AppController.TAG;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashScreenFragment extends Fragment implements Response.ErrorListener, Response.Listener<String> {
    private static final String VOLLEY_REQ_TAG = "LOAD_HOROSCOPE_FROM_ACTIVITY";
    private  String URL = "";
    @Bind(R.id.circle_loading_view)
    AnimatedCircleLoadingView animatedCircleLoadingView;
    @Bind(R.id.loadingMessage)
    TextView loadingMessage;
    public SplashScreenFragment() {

    }

    @OnClick(R.id.circle_loading_view)
    public void onMessageClick(){
        if(loadingMessage.getText().equals("No internet connection. Tap to retry.")){
            loadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_splash_screen, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Utility.setFont(loadingMessage, getActivity().getAssets());
        URL = getActivity().getApplicationContext().getString(R.string.URL);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        int day = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).getInt("day",-1);
        if(isNetworkAvailable()){
            loadData();
        }
        else{
            openApp();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void openApp() {

        String jobs =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("jobs_raw","");
        if(jobs.length()==0){
            onErrorResponse(new VolleyError());
            return;
        }
        animatedCircleLoadingView.resetLoading();
        animatedCircleLoadingView.startIndeterminate();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new JobsHomeFragment())
                .commit();
    }

    private void loadData() {
        loadingMessage.setText(getString(R.string.loadingMessage));
        animatedCircleLoadingView.resetLoading();
        animatedCircleLoadingView.startIndeterminate();
        StringRequest request = new StringRequest(Request.Method.GET,
               URL, this, this){
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
    public void onErrorResponse(VolleyError volleyError) {
        animatedCircleLoadingView.stopFailure();
        loadingMessage.setText("No internet connection. Tap to retry.");
        Toast.makeText(getActivity().getApplicationContext(), "Sarkari Naukri app needs an active internet connection.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String s) {
        animatedCircleLoadingView.stopOk();
        Log.d("response",s);
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit()
                .putString("jobs_raw",s)
                .putInt("date",Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
                .apply();
        openApp();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
