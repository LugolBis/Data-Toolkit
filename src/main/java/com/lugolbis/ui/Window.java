package com.lugolbis.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


import com.formdev.flatlaf.FlatLightLaf;

public class Window extends JFrame {
    private JPanel mainPanel;

    public Window(Dimension dimension) {
        mainPanel = new JPanel();
        int width = dimension.width;
        int height = dimension.height;

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder((int)(height*0.05), (int)(width*0.05), (int)(height*0.05), (int)(width*0.05)));
        mainPanel.setBackground(Color.ORANGE);

        this.setSize(width, height);
        this.add(mainPanel);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension newSize = getSize();

                for (Component component : getContentPane().getComponents()) {
                    if (component instanceof JComponent) {
                        ((JComponent) component).setPreferredSize(newSize);
                    }
                }

                revalidate();
            }
        });
    }

    @Override
    protected void frameInit() {
        super.frameInit();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setName("Data Toolkit");
    }

    public void addComponent(Component component) {
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;

        mainPanel.add(component);
        mainPanel.add(Box.createVerticalStrut((int)(height*0.05)));
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(Component[] components) {
        try {
            javax.swing.UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Window window = new Window(screenSize);

            for (Component component : components) {
                window.addComponent(component);
            }

            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
