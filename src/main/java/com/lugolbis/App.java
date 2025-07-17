package com.lugolbis;

import com.lugolbis.ui.Component;
import com.lugolbis.ui.Window;

public class App {
    public static void main(String[] args) {
        Component component1 = new Component("Title1", 1920, 1080);
        component1.addInput("Instruction1");

        Component component2 = new Component("Title2", 1920, 1080);
        component2.addInput("Instruction2");

        Component[] components = {component1, component2};

        Window.main(components);
    }
}
