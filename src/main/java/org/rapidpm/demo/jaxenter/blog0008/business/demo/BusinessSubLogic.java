package org.rapidpm.demo.jaxenter.blog0008.business.demo;

import java.util.Date;

import javax.annotation.PostConstruct;

/**
 * Created by Sven Ruppert on 04.12.13.
 */
public class BusinessSubLogic {

    private long stamp = System.nanoTime();

    @PostConstruct
    public void init() {
        System.out.println("BusinessSubLogic.init() stamp -> " + stamp);
    }


    public Date createNewDate(){

        return new Date();
    }

}
