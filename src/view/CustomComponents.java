package view;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CustomComponents {
    // Cohesive Sleek Dark Theme Color Palette
    public static final Color COLOR_BG = new Color(18, 18, 20);            // #121214 (Very dark grey)
    public static final Color COLOR_SIDEBAR = new Color(24, 24, 28);       // #18181C (Dark grey sidebar)
    public static final Color COLOR_CARD = new Color(30, 30, 36);          // #1E1E24 (Lighter dark grey card)
    public static final Color COLOR_INPUT = new Color(37, 37, 43);         // #25252B (Input fields background)
    public static final Color COLOR_BORDER = new Color(45, 45, 52);        // #2D2D34 (Borders)
    public static final Color COLOR_PURPLE = new Color(127, 86, 217);      // #7F56D9 (Accent Purple)
    public static final Color COLOR_PURPLE_HOVER = new Color(159, 122, 234); // #9F7AEA (Accent Hover)
    public static final Color COLOR_TEXT_PRIMARY = new Color(244, 244, 246); // Off-white
    public static final Color COLOR_TEXT_SECONDARY = new Color(160, 160, 165); // Light grey
    
    // Fonts
    public static final Font FONT_TITLE = new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Arial", Font.BOLD, 15);
    public static final Font FONT_BODY = new Font("Arial", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 12);

    // Custom Rounded Border
    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 12, 8, 12);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = 12;
            insets.top = 8;
            insets.right = 12;
            insets.bottom = 8;
            return insets;
        }
    }

    // Rounded Text Field
    public static class RoundedTextField extends JTextField {
        public RoundedTextField(int columns) {
            super(columns);
            setup();
        }

        public RoundedTextField() {
            super();
            setup();
        }

        private void setup() {
            setOpaque(false);
            setBackground(COLOR_INPUT);
            setForeground(COLOR_TEXT_PRIMARY);
            setCaretColor(COLOR_TEXT_PRIMARY);
            setFont(FONT_BODY);
            setBorder(new RoundedBorder(COLOR_BORDER, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    // Rounded Password Field
    public static class RoundedPasswordField extends JPasswordField {
        public RoundedPasswordField() {
            super();
            setup();
        }

        private void setup() {
            setOpaque(false);
            setBackground(COLOR_INPUT);
            setForeground(COLOR_TEXT_PRIMARY);
            setCaretColor(COLOR_TEXT_PRIMARY);
            setEchoChar('•');
            setFont(FONT_BODY);
            setBorder(new RoundedBorder(COLOR_BORDER, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    // Rounded Button
    public static class RoundedButton extends JButton {
        private final Color baseColor;
        private final Color hoverColor;
        private boolean isHovered = false;

        public RoundedButton(String text, Color baseColor, Color hoverColor) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
            
            setOpaque(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(FONT_SUBTITLE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (isHovered) {
                g2d.setColor(hoverColor);
            } else {
                g2d.setColor(baseColor);
            }
            
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    // Sleek JComboBox Styling
    public static class SleekComboBox<T> extends JComboBox<T> {
        public SleekComboBox(T[] items) {
            super(items);
            setup();
        }

        public SleekComboBox() {
            super();
            setup();
        }

        private void setup() {
            setBackground(COLOR_INPUT);
            setForeground(COLOR_TEXT_PRIMARY);
            setFont(FONT_BODY);
            setUI(new BasicComboBoxUI() {
                @Override
                protected JButton createArrowButton() {
                    JButton btn = new JButton() {
                        @Override
                        public void paint(Graphics g) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setColor(COLOR_TEXT_SECONDARY);
                            int[] xPoints = {3, 7, 11};
                            int[] yPoints = {6, 10, 6};
                            g2d.fillPolygon(xPoints, yPoints, 3);
                            g2d.dispose();
                        }
                    };
                    btn.setBorderPainted(false);
                    btn.setContentAreaFilled(false);
                    btn.setFocusable(false);
                    return btn;
                }
            });
            setBorder(new RoundedBorder(COLOR_BORDER, 15));
            setOpaque(false);
        }
    }

    // Sleek Table Component
    public static class SleekTable extends JTable {
        public SleekTable() {
            super();
            setup();
        }

        private void setup() {
            setBackground(COLOR_CARD);
            setForeground(COLOR_TEXT_PRIMARY);
            setGridColor(COLOR_BORDER);
            setRowHeight(30);
            setFont(FONT_BODY);
            setSelectionBackground(COLOR_PURPLE);
            setSelectionForeground(Color.WHITE);
            setOpaque(true);

            // Header Style
            JTableHeader header = getTableHeader();
            header.setBackground(COLOR_SIDEBAR);
            header.setForeground(Color.BLACK);
            header.setFont(FONT_SUBTITLE);
            header.setReorderingAllowed(false);
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));
            
            // Custom Renderer
            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    c.setForeground(COLOR_TEXT_PRIMARY);
                    
                    if (isSelected) {
                        c.setBackground(COLOR_PURPLE);
                    } else {
                        // Alternate row coloring
                        if (row % 2 == 0) {
                            c.setBackground(COLOR_CARD);
                        } else {
                            c.setBackground(new Color(26, 26, 32));
                        }
                    }
                    setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            };
            
            setDefaultRenderer(Object.class, cellRenderer);
            setDefaultRenderer(Integer.class, cellRenderer);
            setDefaultRenderer(String.class, cellRenderer);
        }
    }
}
