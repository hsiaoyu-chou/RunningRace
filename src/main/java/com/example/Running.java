package com.example;

/**
 * Created by choulittlefish on 2017/10/20.
 */

//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
import java.util.Random;

//@Component
//@Scope("prototype")

public class Running extends Thread{
    private volatile int value;

    public void run(){

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Random r = new Random();
        if(value < 100){
            value = value + r.nextInt(20);
        }

        //System.out.println(getName() + " is running "+value);

        if(value >= 100) value = 100;

        return;
    }

    public void setValue(int n) {
        this.value = n;
    }

    public int getValue() {
        return this.value;
    }
}
