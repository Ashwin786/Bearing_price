package com.bearing_price.sync;

/**
 * Created by user1 on 14/11/18.
 */
public class Train extends Thread {
    // reference to Line's Object.
    Line line;

    Train(Line line) {
        this.line = line;
    }

    @Override
    public void run() {
        line.getLine();
    }
}
