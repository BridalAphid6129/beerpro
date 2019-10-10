package ch.beerpro.domain.models;

import java.util.Date;
import java.util.Objects;

public class MyBeerFromNote implements MyBeer {
    private Beer beer;
    private Note note;



    public MyBeerFromNote(Note note, Beer beer){
        this.beer = beer;
        this.note = note;
    }

    @Override
    public String getBeerId() {
        return note.getBeerId();
    }

    @Override
    public Date getDate() {
        return note.getCreationDate();
    }


    @Override
    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyBeerFromNote that = (MyBeerFromNote) o;
        return Objects.equals(beer, that.beer) &&
                Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beer, note);
    }

    @Override
    public String toString() {
        return "MyBeerFromNote{" +
                "beer=" + beer +
                ", note=" + note +
                '}';
    }
}
