package org.rapidpm.demo.jaxenter.blog0008.jdk8.afterburner.views;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.rapidpm.demo.jaxenter.blog0008.jdk8.afterburner.injection.InjectionProviderJDK8;

import static java.util.ResourceBundle.getBundle;

/**
 * Created by Sven Ruppert on 04.12.13.
 */
public class FXMLViewJDK8 {
    public static final String DEFAULT_ENDING = "view";
    protected FXMLLoader loader;

    public FXMLViewJDK8() {
        this.init(getClass(), getFXMLName());
    }

    private void init(Class clazz, String conventionalName) {
        final URL resource = clazz.getResource(conventionalName);
        String bundleName = getBundleName();
        ResourceBundle bundle = getResourceBundle(bundleName);
        this.loader = new FXMLLoader(resource, bundle);
        this.loader.setControllerFactory(InjectionProviderJDK8::instantiatePresenter);
        try {
            loader.load();
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot load " + conventionalName, ex);
        }
    }

    public Parent getView() {
        Parent parent = this.loader.getRoot();
        addCSSIfAvailable(parent);
        return parent;
    }

    /**
     * Scene Builder creates for each FXML document a root container. This
     * method omits the root container (e.g. AnchorPane) and gives you the
     * access to its first child.
     *
     * @return the first child of the AnchorPane
     */
    public Node getViewWithoutRootContainer() {
        final ObservableList<Node> children = getView().getChildrenUnmodifiable();
        if (children.isEmpty()) {
            return null;
        }
        return children.listIterator().next();
    }

    void addCSSIfAvailable(Parent parent) {
        URL uri = getClass().getResource(getStyleSheetName());
        if (uri == null) {
            return;
        }
        String uriToCss = uri.toExternalForm();
        parent.getStylesheets().add(uriToCss);
    }

    String getStyleSheetName() {
        return getConventionalName(".css");
    }

    public Object getPresenter() {
        return this.loader.getController();
    }

    String getConventionalName(String ending) {
        return getConventionalName() + ending;
    }

    String getConventionalName() {
        String clazz = this.getClass().getSimpleName().toLowerCase();
        return stripEnding(clazz);
    }

    String getBundleName() {
        String conventionalName = getConventionalName();
        return this.getClass().getPackage().getName() + "." + conventionalName;
    }

    static String stripEnding(String clazz) {
        if (!clazz.endsWith(DEFAULT_ENDING)) {
            return clazz;
        }
        int viewIndex = clazz.lastIndexOf(DEFAULT_ENDING);
        return clazz.substring(0, viewIndex);
    }

    final String getFXMLName() {
        return getConventionalName(".fxml");
    }

    static ResourceBundle getResourceBundle(String name) {
        try {
            return getBundle(name);
        } catch (MissingResourceException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
