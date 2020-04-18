package com.example.splash2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrarEmpresa<mapFragment> extends AppCompatActivity implements OnMapReadyCallback {

 private static final int PETICION_PERMISO_LOCALIZACION = 110;

    GoogleMap Map;
    private EditText name;
    private EditText telefono, correo;
    private EditText latitud, longitud;
    private String Latid , Longi;
    private Button bton_regist, bton_atras;
    private ProgressBar loading;
    private static final String URL_REGIST = "http://192.168.100.37:8090/volley/RegistrarEmpresa.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_empresa);

        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        telefono = findViewById(R.id.telefono);
        correo = findViewById(R.id.correo);
        latitud = findViewById(R.id.latitud);
        longitud = findViewById(R.id.longitud);
        bton_regist = findViewById(R.id.bton_regist);
        bton_atras = findViewById(R.id.bton_atras);
        try {
            Bundle b = this.getIntent().getExtras();
            Toast.makeText(this, "Agregue su Ubicacion  ".toString(), Toast.LENGTH_SHORT).show();

            //Toast.makeText(getApplicationContext(), b.getString("id"), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
        }

        bton_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Regist();
            }
        });
        bton_atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  startActivity(new Intent(activity_registrar_empresa.this,registra_empresa.class));
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.maps);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        Map = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            //firebase

            //mMap.moveCamera(new LatLng(-17.444,-63.444));

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Map.setMyLocationEnabled(true);   //sirve para agregar el boton de buscar mi ubicacion
        //Map.getUiSettings().setMyLocationButtonEnabled(true); para agregar llevar al maps del telefono
        Map.getUiSettings().setZoomControlsEnabled(true);  //sirve para agregar botones de Zonn  de + y -
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);


        Criteria criteria = new Criteria();

        try {
            assert locationManager != null;
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16));
        }catch (Exception e){
            final LatLng Vallegrande = new LatLng(-18.4894001, -64.1081091);
            Map.moveCamera(CameraUpdateFactory.newLatLngZoom(Vallegrande,16));

        }
        Map.getMyLocation();
        Map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Map.clear();
                Map.addMarker(new MarkerOptions().position(latLng).draggable(true).title("su ubicacion").snippet("De la cuidad de Vallegrande"));

            }
        });

        Map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
            mensaje(marker);
            }

        });


       // LatLng(-18.4894001, -64.1081091),16)



    }
    private void mensaje(Marker marker){

        Latid  = String.valueOf(marker.getPosition().latitude);
        Longi = String.valueOf(marker.getPosition().longitude);
        latitud.setText(Latid);
        longitud.setText(Longi);

        //Toast.makeText(this,marker.getPosition().latitude().toString(),Toast.LENGTH_SHORT).show();

    }
    private void Regist() {
        loading.setVisibility(View.VISIBLE);
        bton_regist.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String telefono = this.telefono.getText().toString().trim();
        final String correo = this.correo.getText().toString().trim();
        final String latitud = Latid.trim();
        final String longitud = Longi.trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(RegistrarEmpresa.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegistrarEmpresa.this, "Register Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            bton_regist.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrarEmpresa.this, "Register Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        // Log.i("Error",error.toString());
                        loading.setVisibility(View.GONE);
                        bton_regist.setVisibility(View.VISIBLE);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", name);
                params.put("telefono", telefono);
                params.put("correo", correo);
                params.put("latitud", latitud);
                params.put("longitud", longitud);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        loading.setVisibility(View.GONE);
        bton_regist.setVisibility(View.VISIBLE);







    }
}

