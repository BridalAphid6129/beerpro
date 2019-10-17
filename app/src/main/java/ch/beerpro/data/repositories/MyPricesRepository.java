package ch.beerpro.data.repositories;

import android.util.Pair;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.beerpro.domain.models.Beer;
import ch.beerpro.domain.models.Entity;
import ch.beerpro.domain.models.MyPrice;
import ch.beerpro.domain.models.Note;
import ch.beerpro.domain.utils.FirestoreQueryLiveData;
import ch.beerpro.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.beerpro.domain.utils.LiveDataExtensions.combineLatest;

public class MyPricesRepository {

    private static LiveData<List<MyPrice>> getMyPricesByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance()
                .collection(MyPrice.COLLECTION).whereEqualTo(MyPrice.FIELD_USER_ID, userId),
                MyPrice.class);
    }

    private static LiveData<MyPrice> getUserPricesFor(Pair<String, Beer> input) {
        String userId = input.first;
        Beer beer = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(MyPrice.COLLECTION)
                .document(MyPrice.generateId(userId, beer.getId()));
        return new FirestoreQueryLiveData<>(document, MyPrice.class);
    }

    public Task<Void> setUserPrice(String beerId, double price, String userId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String priceId = MyPrice.generateId(userId, beerId);

        DocumentReference priceQuery = db.collection(Note.COLLECTION).document(priceId);

        return priceQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return priceQuery.delete();
            } else if (task.isSuccessful()) {
                return priceQuery.set(new MyPrice(beerId, userId, price));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<Pair<MyPrice, Beer>>> getMyPriceWithBeer(LiveData<String> currentUserId, LiveData<List<Beer>> allBeers) {
        return map(combineLatest(getMyPrices(currentUserId), map(allBeers, Entity::entitiesById)), input -> {
            List<MyPrice> prices = input.first;
            HashMap<String, Beer> beersById = input.second;

            ArrayList<Pair<MyPrice, Beer>> result = new ArrayList<>();
            for (MyPrice price : prices) {
                Beer beer = beersById.get(price.getBeerId());
                result.add(Pair.create(price, beer));

            }
            return result;
        });
    }

    public LiveData<List<MyPrice>> getMyPrices(LiveData<String> currentUserId) {
        return switchMap(currentUserId, MyPricesRepository::getMyPricesByUser);
    }

    public LiveData<MyPrice> getMyPricesForBeer(LiveData<String> currentUserId, LiveData<Beer> beer) {
        return switchMap(combineLatest(currentUserId, beer), MyPricesRepository::getUserPricesFor);
    }

}


