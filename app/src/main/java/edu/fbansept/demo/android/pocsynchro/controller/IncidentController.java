package edu.fbansept.demo.android.pocsynchro.controller;

import android.content.Context;

import androidx.room.Room;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import edu.fbansept.demo.android.pocsynchro.R;
import edu.fbansept.demo.android.pocsynchro.dao.BddLocale;
import edu.fbansept.demo.android.pocsynchro.model.Incident;
import edu.fbansept.demo.android.pocsynchro.utils.requestmanager.JsonArrayRequestWithToken;
import edu.fbansept.demo.android.pocsynchro.utils.requestmanager.RequestManager;

public final class IncidentController {

    private static IncidentController instance = null;

    private IncidentController() {
    }

    public static IncidentController getInstance() {

        if(instance == null) {
            instance = new IncidentController();
        }

        return instance;
    }

    public interface SuccesEcouteur {
        void onSuccesSauvegarde(Incident incident);
    }

    public interface ErreurEcouteur {
        void onErreurSauvegarde(String messageErreur);
    }

    public void sauvegardeLocal(
            Context context,
            Incident incident,
            SuccesEcouteur ecouteurSucces,
            ErreurEcouteur ecouteurErreur) {

        BddLocale bdd = Room.databaseBuilder(context,
                BddLocale.class, "ma-base-de-donnee").allowMainThreadQueries().build();

        Incident[] incidentBddLocal = bdd.userDao().findByCode(incident.getCode());

        if(incidentBddLocal.length > 0) {
            bdd.userDao().update(incident);
        } else {
            bdd.userDao().insertAll(incident);
        }

        ecouteurSucces.onSuccesSauvegarde(incident);
    }


    public interface SuccesSynchroEcouteur {
        void onSuccesSauvegarde(List<Incident> listeIncident);
    }


    public void synchronisation(
            Context context,
            SuccesSynchroEcouteur ecouteurSucces,
            ErreurEcouteur ecouteurErreur) throws JSONException {

        BddLocale bdd = Room.databaseBuilder(context,
                BddLocale.class, "ma-base-de-donnee").allowMainThreadQueries().build();

        List<Incident> incidentBddLocal = bdd.userDao().getAll();

        JSONArray jsonArrayIncident = new JSONArray();

        for(Incident incident : incidentBddLocal){
            jsonArrayIncident.put(incident.toJson());
        }

        JsonArrayRequestWithToken stringRequest = new JsonArrayRequestWithToken(
                context,
                Request.Method.POST,
                context.getResources().getString(R.string.server_ip) + "user/synchronisation",
                null,
                listeIncidentJson -> {
                    try {
                        Incident[] tableauIncident = new Incident[listeIncidentJson.length()];

                        for (int i = 0; i < listeIncidentJson.length() ; i++) {
                            tableauIncident[i] = new Incident(listeIncidentJson.getJSONObject(i));
                        }

                        bdd.userDao().clear();
                        bdd.userDao().insertAll(tableauIncident);

                        ecouteurSucces.onSuccesSauvegarde(Arrays.asList(tableauIncident));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ecouteurErreur.onErreurSauvegarde("Impossible de sauvegarder");
                    }
                },
                e -> {
                    e.printStackTrace();
                    ecouteurErreur.onErreurSauvegarde("Impossible de sauvegarder");
                }
        ){

            @Override
            public byte[] getBody() {

                return jsonArrayIncident.toString().getBytes(StandardCharsets.UTF_8);

            }
        };

        RequestManager.getInstance(context).addToRequestQueue(stringRequest);

    }
}
