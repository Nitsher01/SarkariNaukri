package com.alchemistcorp.naukri.Fragments;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

import com.alchemistcorp.naukri.Adapter.JazzyListViewAdapter;
import com.alchemistcorp.naukri.R;
import com.alchemistcorp.naukri.Utils.CommonFunctions;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.twotoasters.jazzylistview.JazzyEffect;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.alchemistcorp.naukri.AppController.TAG;

/**
 * Created by Nitin on 2/18/2017.
 */

public class JobsHomeFragment extends android.support.v4.app.Fragment {
    String[] titles , urls , descriptions ;
    JSONArray jobsArray;
    @Bind(R.id.jazzyList)
    JazzyListView jazzyListView;
    @Bind(R.id.etSearchJob)
    EditText etSearchJob;
    @OnClick(R.id.btSearchJobs)
    public void click(){
        if (CommonFunctions.isNetworkAvailable(getContext()) && etSearchJob.getText().length() != 0) {
            String searchString = etSearchJob.getText().toString().replace(" ", "%");
            String SEARCH_URL = getActivity().getApplicationContext().getString(R.string.SEARCH_URL) + searchString ;
            Log.d("search string" , etSearchJob.getText().toString());
            SearchJobFragment searchJobFragment = new SearchJobFragment();
            Bundle bundle = new Bundle();
            bundle.putString("URL", SEARCH_URL);
            searchJobFragment.setArguments(bundle);
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, searchJobFragment)
                    .addToBackStack(null)
                    .commit();
        }else if (etSearchJob.getText().length() == 0){
            Toast.makeText(getActivity().getApplicationContext(), "Enter text to search", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getActivity().getApplicationContext(), "Sarkari Naukri app needs an active internet connection.", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btJobCategories)
    public void Click(){
        this.getFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, new JobCategoriesFragment())
                .addToBackStack(null)
                .commit();
    }
    private int mCurrentTransitionEffect = JazzyHelper.HELIX;
    public JobsHomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_jobs_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ButterKnife.bind(this,view);
        loadData();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Job List");
        jazzyListView.setTransitionEffect(JazzyHelper.SLIDE_IN);
        try {
            titles = generateArray("title");
            urls = generateArray("url");
            descriptions = generateArray("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jazzyListView.setAdapter(new JazzyListViewAdapter(getActivity(),titles ,urls ,descriptions));
        return view;
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

    private void loadData() {
        {
            String jobs =  PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("jobs_raw","");
            Log.d(TAG, jobs);
            if(jobs.length()==0){

            }
            JSONObject jobData;
            try {
                Log.d("jobs ",jobs);
                //jobData = XML.toJSONObject(jobs);
                jobData = new JSONObject(jobs);
                Log.d("jobData " , jobData.toString());
                //jobData = jobData.getJSONObject("rss");
                jobData = jobData.getJSONObject("result");
                jobsArray = jobData.getJSONArray("data");
               // this.titles = jobsArray.get
                Log.d("horoscopeArray ",jobsArray.toString());

            }catch(JSONException e){
                Log.d("error " ,"error aa rha hai ");
                e.printStackTrace();
            }

        }
    }

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
    public void onDetach() {
        super.onDetach();
    }

    private void setupJazziness(int effect) {
        mCurrentTransitionEffect = effect;
        jazzyListView.setTransitionEffect(mCurrentTransitionEffect);
    }
}
