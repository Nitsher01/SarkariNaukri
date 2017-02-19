package com.alchemistcorp.naukri.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alchemistcorp.naukri.R;
import com.alchemistcorp.naukri.Utility;
import com.labo.kaji.fragmentanimations.FlipAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HoroscopeLoadFragment extends Fragment {
    JSONArray horoscopeArray = null;
    public HoroscopeLoadFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.horoscope)
    TextView textView;

    @Bind(R.id.horoscopeImage)
    ImageView imageView;

    @Bind(R.id.horoscopeTitle)
    TextView heading;

    @Bind(R.id.date)
    TextView date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parseAndLoadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_horoscope_load, container, false);
        ButterKnife.bind(this,view);
        int zodiac = getArguments().getInt("zodiac",-1)-1;
        //Utility.setFont(textView,getActivity().getAssets());
        try {
            String raw_data = horoscopeArray.getJSONObject(zodiac).getString("description");
            System.out.println("Raw data "+horoscopeArray.toString());
            Log.d("horoscopeArray ",horoscopeArray.toString());
            int index = raw_data.indexOf("More horoscopes!");
            raw_data  = index>=0 ? raw_data.substring(0,index) : raw_data;
            textView.setText(Html.fromHtml(raw_data));
        }catch (JSONException e){
            e.printStackTrace();
        }
        imageView.setImageResource((int)getImageResIdAndTitle(zodiac)[0]);
        heading.setText((String)getImageResIdAndTitle(zodiac)[1]);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle((String)getImageResIdAndTitle(zodiac)[1]);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(Calendar.getInstance().getTime());
        date.setText("( "+formattedDate+" )");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(FlipAnimation.RIGHT, enter, 600);
        } else {
            return FlipAnimation.create(FlipAnimation.LEFT, enter, 600);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void parseAndLoadData(){
       String horoscope =  PreferenceManager.getDefaultSharedPreferences(getContext()).getString("horoscope_raw","");
        JSONObject horoscopeData;
        try {
            horoscopeData = XML.toJSONObject(horoscope);
            //horoscopeData = horoscopeData.getJSONObject("rss");
            horoscopeData = horoscopeData.getJSONObject("result");
            horoscopeArray = horoscopeData.getJSONArray("data");
            Log.d("horoscopeArray ",horoscopeArray.toString());
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    private Object[] getImageResIdAndTitle(int zodiac){
        switch(zodiac){
            case 0: return new Object[]{R.drawable.aries, "Aries"};
            case 1: return new Object[]{R.drawable.taurus, "Taurus"};
            case 2: return new Object[]{R.drawable.gemini,"Gemini"};
            case 3: return new Object[]{R.drawable.scorpio,"Cancer"};
            case 4: return new Object[]{R.drawable.leo, "Leo"};
            case 5: return new Object[]{R.drawable.virgo, "Virgo"};
            case 6: return new Object[]{R.drawable.libra, "Libra"};
            case 7: return new Object[]{R.drawable.cancer, "Scorpio"};
            case 8: return new Object[]{R.drawable.sagi, "Sagittarius"};
            case 9: return new Object[]{R.drawable.capricorn, "Capricorn"};
            case 10: return new Object[]{R.drawable.aquarius, "Aquarius"};
            case 11: return new Object[]{R.drawable.pisces, "Pisces"};
        }
        return null;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.menu_item_share);
        item.setVisible(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share){
            Intent shareIntent =
                    new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "Today's Horoscope for " + heading.getText());
            String shareMessage = "Today's Horoscope for " + heading.getText()+"\n\n"+textView.getText()+
                    "\n Download the FREE App 'My Daily Horoscopes' Now: http://play.google.com/store/apps/details?id=com.alchemistcorp.myhoroscope";
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    shareMessage);
            startActivity(Intent.createChooser(shareIntent,
                    "Share via..."));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
