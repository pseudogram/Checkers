package FXTutorials;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The main class for a Java FX application must extend the Java FX Application class.
 *
 * A Java FX UI is defined by a Stage and a Scene. The Stage is at the top level, in other words,
 * it is the window. The Scene is the container for all of the content.
 *
 * The content of a Scene is defined by a hierarchical graph of nodes.
 *
 */
public class HelloWorld extends Application {

    /**
     * The main() method is not required for JavaFX applications when the JAR file for the application is created with
     * the JavaFX Packager tool. However, it is useful to include the main() method so you can run JAR files that were
     * created without the JavaFX Launcher.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is the main entry point for Java FX applications.
     *
     * The StackPane object is a resizable layout node.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        // The primaryStage is the window
        primaryStage.setTitle("Hello World!");

        Button btn = new Button();  // Create a button add to the Scene
        btn.setText("Say 'Hello World'");  // Set the text of the button
        btn.setOnAction(new EventHandler<ActionEvent>() {  // Create an action for the button

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();  // Create the root layout node
        root.getChildren().add(btn);  // Add the button to the root node.

        primaryStage.setScene(new Scene(root, 500, 250)); // Create a Scene and add the buttons to it.
        primaryStage.show();
    }
}