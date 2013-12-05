package org.rapidpm.demo.jaxenter.blog0008.business.demo;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Sven Ruppert on 04.12.13.
 */
public class BusinessLogic {


    //hier fehlt ein Inject mit Qualifier, Scopes usw.
    @Inject BusinessSubLogic subLogic;

    @PostConstruct
    public void init() {
        System.out.println("BusinessLogic.init()");
    }

    public String doSomething() {
//        return "done at " + new Date() ;
        return "done at " + subLogic.createNewDate() ;
    }
}
