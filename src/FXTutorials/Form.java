package FXTutorials;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class Form extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is the main entry point for Java FX applications.
     *
     * The StackPane object is a resizable layout node.
     *
     * CSS files can be used for extra styling such as adding backgrounds.
     *
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Java FX Welcome");
//        System.out.println(Pos.valueOf("CENTER"));

        GridPane grid = createGrid();
        addControls(grid);
        addSignInButton(grid);

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);

        // Add a CSS file.
        scene.getStylesheets().add
                (Form.class.getResource("login.css").toExternalForm());

        primaryStage.show();
    }

    /**
     * Use a GridPane layout because it enables you to create a flexible grid of rows and columns in which to lay out
     * controls.
     */
    public GridPane createGrid(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        // Insets
        int insetsSize = 25;
        grid.setPadding(new Insets(insetsSize, insetsSize, insetsSize, insetsSize));
        return grid;
    }

    /**
     *
     * @param grid
     */
    public void addControls(GridPane grid) {
        Text scenetitle = new Text("Welcome");

        // Set the font of the Title, in code
//        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Set the font of the Title using CSS
        scenetitle.setId("welcome-text");

        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);
    }

    /**
     * Hbox is a layout pane used to position the sign in button
     *
     * @param grid
     */
    public void addSignInButton(GridPane grid) {
        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10); // Hbox is a layout pane used to position the sign in button
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        btn.setOnAction(new EventHandler<ActionEvent>() {  // Declare an anonymous class to handle action

            @Override
            public void handle(ActionEvent e) {
                // Set the font of the actiontarget, in code
                actiontarget.setFill(Color.FIREBRICK);
//                actiontarget.setFont(Font.font("Serif", FontWeight.BOLD, 20));

                // Set the font of the actiontarget using CSS
//                actiontarget.setId("actiontarget");

                actiontarget.setText("Sign in button pressed");
            }
        });
    }


}