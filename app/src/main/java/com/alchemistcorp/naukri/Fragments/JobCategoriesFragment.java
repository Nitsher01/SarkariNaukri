package com.alchemistcorp.naukri.Fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.alchemistcorp.naukri.R;
import com.alchemistcorp.naukri.Utils.CommonFunctions;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Nitin on 2/19/2017.
 */

public class JobCategoriesFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.ivAirForceJobs)
    ImageView ivAirForceJobs;
    @Bind(R.id.ivArmyJobs)
    ImageView ivArmyJobs;
    @Bind(R.id.ivBankJobs)
    ImageView ivBankJobs;
    @Bind(R.id.ivRailwayJobs)
    ImageView ivRailwayJobs;
    @Bind(R.id.ivTeachingJobs)
    ImageView ivTeachingJobs;
    @Bind(R.id.ivUPSCJobs)
    ImageView ivUPSCJobs;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_job_categories, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ButterKnife.bind(this,view);
        ivAirForceJobs.setOnClickListener(this);
        ivArmyJobs.setOnClickListener(this);
        ivBankJobs.setOnClickListener(this);
        ivRailwayJobs.setOnClickListener(this);
        ivTeachingJobs.setOnClickListener(this);
        ivUPSCJobs.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        String URL = "";
        switch (view.getId()){
            case R.id.ivAirForceJobs:
                URL = getActivity().getApplicationContext().getString(R.string.JOB_CATEGORIES) + "Indian%20air%20force";
                break;
            case R.id.ivArmyJobs:
                URL = getActivity().getApplicationContext().getString(R.string.JOB_CATEGORIES) + "Indian%20Army";
                break;
            case R.id.ivBankJobs:
                URL = getActivity().getApplicationContext().getString(R.string.JOB_CATEGORIES) + "Bank%20Jobs";
                break;
            case R.id.ivRailwayJobs:
                URL = getActivity().getApplicationContext().getString(R.string.JOB_CATEGORIES) + "Railway%20jobs";
                break;
            case R.id.ivTeachingJobs:
                URL = getActivity().getApplicationContext().getString(R.string.JOB_CATEGORIES) + "Teaching";
                break;
            case R.id.ivUPSCJobs:
                URL = getActivity().getApplicationContext().getString(R.string.JOB_CATEGORIES) + "UPSC";
                break;
        }
        if (URL.length() != 0 && CommonFunctions.isNetworkAvailable(getContext())){
            SearchJobFragment searchJobFragment = new SearchJobFragment();
            Bundle bundle = new Bundle();
            bundle.putString("URL", URL);
            searchJobFragment.setArguments(bundle);
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, searchJobFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
