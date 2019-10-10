package ch.beerpro.data.repositories;

import android.util.Pair;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;


import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.Note;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class NotesRepository {

    private static LiveData<List<Note>> getMyNotesByUser(String userId){
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance()
                .collection(Note.COLLECTION)
                .whereEqualTo(Note.FIELD_USER_ID, userId),
                Note.class);
    }

    private static LiveData<Note> getUserNoteFor(Pair<String, Beer> input){
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(Note.COLLECTION)
        .document(Note.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, Note.class);
    }

    public Task<Void> setUserNote( String beerId, String note, String userId, Date creationDate){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String noteId = Note.generateId(userId, beerId);
        DocumentReference noteQuery = db.collection(Note.COLLECTION).document(noteId);

        return noteQuery.get().continueWithTask(task -> {
            if(Objects.equals(note, "")) {
                return noteQuery.delete();
            } else if (task.isSuccessful()) {
                return noteQuery.set(new Note(beerId, note, userId, creationDate));
            } else {
              throw task.getException();
            }
        });
    }

    public LiveData<List<Pair<Note, Beer>>> getMyNoteWithBeer(LiveData<String> currentUserId, LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyNotes(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<Note> notes = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<Note, Beer>> result = new ArrayList<>();
            for (Note note : notes){
                Beer beer = beersById.get(note.getBeerId());
                result.add(Pair.create(note, beer));

            }
            return result;
        });
    }

    private LiveData<List<Note>> getMyNotes(LiveData<String> currentUserId) {
        return switchMap(currentUserId, NotesRepository::getMyNotesByUser);
    }

    public LiveData<Note> getNotesForBeer(LiveData<String> currentUserId, LiveData<Beer> beer){
        return switchMap(combineLatest(currentUserId,beer), NotesRepository::getUserNoteFor);
    }

}
