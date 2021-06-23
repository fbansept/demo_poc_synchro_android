package edu.fbansept.demo.android.pocsynchro.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

import edu.fbansept.demo.android.pocsynchro.R;
import edu.fbansept.demo.android.pocsynchro.controller.IncidentController;
import edu.fbansept.demo.android.pocsynchro.model.Incident;

public class EditionIncidentActivity extends AppCompatActivity {

    EditText editTextTexte;
    TextView textViewCode;
    Incident incident;
    BottomAppBar bottomAppBar;

    FloatingActionButton fabEditionNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        setContentView(R.layout.activity_edition_incident);

        if (getIntent().hasExtra("incident")) {

            Serializable serializable = getIntent().getSerializableExtra("incident");

            Incident incident = (Incident) serializable;
            this.incident = incident;
        } else {
            this.incident = new Incident();
        }

        editTextTexte = findViewById(R.id.editText_texte);
        editTextTexte.setText(this.incident.getTexte());

        textViewCode = findViewById(R.id.textView_code);
        textViewCode.setText(this.incident.getCode());

        fabEditionNote = findViewById(R.id.fab_editionNote);

        fabEditionNote.setOnClickListener(v -> {

            incident.setTexte(editTextTexte.getText().toString());

            IncidentController.getInstance().sauvegardeLocal(
                    this,
                    incident,
                    incident -> startActivity(new Intent(this, ListeIncidentActivity.class)),
                    messageErreur -> Toast.makeText(this, messageErreur, Toast.LENGTH_LONG).show());
        });

        bottomAppBar = findViewById(R.id.bottomAppBar_EditionActivity);

        bottomAppBar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.menuItem_supprimerIncident) {

                //Flag l'incident comme devant être supprimé sur la base de donnée du serveur.
                incident.setaSupprimer(true);
                IncidentController.getInstance().sauvegardeLocal(this,incident, i -> {}, e -> {});
                startActivity(new Intent(this, ListeIncidentActivity.class));
            }

            return true;
        });
    }
}