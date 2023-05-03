package memograph;

import javax.swing.*;
import java.awt.*;
import java.util.*;

final class Drawer extends JFrame {
    private static final double SCALING_FACTOR = 0.9;

    Drawer(Date[] dates, long[] usedMemories) {
        setTitle("Memory usage graph");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = (int)(screenSize.width * SCALING_FACTOR);
        int height = (int)(screenSize.height * SCALING_FACTOR);
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        GraphicPane pane = new GraphicPane(80, 50, width - 180, height - 120, usedMemories,
                dates);
        pane.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        pane.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        add(pane);
    }
}
