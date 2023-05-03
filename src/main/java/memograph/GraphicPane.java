package memograph;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

final class GraphicPane extends JComponent {
    private static final Color POINT_COLOR = Color.GRAY;
    private static final int POINT_SIZE = 6;
    private final int startX, startY, width, height;
    private final List<Point> points;
    private final Date[] timestamps;
    private final long totalMemory;

    public GraphicPane(int startX, int startY, int width, int height, long[] usedMemories, Date[] timestamps) {
        points = new ArrayList<>();
        if (timestamps.length == 0) {
            throw new RuntimeException("No data for drawing graphic");
        }
        int interval = 0;
        if (timestamps.length > 1) {
            interval = width / (usedMemories.length - 2);
        }
        this.totalMemory = usedMemories[usedMemories.length - 1];
        for (int i = 0; i < usedMemories.length - 1; i++) {
            int x = startX + interval * i;
            int y = height + 70 - (int) ((usedMemories[i] * height) / totalMemory) - POINT_SIZE / 2;
            Point point = new Point(x, y);
            points.add(point);
        }
        this.startX = startX;
        this.startY = startY + height;
        this.width = width + 20;
        this.height = height;
        this.timestamps = timestamps;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawAxis(g2);
        drawMarkup(g2);
        double border = 0.8;
        drawBorder(g2, border);
        drawGraphLine(g2, border);
    }

    private void drawAxis(Graphics2D g2) {
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);
        g2.drawLine(this.startX, this.startY, this.startX, this.startY - height); //ordinate
        g2.drawLine(this.startX, this.startY, this.startX + width, this.startY); //abscissa
        g2.drawLine(this.startX, this.startY - height, this.startX - 10, this.startY - height + 20);
        g2.drawLine(this.startX, this.startY - height, this.startX + 10, this.startY - height + 20);
        g2.drawLine(this.startX + width, this.startY, this.startX + width - 20, this.startY + 10);
        g2.drawLine(this.startX + width, this.startY, this.startX + width - 20, this.startY - 10);
    }

    private void drawMarkup(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawString("Used memory", 5, 40);
        for (long i = this.totalMemory / 10; i <= totalMemory; i += this.totalMemory / 10) {
            g2.drawLine(
                    this.startX - 2,
                    this.height + 70 - (int) ((i * height) / totalMemory),
                    this.startX + 2,
                    this.height + 70 - (int) ((i * height) / totalMemory)
            );
            g2.drawString(String.valueOf(i), 5, this.height + 70 - (int) ((i * height) / totalMemory));
        }
        g2.drawString("Timestamps", this.width - 20, 80 + this.height);
        if (this.timestamps.length == 1) {
            g2.drawLine(
                    this.startX,
                    this.startY - 2,
                    this.startX,
                    this.startY + 2
            );
            g2.drawString(
                    this.timestamps[0].toString(),
                    this.startX - 70,
                    this.height + 68
            );
        }
        else {
            long timePeriod = this.timestamps[this.timestamps.length - 1].getTime() - this.timestamps[0].getTime();
            long startTime = this.timestamps[0].getTime();
            int step;
            if (this.timestamps.length < 4) {
                step = 1;
            } else {
                step = this.timestamps.length / 4;
            }
            Font font = new Font(g2.getFont().getName(), Font.BOLD, 10);
            g2.setFont(font);
            for (int i = 0; i < this.timestamps.length - 1; i += step) {
                g2.drawLine(
                        this.startX + (int) (((double)this.timestamps[i].getTime() - startTime) / timePeriod * width),
                        this.startY - 2,
                        this.startX + (int) (((double)this.timestamps[i].getTime() - startTime) / timePeriod * width),
                        this.startY + 2
                );
                g2.drawString(
                        this.timestamps[i].toString(),
                        this.startX + (int) (((double)this.timestamps[i].getTime() - startTime) / timePeriod * width) - 70,
                        this.height + 68
                );
            }
        }
    }

    private void drawBorder(Graphics2D g2, double percent) {
        if (percent <= 0 || percent > 1) {
            throw new IllegalArgumentException("Unacceptable boundary percent");
        }
        int border = (int) (this.height * (1 - percent)) + 70;
        g2.setColor(Color.RED);
        Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2.setStroke(dashed);
        g2.drawLine(this.startX, border, this.startX + this.width, border);
        g2.drawString((int) (percent * 100) + "%", this.startX + this.width, border);
    }

    private void drawGraphLine(Graphics2D g2, double percent) {
        int borderHeight = (int) (this.height * (1 - percent)) + 70;
        int lastX = (int) points.get(0).getX();
        int lastY = (int) points.get(0).getY();
        g2.setStroke(new BasicStroke(2));
        g2.setColor(POINT_COLOR);
        for (Point p : points) {
            int x = (int) p.getX();
            int y = (int) p.getY();
            if (y <= borderHeight) {
                if (lastY <= borderHeight) {
                    g2.setColor(Color.RED);
                    g2.fillOval(x, y, POINT_SIZE, POINT_SIZE);
                    g2.drawLine(lastX + POINT_SIZE / 2, lastY + POINT_SIZE / 2, x + POINT_SIZE / 2, y + POINT_SIZE / 2);
                } else {
                    int borderX = lastX + (x - lastX) * (lastY + POINT_SIZE / 2 - borderHeight) / (lastY - y);
                    g2.drawLine(lastX + POINT_SIZE / 2, lastY + POINT_SIZE / 2, borderX, borderHeight);
                    g2.setColor(Color.RED);
                    g2.fillOval(x, y, POINT_SIZE, POINT_SIZE);
                    g2.drawLine(borderX, borderHeight, x + POINT_SIZE / 2, y + POINT_SIZE / 2);
                }
                g2.setColor(POINT_COLOR);
            } else if (lastY <= borderHeight) {
                g2.fillOval(x, y, POINT_SIZE, POINT_SIZE);
                int borderX = x + POINT_SIZE / 2 - (x - lastX) * (y + POINT_SIZE / 2 - borderHeight) / (y - lastY);
                g2.setColor(Color.RED);
                g2.drawLine(lastX + POINT_SIZE / 2, lastY + POINT_SIZE / 2, borderX, borderHeight);
                g2.setColor(POINT_COLOR);
                g2.drawLine(borderX, borderHeight, x + POINT_SIZE / 2, y + POINT_SIZE / 2);
            } else {
                g2.fillOval(x, y, POINT_SIZE, POINT_SIZE);
                g2.drawLine(lastX + POINT_SIZE / 2, lastY + POINT_SIZE / 2, x + POINT_SIZE / 2, y + POINT_SIZE / 2);
            }
            lastX = x;
            lastY = y;
        }
    }
}
