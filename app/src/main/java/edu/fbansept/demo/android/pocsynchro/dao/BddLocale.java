package edu.fbansept.demo.android.pocsynchro.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import edu.fbansept.demo.android.pocsynchro.model.Incident;

@Database(entities = {Incident.class}, version = 1)
public abstract class BddLocale extends RoomDatabase {
    public abstract IncidentDao userDao();
}