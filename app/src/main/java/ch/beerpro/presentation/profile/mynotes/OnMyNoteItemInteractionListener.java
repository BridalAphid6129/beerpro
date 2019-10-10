package ch.beerpro.presentation.profile.mynotes;

import android.widget.ImageView;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Note;

public interface OnMyNoteItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer beer);

    void onNoteClickedListener(Beer beer, Note note);

}
