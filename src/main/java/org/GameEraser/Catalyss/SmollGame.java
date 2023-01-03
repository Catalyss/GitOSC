package org.GameEraser.Catalyss;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class SmollGame {
    private static boolean Replay = true;
    private static int Try = 3;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            while (Replay) {
                int MAX = 0;
                int MIN = 0;
                boolean win = false;
                boolean Setup = true;
                while (Setup) {
                    System.out.println("Please input a lower bound");
                    String line = scanner.nextLine();
                    try {
                        MIN = Integer.parseInt(line);
                    } catch (Exception ignored) {
                        System.out.println("Please input and integer");
                        return;
                    }
                    System.out.println("Please input a upper bound");
                    line = scanner.nextLine();
                    try {
                        MAX = Integer.parseInt(line);
                        if (MIN + 1 > MAX) {
                            System.out.println("Upper bound should be bigger than " + (MIN + 1));
                        }
                    } catch (Exception ignored) {
                        System.out.println("Please input and integer");
                        return;
                    }
                    Setup = false;
                }
                int RGN = 0;
                for (int i = 0; i < Try; i++) {
                    int AWNSER;
                    if(i==0) {
                        RGN = new Random().nextInt(MAX - MIN) + MIN;
                        System.out.println("Let's Play now\n}---{\nGuess the number Between " + MIN + " - " + MAX);
                    }
                    String line = scanner.nextLine();
                    try {
                        AWNSER = Integer.parseInt(line);
                    } catch (Exception ignored) {
                        System.out.println("Please input and integer");
                        i--;
                        return;
                    }
                    if (AWNSER == RGN) {
                        win = true;
                        break;
                    }
                    if (AWNSER < RGN) System.out.println("Higher");
                    if (AWNSER > RGN) System.out.println("Lower");
                }
                if (win) System.out.println("You won");
                else System.out.println("You lost");
                System.out.println("Wanna play again ?(y/n)");
                String line = scanner.nextLine();
                if (line.toLowerCase().contains("y")) Replay = true;
                if (line.toLowerCase().contains("n")) Replay = false;
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            // System.in has been closed
            System.out.println("System.in was closed; exiting");
        }
    }
}