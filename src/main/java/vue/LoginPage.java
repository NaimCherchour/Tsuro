package main.java.vue;

import main.java.menu.MainMenu;
import main.java.menu.Option;
import main.java.model.UserLogin;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * Représente la vue de la page de Connexion/Inscription.
 */
public class LoginPage implements LoginListener {

    private JFrame frame; // le frame de l'application
    private JButton loginButton;
    private JTextField userName;
    private JPasswordField password;
    private JToggleButton showPasswordButton;
    private JLabel usernameError;
    private JLabel passwordError;
    private UserLogin userLog; // Le modèle : gestion de la connexion et de l'Ajout de l'utilisateur

    private Boolean isConnected = false ;
    private Font pixelFont;




    /**
     * Constructeur de la classe LoginPage.
     *
     * @param frame Le frame de l'application.
     * @throws IOException En cas d'erreur lors de la lecture de l'image de fond.
     */
    public LoginPage(JFrame frame) throws IOException, FontFormatException {
        this.frame = frame;
        userLog = new UserLogin(this);

        // Charger la police pixel art
        try (InputStream is = new FileInputStream("src/main/resources/pixel_font.ttf")) {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.BOLD, 16);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        userName = new RoundedTextField(20);
        userName.setBackground(Color.WHITE);
        userName.setFont(pixelFont);

        password = new RoundedPasswordField(20);
        password.setBackground(Color.WHITE);
        password.setFont(pixelFont);

        showPasswordButton = new RoundedToggleButton("Afficher", 20);
        showPasswordButton.setFont(pixelFont);
        showPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordButton.isSelected()) {
                    password.setEchoChar((char) 0); // Afficher le mot de passe
                    showPasswordButton.setText("Masquer");
                } else {
                    password.setEchoChar('*'); // Masquer le mot de passe
                    showPasswordButton.setText("Afficher");
                }
            }
        });

        loginButton = new RoundedButton("CONNEXION", 20);
        loginButton.setBackground(new Color(189, 224, 140));
        loginButton.setForeground(Color.BLACK);
        loginButton.setFont(pixelFont);

        usernameError = new JLabel();
        usernameError.setFont(pixelFont);
        passwordError = new JLabel();
        passwordError.setFont(pixelFont);

        ImageIcon backgroundImage = new ImageIcon("src/main/resources/fond.png");

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dessiner l'image de fond
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        frame.setContentPane(backgroundPanel);

        init();
    }

    /**
     * Ajoute les écouteurs d'événements aux composants graphiques.
     */
    public void addEventListeners() {
        // Nouvel ActionListener pour le bouton de connexion
        ActionListener loginAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String trimmedUsername = userName.getText().trim();
                String trimmedPassword = String.valueOf(password.getPassword()).trim();

                if (!trimmedUsername.isEmpty() && !trimmedUsername.equals("Entrez votre nom d'utilisateur")
                        && validatePassword(trimmedPassword)) {
                    if (userLog.login(trimmedUsername, trimmedPassword)) {
                        isConnected = true;
                        Option.playSound();
                        MainMenu.createAndShowGUI(frame, trimmedUsername);
                    }
                }
            }
        };

        loginButton.addActionListener(loginAction);

        // Ajout du KeyListener pour détecter la touche "Entrée"
        userName.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        password.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });

        // Reste des écouteurs pour les changements de document et les focus
        userName.getDocument().addDocumentListener(new DocumentListener() {
            public void removeUpdate(DocumentEvent e) {
                updateUsernameError();
            }

            public void insertUpdate(DocumentEvent e) {
                updateUsernameError();
            }

            public void changedUpdate(DocumentEvent e) {
                updateUsernameError();
            }

            private void updateUsernameError() {
                if (!userName.getText().isEmpty() && !userName.getText().equals("Entrez votre nom d'utilisateur")
                        && !userName.getText().trim().isEmpty()) {
                    usernameError.setForeground(new Color(18, 73, 21));
                    usernameError.setText("Nom d'utilisateur valide");
                } else {
                    usernameError.setForeground(Color.RED);
                    usernameError.setText("Nom d'utilisateur non valide");
                    usernameError.setText("");
                }
                usernameError.setFont(pixelFont); // Appliquer la police pixel art
            }
        });

        password.getDocument().addDocumentListener(new DocumentListener() {
            public void removeUpdate(DocumentEvent e) {
                updatePasswordError();
            }

            public void insertUpdate(DocumentEvent e) {
                updatePasswordError();
            }

            public void changedUpdate(DocumentEvent e) {
                updatePasswordError();
            }

            private void updatePasswordError() {
                String pass = String.valueOf(password.getPassword());
                if (!pass.isEmpty() && !pass.equals("Entrez votre mot de passe")) {
                    if (validatePassword(pass) ) {
                        passwordError.setForeground(new Color(18, 73, 21));
                        passwordError.setText("Mot de passe valide");
                    }
                } else {
                    passwordError.setText("");
                }
                passwordError.setFont(pixelFont); // Appliquer la police pixel art
            }
        });

        userName.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                if (userName.getText().equals("")) {
                    userName.setText("Entrez votre nom d'utilisateur");
                    userName.setForeground(Color.gray);
                }
            }

            public void focusGained(FocusEvent e) {
                if (userName.getText().equals("Entrez votre nom d'utilisateur")) {
                    userName.setText("");
                    userName.setForeground(Color.black);
                }
            }
        });

        password.addFocusListener(new FocusListener() {
            public void focusLost(FocusEvent e) {
                String pass = String.valueOf(password.getPassword());
                if (pass.equals("")) {
                    password.setText("Entrez votre mot de passe");
                    password.setForeground(Color.gray);
                    password.setEchoChar((char) 0);
                }
            }

            public void focusGained(FocusEvent e) {
                String pass = String.valueOf(password.getPassword());
                if (pass.equals("Entrez votre mot de passe")) {
                    password.setText("");
                    password.setEchoChar('*');
                    password.setForeground(Color.black);
                }
            }
        });
    }


    /**
     * Initialise l'interface graphique.
     */
    public void init() {
        userName.setPreferredSize(new Dimension(250, 35));
        password.setPreferredSize(new Dimension(250, 35));
        loginButton.setPreferredSize(new Dimension(250, 35));
        loginButton.setBackground(new Color(201, 228, 131));
        loginButton.setFocusPainted(false);

        userName.setText("Entrez votre nom d'utilisateur");
        userName.setForeground(Color.gray);
        userName.setFont(pixelFont); // Appliquer la police pixel art

        password.setText("Entrez votre mot de passe");
        password.setForeground(Color.gray);
        password.setEchoChar((char) 0);
        password.setFont(pixelFont); // Appliquer la police pixel art

        usernameError.setFont(pixelFont);
        usernameError.setForeground(Color.RED);

        passwordError.setFont(pixelFont);
        passwordError.setForeground(Color.RED);

        frame.setLayout(new GridBagLayout());

        Insets textInsets = new Insets(10, 120, 5, 10);
        Insets buttonInsets = new Insets(20, 120, 10, 10);
        Insets errorInsets = new Insets(0, 120, 0, 0);

        GridBagConstraints input = new GridBagConstraints();
        input.anchor = GridBagConstraints.CENTER;
        input.insets = textInsets;
        input.gridy = 1;
        frame.add(userName, input);

        input.gridy = 2;
        input.insets = errorInsets;
        input.anchor = GridBagConstraints.WEST;
        frame.add(usernameError, input);

        input.gridy = 3;
        input.insets = textInsets;
        input.anchor = GridBagConstraints.CENTER;
        frame.add(password, input);

        input.gridy = 4;
        input.insets = errorInsets;
        input.anchor = GridBagConstraints.WEST;
        frame.add(passwordError, input);

        input.gridx = 1;
        input.gridy = 3;
        input.insets = new Insets(15, 0, 10, 10);
        input.anchor = GridBagConstraints.EAST;
        frame.add(showPasswordButton, input);

        input.insets = buttonInsets;
        input.anchor = GridBagConstraints.WEST;
        input.gridx = 0;
        input.gridy = 5;
        frame.add(loginButton, input);

        frame.setSize(1065, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.requestFocus();
        addEventListeners();
    }


    private boolean validatePassword(String text) {
        if( !isConnected) {
            passwordError.setForeground(Color.RED);
        }
        passwordError.setFont(pixelFont); // Appliquer la police pixel art
        if (text.length() < 8 || text.equals("Entrez votre mot de passe" )) {
            passwordError.setText("Mot de passe invalide");
            return false;
        } else
            return true;
    }



    /**
     * Méthode appelée lorsque la connexion est réussie.
     */
    @Override
    public void notifySuccessLog() {
        passwordError.setForeground(new Color(18, 73, 21));
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #32CD32;'> Heureux De Vous Revoir </h1>"
                + "</div></html>";

        JOptionPane.showMessageDialog(null, message, "Connexion réussie", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Méthode appelée lorsque le mot de passe est incorrect.
     */
    @Override
    public void notifyWrongPass() {
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #FF5733;'>Mot de passe incorrect !</h1>"
                + "<p>Réessayez en cliquant sur Yes.</p>"
                + "</div></html>";

        int option = JOptionPane.showConfirmDialog(null, message, "Mauvais mot de passe", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            password.setText("");
            password.requestFocus();
        } else {
            userName.setText("");
            userName.requestFocus();
            password.setText("");
        }
    }

    /**
     * Méthode appelée lorsqu'un nouvel utilisateur est créé.
     */
    @Override
    public void notifyNewUser() {
        String message = "<html><div style='text-align: center;'>"
                + "<h1 style='color: #32CD32;'> Bienvenue dans le jeu Tsuro </h1>"
                + "</div></html>";

        JOptionPane.showMessageDialog(null, message, "Nouvel utilisateur", JOptionPane.INFORMATION_MESSAGE);
    }

    class RoundedTextField extends JTextField {
        private int radius;

        public RoundedTextField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.WHITE);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    class RoundedPasswordField extends JPasswordField {
        private int radius;

        public RoundedPasswordField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
            setBackground(Color.WHITE);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    class RoundedToggleButton extends JToggleButton {
        private int radius;

        public RoundedToggleButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setOpaque(false);
            setContentAreaFilled(false);
            setBackground(new Color(254, 254, 220)); // Couleur de fond modifiée
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setOpaque(false);
            setContentAreaFilled(false);
            setBackground(Color.WHITE);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
        }

        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }
}