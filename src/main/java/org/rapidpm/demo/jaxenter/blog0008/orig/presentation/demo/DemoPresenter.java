package org.rapidpm.demo.jaxenter.blog0008.orig.presentation.demo;

import javax.inject.Inject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.rapidpm.demo.jaxenter.blog0008.business.demo.BusinessLogic;

/**
 * Created by Sven Ruppert on 04.12.13.
 */
public class DemoPresenter {
    @FXML Label messageA;
    @FXML Label messageB;

    @Inject BusinessLogic businessLogicA;
    @Inject BusinessLogic businessLogicB;

    public void launch() {
        messageA.setText(businessLogicA.doSomething());
        messageB.setText(businessLogicB.doSomething());
    }
}
