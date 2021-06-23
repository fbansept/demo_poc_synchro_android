package edu.fbansept.demo.android.pocsynchro.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.List;

import edu.fbansept.demo.android.pocsynchro.R;
import edu.fbansept.demo.android.pocsynchro.controller.IncidentController;
import edu.fbansept.demo.android.pocsynchro.dao.BddLocale;
import edu.fbansept.demo.android.pocsynchro.model.Incident;
import edu.fbansept.demo.android.pocsynchro.view.adapter.ListeIncidentAdapter;

public class ListeIncidentActivity extends AppCompatActivity {

    private FloatingActionButton buttonAjouterNote;
    private RecyclerView recyclerViewListeNote;
    BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        setContentView(R.layout.activity_liste_incident);

        recyclerViewListeNote = findViewById(R.id.liste_note);
        recyclerViewListeNote.setLayoutManager(new LinearLayoutManager(this));


        BddLocale bdd = Room.databaseBuilder(this,
                BddLocale.class, "ma-base-de-donnee").allowMainThreadQueries().build();

        List<Incident> incidentBddLocal = bdd.userDao().getAllNonSupprime();

        recyclerViewListeNote.setAdapter(
                new ListeIncidentAdapter(incidentBddLocal,
                        incidentClic -> {
                            Intent intent = new Intent(this, EditionIncidentActivity.class);
                            intent.putExtra("incident", incidentClic);
                            startActivity(intent);
                        }));


        this.buttonAjouterNote = findViewById(R.id.button_ajouterNote);

        this.buttonAjouterNote.setOnClickListener((View v) -> {
            startActivity(new Intent(this, EditionIncidentActivity.class));
        });

        bottomAppBar = findViewById(R.id.bottomAppBar_listeIncidentActivity);

        bottomAppBar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.menuItem_synchronisation) {
                try {
                    IncidentController.getInstance().synchronisation(
                            this,
                            listeIncident -> {
                                ((ListeIncidentAdapter)recyclerViewListeNote.getAdapter()).changeData(listeIncident);
                            },
                            e -> { }
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return true;
            }

            return false;
        });
    }
}