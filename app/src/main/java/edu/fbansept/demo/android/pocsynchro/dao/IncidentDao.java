package edu.fbansept.demo.android.pocsynchro.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.fbansept.demo.android.pocsynchro.model.Incident;

@Dao
public interface IncidentDao {

    @Query("SELECT * FROM Incident")
    List<Incident> getAll();

    @Query("SELECT * FROM Incident WHERE aSupprimer != 1")
    List<Incident> getAllNonSupprime();

    @Query("SELECT * FROM Incident WHERE code = :code")
    Incident[] findByCode(String code);

    @Query("DELETE FROM Incident")
    public void clear();

    @Insert
    void insertAll(Incident... incidents);

    @Delete
    void delete(Incident incident);

    @Update
    void update(Incident incident);
}