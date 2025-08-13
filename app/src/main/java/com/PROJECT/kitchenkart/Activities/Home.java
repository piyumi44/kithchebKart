package com.PROJECT.kitchenkart.Activities;

public class Home {

    import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class EcommerceHomePage {

        public static void main(String[] args) {
            // Create the main frame
            JFrame frame = new JFrame("Ecommerce");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(375, 667); // Standard mobile size
            frame.setLayout(new BorderLayout());

            // Create a panel for the content with vertical layout
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            JScrollPane scrollPane = new JScrollPane(mainPanel);

            // Header section
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // App name/title
            JLabel appTitle = new JLabel("KITCHENKART");
            appTitle.setFont(new Font("Arial", Font.BOLD, 20));
            appTitle.setHorizontalAlignment(SwingConstants.CENTER);
            headerPanel.add(appTitle, BorderLayout.CENTER);

            // Navigation menu (D41)
            JPanel navPanel = new JPanel();
            navPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton menuButton = new JButton("D41");
            menuButton.setFont(new Font("Arial", Font.PLAIN, 12));
            navPanel.add(menuButton);

            // Add dropdown menu functionality
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(new JMenuItem("Search"));
            popupMenu.add(new JMenuItem("Favorites"));
            popupMenu.add(new JMenuItem("History"));
            popupMenu.add(new JMenuItem("Following"));

            menuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    popupMenu.show(menuButton, 0, menuButton.getHeight());
                }
            });

            // Categories section
            JPanel categoriesPanel = new JPanel();
            categoriesPanel.setLayout(new GridLayout(1, 4, 10, 0));
            categoriesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            String[] categories = {"Bakery", "Fruits", "Juice", "Rice"};
            for (String category : categories) {
                JButton categoryButton = new JButton(category);
                categoryButton.setFont(new Font("Arial", Font.PLAIN, 12));
                categoriesPanel.add(categoryButton);
            }

            // Products section
            JPanel productsPanel = new JPanel();
            productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));
            productsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Product 1
            JPanel product1 = createProductPanel("Dried", "Biriyani", "Rs.700.00");
            productsPanel.add(product1);
            productsPanel.add(Box.createVerticalStrut(10));

            // Product 2
            JPanel product2 = createProductPanel("Dried", "Panthe", "Rs.50.00");
            productsPanel.add(product2);

            // Footer section
            JPanel footerPanel = new JPanel();
            footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            JLabel footerLabel = new JLabel("Fill (148) × 22 Hug");
            footerPanel.add(footerLabel);

            // Add all components to main panel
            mainPanel.add(headerPanel);
            mainPanel.add(navPanel);
            mainPanel.add(categoriesPanel);
            mainPanel.add(productsPanel);
            mainPanel.add(footerPanel);

            // Add to frame and display
            frame.add(scrollPane, BorderLayout.CENTER);
            frame.setVisible(true);
        }

        private static JPanel createProductPanel(String type, String name, String price) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JLabel typeLabel = new JLabel(type);
            typeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
            typeLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));

            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

            JLabel priceLabel = new JLabel(price);
            priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
            priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
            priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(nameLabel, BorderLayout.CENTER);
            centerPanel.add(priceLabel, BorderLayout.EAST);

            panel.add(typeLabel, BorderLayout.NORTH);
            panel.add(centerPanel, BorderLayout.CENTER);

            return panel;
        }
    }

}
