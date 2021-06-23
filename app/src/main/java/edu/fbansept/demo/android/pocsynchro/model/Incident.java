package edu.fbansept.demo.android.pocsynchro.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

@Entity
public class Incident implements Serializable {

    @PrimaryKey
    @NonNull
    public String code;

    public int id;

    public String texte;

    //si cette valeur est vrai alors le serveur supprimera cet incident quand il le recevra
    public boolean aSupprimer;

    public Incident() {
        this.code = UUID.randomUUID().toString();
    }

    public Incident(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.code = jsonObject.getString("code");
        this.texte = jsonObject.getString("texte");

        if(jsonObject.has("supprime")) {
            this.aSupprimer = jsonObject.getBoolean("aSupprimer");
        } else {
            this.aSupprimer = false;
        }

    }

    public JSONObject toJson() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.id);
        jsonObject.put("code", this.code);
        jsonObject.put("texte", this.texte);
        jsonObject.put("aSupprimer", this.aSupprimer);

        return jsonObject;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public boolean isaSupprimer() {
        return aSupprimer;
    }

    public void setaSupprimer(boolean aSupprimer) {
        this.aSupprimer = aSupprimer;
    }


}