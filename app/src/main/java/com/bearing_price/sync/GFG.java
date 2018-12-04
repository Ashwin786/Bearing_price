package com.bearing_price.sync;

/**
 * Created by user1 on 14/11/18.
 */
public class GFG {
/*
    public static void main(String[] args) {
        // Object of Line class that is shared
        // among the threads.
       *//* Line obj = new Line();

        // creating the threads that are
        // sharing the same Object.
        Train train1 = new Train(obj);
        Train train2 = new Train(obj);

        // threads start their execution.
        train1.start();
        train2.start();*//*
       *//* B b;
        C c;
        B b1;
        c = new C();
        b1 = new B(c);
        b = new B(c);

        b.start();
        b1.start();*//*
    }*/

    public void start() {
        Line obj = new Line();
//        Line obj1 = new Line();

        // creating the threads that are
        // sharing the same Object.
        Train thread1 = new Train(obj);
        Train thread2 = new Train(obj);

        // threads start their execution.
        thread1.start();
        thread2.start();
    }
}
