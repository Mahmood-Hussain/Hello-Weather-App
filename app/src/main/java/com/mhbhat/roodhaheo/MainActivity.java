package com.mhbhat.roodhaheo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText city;
    Button myBtn;
    TextView result;
    TextView temp;
    TextView lon;
    TextView lat;
    ImageView sunny;
    ImageView partlysunny;
    ImageView cloudy;
    ImageView rainy;
    ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (EditText)findViewById(R.id.city);
        myBtn = (Button)findViewById(R.id.button);
        result = (TextView)findViewById(R.id.result);
        temp = (TextView)findViewById(R.id.temp);
        lon = (TextView)findViewById(R.id.lon);
        lat = (TextView)findViewById(R.id.lat);
        sunny = (ImageView)findViewById(R.id.sunny);
        partlysunny = (ImageView)findViewById(R.id.partlysunny);
        cloudy = (ImageView)findViewById(R.id.cloudy);
        rainy = (ImageView)findViewById(R.id.rainy);
        mprogressBar = (ProgressBar)findViewById(R.id.progressBar);


//        https://api.openweathermap.org/data/2.5/weather?q=gurugram&appid=dea4b2b7853f3f59ed7f328d2454d7df

        final String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=";
       final String API = "&appid=dea4b2b7853f3f59ed7f328d2454d7df";
        sunny.setAlpha(0f);
        partlysunny.setAlpha(0f);
        cloudy.setAlpha(0f);
        rainy.setAlpha(0f);
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().equals("")){
                    myBtn.setEnabled(false);
                } else {
                    myBtn.setEnabled(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    myBtn.setEnabled(false);
                } else {
                    myBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                    myBtn.setEnabled(false);
                } else {
                    myBtn.setEnabled(true);
                }
            }
        });
        mprogressBar.setVisibility(View.INVISIBLE);

        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressBar.setVisibility(View.VISIBLE);
                final String myURL = baseURL + city.getText().toString() + API;
                Log.i("url", "url" + myURL);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myURL, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    mprogressBar.setVisibility(View.INVISIBLE);
                                    String weather = response.getString("weather");
                                    if (weather.equals("")){
                                       Toast toast = Toast.makeText(getApplicationContext(), "Can't fetch right now", Toast.LENGTH_SHORT);
                                       toast.show();
                                    }
                                    String coord = response.getString("coord");
                                    JSONObject cor = new JSONObject(coord);
                                    String lonVal = cor.getString("lon");
                                    String latVal = cor.getString("lat");
                                    lon.setText("Lon: "+lonVal);
                                    lat.setText("Lat: "+latVal);

                                    JSONArray weatherArr = new JSONArray(weather);

                                    for (int i = 0; i < weatherArr.length(); i++){
                                        JSONObject weaJSON = weatherArr.getJSONObject(i);

                                        String desc = weaJSON.getString("description");
                                        Log.i("desc", "Description: "+desc);
                                        if (desc.equals("haze") || desc.equals("mist")|| desc.equals("clear")|| desc.equals("smoke")|| desc.equals("clear sky")){
                                            sunny.setAlpha(1f);
                                            partlysunny.setAlpha(0f);
                                            cloudy.setAlpha(0f);
                                            rainy.setAlpha(0f);
                                            result.setText(desc);
                                            Log.i("imloop", "Im loop");
                                        }else if (desc.equals("few clouds") || desc.equals("clouds") || desc.equals("most clouds") || desc.equals("overcast clouds")){
                                            sunny.setAlpha(0f);
                                            partlysunny.setAlpha(0.5f);
                                            cloudy.setAlpha(0f);
                                            rainy.setAlpha(0f);
                                            result.setText(desc);
                                        }else if (desc.equals("drizzling") || desc.equals("cloud")){
                                            sunny.setAlpha(0f);
                                            partlysunny.setAlpha(0f);
                                            cloudy.setAlpha(1f);
                                            rainy.setAlpha(0f);
                                            result.setText(desc);
                                        }else if (desc.equals("moderate rain") || desc.equals("rain") || desc.equals("light rain")|| desc.equals("heavy intensity rain")){
                                            sunny.setAlpha(0f);
                                            partlysunny.setAlpha(0f);
                                            cloudy.setAlpha(0f);
                                            rainy.setAlpha(1f);
                                            result.setText(desc);
                                        }else{
                                            result.setText(desc);
                                        }
                                    }

                                    String main = response.getString("main");
                                    JSONObject mainJSON = new JSONObject(main);
//                                    Log.i("main", "mainly is "+main);

                                    int myTemp = mainJSON.getInt("temp");
                                    int tempInCel = myTemp - 273;
                                    temp.setText(Integer.toString(tempInCel)+ " Degree Celcius");
//                                    Log.i("temp", "Temperature: "+temp);

                                } catch (JSONException e) {
                                    Log.i("error", "Error! "+e);
                                    e.printStackTrace();
                                    Toast toast =  Toast.makeText(getApplicationContext(), "Something is not right", Toast.LENGTH_SHORT);
                                    toast.show();
                                    mprogressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               Toast toast =  Toast.makeText(getApplicationContext(), "Something is not right", Toast.LENGTH_SHORT);
                               toast.show();
                                result.setText("Please check your input");
                                temp.setText("");
                                lon.setText("");
                                lat.setText("");
                                sunny.setAlpha(0f);
                                partlysunny.setAlpha(0f);
                                cloudy.setAlpha(0f);
                                rainy.setAlpha(0f);
                                mprogressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                );
                Singleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}
