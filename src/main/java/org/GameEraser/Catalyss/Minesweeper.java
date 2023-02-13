package org.GameEraser.Catalyss;

import org.json.simple.parser.JSONParser;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.FileReader;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Minesweeper extends JFrame {

    private JLabel statusbar;
    public Board bj = null;

    public Minesweeper() {
        statusbar = new JLabel("");
        bj=new Board(statusbar);
        org.json.simple.JSONObject Json = new org.json.simple.JSONObject();
        try {
            FileReader fr = new FileReader("src/resources/Settings.json");
            JSONParser parser = new JSONParser();
            Json = (org.json.simple.JSONObject) parser.parse(fr);
        } catch (Exception ignored) {}
        if (Json.containsKey("MINE")) try{bj.N_MINES=(int) Json.get("MINE");}catch(Exception ignored){}
        if (Json.containsKey("BOARDX")) try{bj.N_COLS=(int) Json.get("BOARDX");}catch(Exception ignored){}
        if (Json.containsKey("BOARDY")) try{bj.N_ROWS=(int) Json.get("BOARDY");}catch(Exception ignored){}
        initUI();
    }

    private void initUI() {
        add(statusbar, BorderLayout.SOUTH);

        add(bj);

        setResizable(false);
        pack();

        setTitle("Minesweeper");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new Minesweeper();
            ex.setVisible(true);
        });
    }
}
