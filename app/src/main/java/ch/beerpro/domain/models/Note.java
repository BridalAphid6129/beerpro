package ch.beerpro.domain.models;

import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.Objects;

public class Note implements Entity {
    public static final String COLLECTION = "notes";
    public static final String FIELD_BEER_ID = "beerId";
    public static final String FIELD_USER_ID = "userId";

    @Exclude
    private String id;
    private String beerId;
    private String note;
    private String userId;

    private Date creationDate;

    public Note( String beerId, String note, String userId, Date creationDate) {
        this.beerId = beerId;
        this.note = note;
        this.userId = userId;
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note1 = (Note) o;
        return Objects.equals(id, note1.id) &&
                Objects.equals(beerId, note1.beerId) &&
                Objects.equals(note, note1.note) &&
                Objects.equals(creationDate, note1.creationDate) &&
                Objects.equals(userId, note1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beerId, note, creationDate, userId);
    }

    public Note(){

    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBeerId() {
        return beerId;
    }

    public void setBeerId(String beerId) {
        this.beerId = beerId;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", beerId='" + beerId + '\'' +
                ", note='" + note + '\'' +
                ", creationDate=" + creationDate +
                ", userId='" + userId + '\'' +
                '}';
    }
    public static String generateId(String userId, String beerId){
        return String.format("%s_%s", userId, beerId);
    }

}
