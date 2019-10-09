package ch.beerpro.domain.models;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Objects;

public class BeerInFridge implements Entity, Serializable {

    public static final String COLLECTION = "beersInFridge";
    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_ID = "userId";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_AMOUNT = "amount";

    /**
     * The id is formed by `$userId_$beerId` to make queries easier.
     */
    @Exclude
    private String id;
    private String beerId;
    private String userId;
    private String amount;

    public BeerInFridge(String beerId, String userId, String amount) {
        this.beerId = beerId;
        this.userId = userId;
        this.amount = amount;
    }

    public BeerInFridge() {
    }

    public static String generateId(String userId, String beerId){
        return String.format("%s_%s", userId, beerId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getBeerId() {
        return beerId;
    }

    public void setBeerId(String beerId) {
        this.beerId = beerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeerInFridge that = (BeerInFridge) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(beerId, that.beerId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beerId, userId, amount);
    }

    @NonNull
    public String toString(){
        return "BeerInFridge(id=" + this.getId() + ", userId=" + this.getUserId() + ", beerId=" + this.getBeerId() + ", amount=" + this.getAmount() + ")";
    }
}
