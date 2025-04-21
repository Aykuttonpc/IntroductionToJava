package com.example.chapter15;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class _2EventAndEventSoruce extends Application {

    /*
     An event is an object created from an event source. Firing an event means to create an
     event and delegate the handler to handle the event.
     The component that creates an event and fires it is called the event source object or simply source object or  source component
     An event object contains whatever properties are pertinent to the event. You can identify
     the source object of an event using the getSource() instance method in the EventObject
     class. The subclasses of EventObject deal with specific types of events, such as action
     events, window events, mouse events, and key events.

 15.1    What is an event source object? What is an event object? Describe the relationship
         between an event source object and an event object.
Answer
Event Source Object
bir kullanıcı etkileşimi veya sistem olayı meydana geldiğinde bu olayı tetikleyen GUI bileşenidir
Örneğin
button tıklandıgında
textfield entera basıldıgında
mouse tıklandıgında

Event object
ActionEvent Hangi butonun tıklandıgını
MouseEvent Farenin kordinatı
KeyEvent hangi tuşa  basıldıgı

aralarındaki ilişki
Olay kaynağı, bir etkileşim olduğunda uygun olay nesnesi oluşturur (örneğin, Button tıklandığında ActionEvent oluşturur).
Bu nesne, ilgili event handler'a iletilir.
Handler, olay nesnesinin içerdiği bilgileri kullanarak tepki verir.

15.2 Can a button fire a MouseEvent? Can a button fire a KeyEvent? Can a button fire an ActionEvent?
✅ Evet. Bir buton fareyle tıklandığında MouseEvent tetikler. Örneğin:
button.setOnMouseClicked(e -> System.out.println("Fare tıklandı: " + e.getButton()));
b) Button → KeyEvent:

❌ Hayır. Butonlar klavye olayları tetiklemez. KeyEvent genellikle TextField veya Scene gibi bileşenlerde kullanılır.
c) Button → ActionEvent:

✅ Evet. Bir butonun temel olayıdır. Tıklandığında veya Space/Enter tuşlarına basıldığında ActionEvent tetikler. Örneğin:
button.setOnAction(e -> System.out.println("Buton tıklandı!"));
     */
    private TextArea logArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Ana layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Log alanı
        logArea = new TextArea();
        logArea.setEditable(false);
        root.setBottom(logArea);

        // Tüm örneklerin olduğu tab panel
        TabPane tabPane = new TabPane();

        // 1. ActionEvent Örnekleri
        Tab actionTab = new Tab("Action Events Sekansı");
        actionTab.setContent(createActionEventsDemo());

        // 2. MouseEvent Örnekleri
        Tab mouseTab = new Tab("Mouse Events Sekansı");
        mouseTab.setContent(createMouseEventsDemo());

        // 3. KeyEvent Örnekleri
        Tab keyTab = new Tab("Key Events örneği");
        keyTab.setContent(createKeyEventsDemo());

        // 4. Diğer Kontroller
        Tab controlsTab = new Tab("UI Kontrol");
        controlsTab.setContent(createControlsDemo());

        tabPane.getTabs().addAll(actionTab, mouseTab, keyTab, controlsTab);
        root.setCenter(tabPane);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("JavaFX Event Handling Örnek");
        primaryStage.show();
    }

    private void log(String message) {
        logArea.appendText(message + "\n");
    }

    // 1. ActionEvent Örnekleri
    private VBox createActionEventsDemo() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        // Button
        Button button = new Button("Tıklat bana knk!");
        button.setOnAction(e -> log("Tıklatdılar bana ehe : " + e.getSource()));

        // TextField
        TextField textField = new TextField();
        textField.setPromptText("enter'a bas usda Enter");
        textField.setOnAction(e -> log("Enter'a niye basıyon : " + textField.getText()));

        // CheckBox
        CheckBox checkBox = new CheckBox("Check sekansı");
        checkBox.setOnAction(e -> log("CheckBox  status: " + checkBox.isSelected()));

        // RadioButton
        ToggleGroup group = new ToggleGroup();
        RadioButton radio1 = new RadioButton("Sekans 1");
        radio1.setToggleGroup(group);
        RadioButton radio2 = new RadioButton("Sekans 2");
        radio2.setToggleGroup(group);
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                log("Seçildi: " + ((RadioButton)newVal).getText());
            }
        });

        // ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Zamazingo 1", "Zamazingo 2", "Zamazingo 3");
        comboBox.setOnAction(e -> log("ComboBox selected: " + comboBox.getValue()));

        box.getChildren().addAll(
                new Label("Button:"), button,
                new Label("TextField:"), textField,
                new Label("CheckBox:"), checkBox,
                new Label("RadioButtons:"), radio1, radio2,
                new Label("ComboBox:"), comboBox
        );

        return box;
    }

    // 2. MouseEvent Örnekleri
    private VBox createMouseEventsDemo() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        // ImageView oluştur
        ImageView imageView = new ImageView();

        try {
            // Resmi resources'tan yükle
            InputStream inputStream = getClass().getResourceAsStream("/balık.jpeg");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                imageView.setImage(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
            } else {
                log("Resim bulunamadı! (balık.jpeg)");
                // Alternatif olarak placeholder ekleyebilirsiniz
                imageView.setImage(null);
                Rectangle placeholder = new Rectangle(200, 150, Color.LIGHTBLUE);
                imageView.setClip(placeholder);
            }
        } catch (Exception e) {
            log("Resim yüklenirken hata: " + e.getMessage());
        }

        // Mesaj için Label oluştur (başlangıçta görünmez)
        Label messageLabel = new Label("Yemlendin enayi!");
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
        messageLabel.setVisible(false);

        // Mouse olayları
        imageView.setOnMouseEntered(e -> {
            log("Balık resminin üzerindesiniz!");
            imageView.setEffect(new Glow(0.5)); // Hover efekti
            messageLabel.setVisible(true); // Mesajı göster
        });

        imageView.setOnMouseExited(e -> {
            imageView.setEffect(null);
            messageLabel.setVisible(false); // Mesajı gizle
        });

        // Resim ve mesajı StackPane içine yerleştir
        StackPane container = new StackPane();
        container.getChildren().addAll(imageView, messageLabel);

        box.getChildren().addAll(
                new Label("Balık Resmi:"), container,
                new Label("Fareyi resmin üzerine getirin")
        );

        return box;
    }

    // 3. KeyEvent Örnekleri
    private VBox createKeyEventsDemo() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        TextField textField = new TextField();
        textField.setPromptText("Type here for key events");

        textField.setOnKeyPressed(e -> log(String.format(
                "Key pressed: %s (Code: %s)", e.getText(), e.getCode())));

        textField.setOnKeyReleased(e -> log(String.format(
                "Key released: %s", e.getCode())));

        textField.setOnKeyTyped(e -> log(String.format(
                "Key typed: '%s'", e.getCharacter())));

        box.getChildren().addAll(
                new Label("Key Events in TextField:"), textField,
                new Label("Try: type, hold, release keys")
        );

        return box;
    }

    // 4. Diğer UI Kontrolleri
    private VBox createControlsDemo() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));

        // Slider
        Slider slider = new Slider(0, 100, 50);
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                log("Slider value: " + newVal.intValue()));

        // DatePicker
        DatePicker datePicker = new DatePicker();
        datePicker.setOnAction(e -> log("Date selected: " + datePicker.getValue()));

        // ColorPicker
        ColorPicker colorPicker = new ColorPicker(Color.BLUE);
        colorPicker.setOnAction(e -> log("Color selected: " + colorPicker.getValue()));

        // ListView
        ListView<String> listView = new ListView<>();
        listView.getItems().addAll("Apple", "Banana", "Orange");
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                log("List selection: " + newVal));

        box.getChildren().addAll(
                new Label("Slider:"), slider,
                new Label("DatePicker:"), datePicker,
                new Label("ColorPicker:"), colorPicker,
                new Label("ListView:"), listView
        );

        return box;
    }
}