package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;

import java.util.Objects;

public class MyPrice implements Entity {
    public static final String COLLECTION = "prices";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_USER_ID = "userId";

    @Exclude
    private String id;
    private String beerId;
    private String userId;
    private double price;

    public MyPrice(String beerId, String userId, double price) {
        this.beerId = beerId;
        this.userId = userId;
        this.price = price;
    }

    public MyPrice() {

    }

    public static String generateId(String userId, String beerId) {
        return String.format("%s_%s", userId, beerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyPrice myPrice = (MyPrice) o;
        return Double.compare(myPrice.price, price) == 0 &&
                Objects.equals(id, myPrice.id) &&
                Objects.equals(beerId, myPrice.beerId) &&
                Objects.equals(userId, myPrice.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beerId, userId, price);
    }

    @Override
    public String toString() {
        return "MyPrice{" +
                "id='" + id + '\'' +
                ", beerId='" + beerId + '\'' +
                ", userId='" + userId + '\'' +
                ", price=" + price +
                '}';
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
