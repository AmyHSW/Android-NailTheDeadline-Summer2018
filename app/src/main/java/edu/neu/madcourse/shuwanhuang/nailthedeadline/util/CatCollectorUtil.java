package edu.neu.madcourse.shuwanhuang.nailthedeadline.util;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Cat;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;

public class CatCollectorUtil {

    private static final int NUM_MINUTES_PER_HOUR = 60;
    private static final int DISTRIBUTION_LENGTH = 10;
    private static final int MAX_HOURS = 16;
    // For tasks whose number of minutes is greater than 15 * 60, they will follow the probability
    // distribution of 15 hours. We set it up this way because we want to recommend our users
    // to split a big project into small tasks and use our app to focus on those "milestone" tasks.
    private static final int[][] CAT_ID_DISTRIBUTION = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 1, 1, 1, 1, 2, 2},
            {0, 1, 1, 1, 1, 2, 2, 2, 2, 3},
            {1, 1, 1, 1, 2, 2, 2, 2, 3, 3},
            {1, 1, 2, 2, 2, 2, 3, 3, 3, 4},
            {1, 2, 2, 2, 2, 3, 3, 3, 4, 4},
            {1, 2, 2, 3, 3, 3, 3, 4, 4, 5},
            {1, 2, 3, 3, 3, 4, 4, 4, 5, 5},
            {2, 3, 3, 4, 4, 4, 5, 5, 5, 6},
            {3, 4, 4, 5, 5, 5, 6, 6, 6, 7},
            {3, 4, 5, 5, 6, 6, 7, 7, 7, 8},
            {4, 5, 6, 6, 7, 7, 8, 8, 8, 9},
            {5, 6, 7, 7, 8, 8, 9, 9, 9, 10},
            {6, 7, 8, 8, 9, 9, 10, 10, 10, 11},
            {6, 7, 8, 9, 9, 10, 10, 11, 11, 11},
            {7, 8, 9, 10, 10, 11, 11, 11, 12, 12},
    };

    public static Cat collectCatForTask(Task task) {
        int numMinutes = task.getWorkedOnInMinute();
        int numHrs = numMinutes / NUM_MINUTES_PER_HOUR;
        if (numHrs > MAX_HOURS) numHrs = MAX_HOURS;
        int[] catIdDistribution = CAT_ID_DISTRIBUTION[numHrs];
        int idx = (int) Math.floor(Math.random() * DISTRIBUTION_LENGTH);
        return new Cat(catIdDistribution[idx]);
    }
}
