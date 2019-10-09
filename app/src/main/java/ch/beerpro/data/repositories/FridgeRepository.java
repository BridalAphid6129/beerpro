package ch.beerpro.data.repositories;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.BeerInFridge;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;
import ch.beerpro.presentation.utils.EntityClassSnapshotParser;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class FridgeRepository {

    //watch wishlist repository

    private static LiveData<List<BeerInFridge>> getBeersInFridgeByUser(String userId){
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(BeerInFridge.COLLECTION)
        .orderBy(BeerInFridge.FIELD_AMOUNT, Query.Direction.DESCENDING).whereEqualTo(BeerInFridge.FIELD_USER_ID, userId),
                BeerInFridge.class);

    }

    private static LiveData<BeerInFridge> getUserBeerInFridgeListFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(BeerInFridge.COLLECTION)
                .document(BeerInFridge.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, BeerInFridge.class);
    }

    public Task<Void> setUserFridgeItemAmount(String userId, String itemId, String amount) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String beerInFridgeId = BeerInFridge.generateId(userId, itemId);

        DocumentReference beerInFridgeEntryQuery = db.collection(BeerInFridge.COLLECTION).document(beerInFridgeId);

        return beerInFridgeEntryQuery.get().continueWithTask(task -> {
            if(Objects.equals(amount, "0")) {
                return beerInFridgeEntryQuery.delete();
            } else if (task.isSuccessful()) {
                return beerInFridgeEntryQuery.set(new BeerInFridge(userId, itemId, amount));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<Pair<BeerInFridge, Beer>>> getMyFridgeWithBeers(LiveData<String> currentUserId,
                                                                         LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyFridge(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<BeerInFridge> beersInFridge = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<BeerInFridge, Beer>> result = new ArrayList<>();
            for(BeerInFridge beerInFridge : beersInFridge) {
                Beer beer = beersById.get(beerInFridge.getBeerId());
                result.add(Pair.create(beerInFridge, beer));
            }
            return result;
        });
    }

    public LiveData<List<BeerInFridge>> getMyFridge(LiveData<String> currentUserId) {
        return switchMap(currentUserId, FridgeRepository::getBeersInFridgeByUser);
    }

    public LiveData<BeerInFridge> getMyBeerInFridgeForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {
        return switchMap(combineLatest(currentUserId, beer), FridgeRepository::getUserBeerInFridgeListFor);
    }
}
