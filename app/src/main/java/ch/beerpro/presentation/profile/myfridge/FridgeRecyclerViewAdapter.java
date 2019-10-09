package ch.beerpro.presentation.profile.myfridge;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import ch.beerpro.domain.models.BeerInFridge;
import ch.beerpro.presentation.utils.EntityPairDiffItemCallback;

public class FridgeRecyclerViewAdapter extends ListAdapter<Pair<BeerInFridge, Beer>, FridgeRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "FridgeRecyclerViewAda";

    private static final DiffUtil.ItemCallback<Pair<BeerInFridge, Beer>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();

    private final OnFridgeItemInteractionListener listener;

    protected FridgeRecyclerViewAdapter(OnFridgeItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_fridge_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<BeerInFridge, Beer> item = getItem(position);
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

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.numRatings)
        TextView numRatings;

        @BindView(R.id.amount)
        TextView amount;

        @BindView(R.id.removeFromFridge)
        Button remove;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(BeerInFridge beerInFridge, Beer beer, OnFridgeItemInteractionListener listener) {
            name.setText(beer.getName());
            manufacturer.setText(beer.getManufacturer());
            category.setText(beer.getCategory());
            GlideApp.with(itemView).load(beer.getPhoto())
                    .apply(new RequestOptions()
                            .override(240, 240)
                            .centerInside())
                    .into(photo);
            ratingBar.setNumStars(5);
            ratingBar.setRating(beer.getAvgRating());
            numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, beer.getNumRatings()));
            itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, beer));
            amount.setText(beerInFridge.getAmount());
            remove.setOnClickListener(view -> listener.onBeerInFridgeClickedListener(beer, "0"));
        }
    }
}
