package me.interair.lexer.mr;

import me.interair.lexer.mr.gui.TextEditor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        TextEditor textEditor = new TextEditor();
        textEditor.setVisible(true);
    }
}