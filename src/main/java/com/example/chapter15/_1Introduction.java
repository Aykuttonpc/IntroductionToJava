package com.example.chapter15;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.IkonHandler;
import javafx.event.ActionEvent;


import javax.swing.*;
import javax.swing.plaf.PanelUI;


public class _1Introduction {

    /*
        1. The object must be an instance of the EventHandler<T extends Event> interface.
        This interface defines the common behavior for all handlers. <T extends Event>
        denotes that T is a generic type that is a subtype of Event.
        2. The EventHandler object handler must be registered with the event source object
        using the method source.setOnAction(handler).
        The EventHandler<ActionEvent> interface contains the handle(ActionEvent)
        method for processing the action event.
     */

    public static void   start(Stage primaryStage) {
        HBox pane = new HBox(10);
        pane.setAlignment(Pos.CENTER);
        Button btOk = new Button("OK");
        Button btCancel = new Button("Cancel");
        OKHandlerClass handler1 = new  OKHandlerClass();
        btOk.setOnAction(handler1);
        CancelHandlerClass handler2 = new CancelHandlerClass();
        btCancel.setOnAction(handler2);
        pane.getChildren().addAll(btOk, btCancel);

        //create scene
        Scene scene = new Scene(pane);
        primaryStage.setTitle("HandleEvent"); //set stage title
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

 class OKHandlerClass implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent e) {
        System.out.println("OK Button clicked");
    }
}

 class CancelHandlerClass implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent e) {
        System.out.println("Cancel Button clicked");
    }
}






