package com.elic0de.aooni.util.tuplet;

public class Triple<F, S, T> extends Tuple<F, S> {

    public final T third;

    public  Triple(F first, S second, T third){
        super(first, second);
        this.third = third;
    }

}