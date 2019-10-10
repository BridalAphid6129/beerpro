package ch.beerpro.presentation.profile.mynotes;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import ch.beerpro.GlideApp;
import ch.beerpro.R;
import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Note;
import ch.beerpro.presentation.utils.EntityPairDiffItemCallback;

public class MyNotesRecyclerViewAdapter extends ListAdapter<Pair<Note, Beer>, MyNotesRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyNoteRecyclerViewAdapter";
    private static final DiffUtil.ItemCallback<Pair<Note,Beer>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();
    private final OnMyNoteItemInteractionListener listener;

    protected MyNotesRecyclerViewAdapter(OnMyNoteItemInteractionListener listener){
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyNotesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_notes_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNotesRecyclerViewAdapter.ViewHolder holder, int position) {
        Pair<Note,Beer> item = getItem(position);
        holder.bind(item.first, item.second, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.manufacturer)
        TextView manufacturer;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.photo)
        ImageView photo;

        @BindView(R.id.note)
        EditText note;

        @BindView(R.id.removeFromFridge)
        Button remove;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Note note, Beer beer, OnMyNoteItemInteractionListener listener){
            name.setText(beer.getName());
            manufacturer.setText(beer.getManufacturer());
            category.setText(beer.getCategory());
            GlideApp.with(itemView).load(beer.getPhoto())
                    .apply(new RequestOptions().override(240,240).centerInside())
                    .into(photo);
            itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, beer));
            note.setNote(note.getNote());
            remove.setOnClickListener(v -> listener.onNoteClickedListener(beer, note));
        }
    }
}
