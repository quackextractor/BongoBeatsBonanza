package main.code;

import main.code.view.GameFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(()->{GameFrame gameFrame = new GameFrame();});

    }
}