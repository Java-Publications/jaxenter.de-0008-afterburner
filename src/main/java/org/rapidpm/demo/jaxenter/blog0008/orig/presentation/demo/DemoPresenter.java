package org.rapidpm.demo.jaxenter.blog0008.orig.presentation.demo;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.rapidpm.demo.jaxenter.blog0008.business.demo.BusinessLogic;

/**
 * Created by Sven Ruppert on 04.12.13.
 */
public class DemoPresenter {
    @FXML Label message;

    @Inject BusinessLogic businessLogic;

    public void launch() {
        System.out.println("message = " + message);
        message.setText(businessLogic.doSomething());
    }
}
