package com.alchemistcorp.myhoroscope.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Field;
import com.alchemistcorp.myhoroscope.R;
import com.labo.kaji.fragmentanimations.FlipAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListZodiacsFragment extends Fragment {


    public ListZodiacsFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.aries)
    public void clickAries(){
        openDetails(1);
    }

    @OnClick(R.id.taurus)
    public void clickTaurus(){
        openDetails(2);
    }

    @OnClick(R.id.gemini)
    public void clickGemini(){
        openDetails(3);
    }

    @OnClick(R.id.cancer)
    public void clickCancer(){
        openDetails(4);
    }

    @OnClick(R.id.leo)
    public void clickLeo(){
        openDetails(5);
    }

    @OnClick(R.id.virgo)
    public void clickVirgo(){
        openDetails(6);
    }

    @OnClick(R.id.libra)
    public void clickLibra(){
        openDetails(7);
    }

    @OnClick(R.id.scorpio)
    public void clickScorpio(){
        openDetails(8);
    }

    @OnClick(R.id.sagittarius)
    public void clickSagittarius(){
        openDetails(9);
    }

    @OnClick(R.id.capricorn)
    public void clickCapricorn(){
        openDetails(10);
    }

    @OnClick(R.id.aquarius)
    public void clickAquarius(){
        openDetails(11);
    }

    @OnClick(R.id.pisces)
    public void clickPisces(){
        openDetails(12);
    }

    private void openDetails(int payload){
        Bundle bundle = new Bundle();
        bundle.putInt("zodiac",payload);
        Fragment fragment = new HoroscopeLoadFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout,fragment,"nafrat")
                .addToBackStack(null)
        .commit();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_list_zodiacs, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ButterKnife.bind(this,view);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("My Daily Horoscope");
        return view;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share){
            Intent shareIntent =
                    new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "My Daily Horoscopes");
            String shareMessage = "My Daily Horoscopes"+"\n\n"+
                    " Download the FREE App 'My Daily Horoscopes' and predict your future by your Zodiacs."+
                    " \n Notification every morining with Horoscope for the Day." +
                    "\n Internet Connection needed only once a day.\n"+
                    "http://play.google.com/store/apps/details?id=com.alchemistcorp.myhoroscope";
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                    shareMessage);

            startActivity(Intent.createChooser(shareIntent,
                    "Share via..."));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
