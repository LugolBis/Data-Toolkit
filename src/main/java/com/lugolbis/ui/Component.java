package com.lugolbis.ui;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.Font;

public class Component extends JComponent {
    private JPanel panel;
    private int width;
    private int height;

    public Component(String title, int width, int height) {
        panel = new JPanel();
        this.width = width;
        this.height = height;

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder((int)(height*0.05), (int)(width*0.05), (int)(height*0.05), (int)(width*0.05)));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, (int)(width*0.02)));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut((int)(height*0.05)));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(panel);
    }

    public void addInput(String instruction) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, (int)(height*0.05), (int)(width*0.1)));
        JLabel inputLabel = new JLabel(instruction);
        JTextField inputText = new JTextField((int)(width*0.1));

        inputText.setEditable(true);

        inputPanel.add(inputLabel);
        inputPanel.add(inputText);
        panel.add(inputPanel);
    }
}
