package com.ozstrategy.model;

public abstract class AbstractBaseObject {

    public AbstractBaseObject() {
        super();
    }


    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

} 
