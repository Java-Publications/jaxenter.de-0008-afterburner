package org.rapidpm.demo.jaxenter.blog0008.orig;

import com.airhacks.afterburner.injection.InjectionProvider;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.rapidpm.demo.jaxenter.blog0008.orig.presentation.demo.DemoView;

/**
 * Created by Sven Ruppert on 04.12.13.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DemoView appView = new DemoView();
        Scene scene = new Scene(appView.getView());
        stage.setTitle("demo.fx");
        final String uri = getClass().getResource("main.css").toExternalForm();
        scene.getStylesheets().add(uri);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        InjectionProvider.forgetAll();
    }

    public static void main(String[] args) {
        launch(args);
    }
}