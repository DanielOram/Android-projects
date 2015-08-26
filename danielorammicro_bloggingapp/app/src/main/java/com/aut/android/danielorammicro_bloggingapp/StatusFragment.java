package com.aut.android.danielorammicro_bloggingapp;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.aut.android.danielorammicro_bloggingapp.R;
import com.marakana.android.yamba.clientlib.YambaClient;

import android.widget.Toast;

/**
 * Created by cwc8902 on 13/08/2015.
 */
public class StatusFragment extends Fragment implements View.OnClickListener,LocationListener {

    private static final int MAX_LENGTH = 140;
    private static final String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    private TextView statusText, counterText;
    private ImageButton updateButton;
    private int defaultColor;
    private LocationManager locationManager;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.status,container, false);

        //Find the views
        statusText = (TextView)view.findViewById(R.id.status_update_text);
        statusText.addTextChangedListener(new MyTextWatcher());
        counterText = (TextView) view.findViewById(R.id.status_update_counter_text);
        defaultColor = counterText.getTextColors().getDefaultColor();
        updateButton = (ImageButton) view.findViewById(R.id.status_update_button);
        updateButton.setOnClickListener(this);

        //Get Location
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LOCATION_PROVIDER);

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        locationManager.requestLocationUpdates(LOCATION_PROVIDER, 10000,100,this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    //Called when updatebutton is clicked on.
    public void onClick(View v){
        final String status = statusText.getText().toString();

        new StatusUpdateTask().execute(status);
    }

    //Posts the status update in a separate task
    class StatusUpdateTask extends AsyncTask<String,Void,String>{
        Dialog dialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            dialog = new Dialog(getActivity());
            dialog.setTitle("Posting status update...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String username = prefs.getString("username", null);
                String password = prefs.getString("password",null);
                String server = prefs.getString("server",null);
                YambaClient yambaClient = new YambaClient(username,password);
                if (server != null && server.length() > 0) yambaClient.setApiRoot(server);
                if (location != null){
                    yambaClient.updateStatus(params[0], location.getLatitude(),location.getLongitude()); //could take awhile

                } else {
                    yambaClient.updateStatus(params[0]);
                }

                //Send broadcast that there may be new data on the server
                getActivity().sendBroadcast(new Intent("com.marakana.android.yamba.REFRESH_ACTION"));
                return "Status update posted successfully";
            } catch (Exception e){
                Log.e("StatusActivity","CRASHED!",e);
                e.printStackTrace();
                return "Failed to post the status update";
            }
        }

        //Called once doInBackground is complete
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            dialog.dismiss();
            Toast.makeText(getActivity(),result,Toast.LENGTH_LONG).show();
        }
    }

    class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s){
            int count = MAX_LENGTH - s.length();
            counterText.setText(Integer.toString(count));
            //change the colour
            if(count < 30){
                counterText.setText(Integer.toString(count));
                counterText.setTextScaleX(2);
            } else {
                counterText.setTextColor(defaultColor);
                counterText.setTextScaleX(1);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
