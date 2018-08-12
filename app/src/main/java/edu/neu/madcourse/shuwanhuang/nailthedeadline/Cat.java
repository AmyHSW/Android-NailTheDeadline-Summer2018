package edu.neu.madcourse.shuwanhuang.nailthedeadline;

public class Cat {

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
        catID = id;
    }

    public int getID() {
        return catID;
    }

    public Integer getImage() {
        return CAT_IMAGES[catID];
    }
}
