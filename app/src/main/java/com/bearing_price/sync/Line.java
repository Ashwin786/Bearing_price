package com.bearing_price.sync;

import android.util.Log;

/**
 * Created by user1 on 14/11/18.
 */
public class Line {
    // if multiple threads(trains) will try to
    // access this unsynchronized method,
    // they all will get it. So there is chance
    // that Object's state will be corrupted.
    synchronized public void getLine() {
        for (int i = 0; i < 3; i++) {
//            System.out.println(i);
            Log.e("Thread",""+i);
            try {
                Thread.sleep(400);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
