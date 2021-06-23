package edu.fbansept.demo.android.pocsynchro.controller;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.fbansept.demo.android.pocsynchro.R;
import edu.fbansept.demo.android.pocsynchro.utils.requestmanager.RequestManager;

public class ConnexionController {

    private static ConnexionController instance = null;

    private ConnexionController() {
        super();
    }

    public static ConnexionController getInstance() {
        if(instance == null) {
            instance = new ConnexionController();
        }

        return instance;
    }

    public interface SuccesEcouteur {
        void onSuccesConnexion();
    }

    public interface ErreurEcouteur {
        void onErreurConnexion(String messageErreur);
    }

    public void connexion(
            Context context,
            String login,
            String password,
            SuccesEcouteur ecouteurSucces,
            ErreurEcouteur ecouteurErreur
    ){
        StringRequest stringRequest = new StringRequest

                (Request.Method.POST,  context.getResources().getString(R.string.server_ip)+ "authentification" ,
                        token -> {
                            SharedPreferences preference = context.getSharedPreferences(
                                    context.getResources().getString(R.string.fichier_preference), 0); // 0 - for private mode
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString("token", token); // Storing string
                            editor.apply();

                            ecouteurSucces.onSuccesConnexion();
                        },
                        error -> {
                             ecouteurErreur.onErreurConnexion("Impossible de se connecter");
                        }
                ){

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    //Attntion si code de dale : jsonBody.put("username", login);
                    jsonBody.put("pseudo", login);
                    jsonBody.put("motDePasse", password);

                    return jsonBody.toString().getBytes(StandardCharsets.UTF_8);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };

        RequestManager.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void deconnexion(
            Context context,
            SuccesEcouteur ecouteurSucces
    ){
        SharedPreferences preference = context.getSharedPreferences(
                context.getResources().getString(R.string.fichier_preference), 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.remove("token"); // Storing string
        editor.apply();
        ecouteurSucces.onSuccesConnexion();
    }
}
