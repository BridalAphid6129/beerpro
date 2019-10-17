package ch.beerpro.presentation.profile.mynotes;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;

import ch.beerpro.data.repositories.BeersRepository;
import ch.beerpro.data.repositories.CurrentUser;
import ch.beerpro.data.repositories.NotesRepository;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Note;

public class MyNotesViewModel extends ViewModel implements CurrentUser {

    private static final String TAG ="MyNotesViewModel";
    private final MutableLiveData<String> currentUserId = new MutableLiveData<>();
    private final NotesRepository notesRepository;
    private final BeersRepository beersRepository;

    public MyNotesViewModel() {
        notesRepository = new NotesRepository();
        beersRepository = new BeersRepository();
        currentUserId.setValue(getCurrentUser().getUid());

    }

    public LiveData<List<Pair<Note, Beer>>> getMyNotes(){
        return notesRepository.getMyNoteWithBeer(currentUserId,beersRepository.getAllBeers());
    }

    public Task<Void> setUserNotes(String beerId, String note){
        return notesRepository.setUserNote(beerId, note, getCurrentUser().getUid(), new Date());
    }

}
