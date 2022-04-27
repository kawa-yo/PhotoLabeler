package tests;

import javax.swing.InputMap;

public class Test {

    public static void main(String[] args) {
        InputMap inputMap = new InputMap();
        Object keys = inputMap.allKeys();
        System.out.println(keys);
    }
}
