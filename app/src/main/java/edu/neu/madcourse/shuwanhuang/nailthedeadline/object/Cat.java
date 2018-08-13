package edu.neu.madcourse.shuwanhuang.nailthedeadline.object;

import java.util.Objects;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;

public class Cat {

    private static final int MIN_CAT_ID = 0;
    private static final int MAX_CAT_ID = 12;

    // references to our images
    private static final Integer[] CAT_IMAGES = {
            R.drawable.fail_logo,
            R.drawable.cat1, R.drawable.cat2,
            R.drawable.cat3, R.drawable.cat4,
            R.drawable.cat5, R.drawable.cat6,
            R.drawable.cat7, R.drawable.cat8,
            R.drawable.cat9, R.drawable.cat10,
            R.drawable.cat11, R.drawable.cat12,
    };

    private final int catID;

    public Cat(int id) {
        if (id < MIN_CAT_ID || id > MAX_CAT_ID) {
            throw new RuntimeException("The cat ID (" + id + ") is invalid.");
        }
        catID = id;
    }

    public int getID() {
        return catID;
    }

    public Integer getImage() {
        return CAT_IMAGES[catID];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cat cat = (Cat) o;
        return catID == cat.catID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(catID);
    }
}
