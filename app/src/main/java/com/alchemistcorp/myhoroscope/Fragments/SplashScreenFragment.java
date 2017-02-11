package com.alchemistcorp.myhoroscope.Fragments;

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

import com.alchemistcorp.myhoroscope.AppController;
import com.alchemistcorp.myhoroscope.R;
import com.alchemistcorp.myhoroscope.Utility;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        if(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == day){
            openApp();
        }
        else{
            loadData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void openApp() {
        animatedCircleLoadingView.resetLoading();
        animatedCircleLoadingView.startIndeterminate();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, new ListZodiacsFragment())
                .commit();
    }

    private void loadData() {
        loadingMessage.setText(getString(R.string.loadingMessage));
        animatedCircleLoadingView.resetLoading();
        animatedCircleLoadingView.startIndeterminate();
        StringRequest request = new StringRequest(Request.Method.GET,
               URL, this, this);
        request.setShouldCache(Boolean.FALSE);
        // AppController.getInstance().cancelPendingRequests(VOLLEY_REQ_TAG);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(request,VOLLEY_REQ_TAG );
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        animatedCircleLoadingView.stopFailure();
        loadingMessage.setText("No internet connection. Tap to retry.");
        Toast.makeText(getActivity().getApplicationContext(), "Horoscopes need internet connection.", Toast.LENGTH_LONG);
    }

    @Override
    public void onResponse(String s) {
        animatedCircleLoadingView.stopOk();
        Log.d("response",s);
        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit()
                .putString("horoscope_raw",s)
                .putInt("date",Calendar.getInstance().get(Calendar.DAY_OF_YEAR))
                .apply();
        openApp();
    }
}
