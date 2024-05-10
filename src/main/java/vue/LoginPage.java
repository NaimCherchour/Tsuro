package main.java.vue;

import main.java.menu.MainMenu;
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
import java.util.Arrays;
import java.util.Objects;

/**
 * représente la vue de la page de Connexion/Inscription
 */
public class LoginPage implements LoginListener {

	private JFrame frame; // le frame de l'application
	private JButton loginButton;
	private JTextField userName;
	private JPasswordField password;
	private JToggleButton showPasswordButton;
	private JLabel usernameError;
	private JLabel passwordError;
	private UserLogin userLog ; // Le modèle : gestion de la connexion et de l'Ajout de l'utilisateur


	public LoginPage(JFrame frame) throws IOException {
		this.frame	= frame;
		userLog = new UserLogin(this);
		userName = new JTextField() {
	 		protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		  }
		  public void updateUI() {
		    super.updateUI();
		    setOpaque(false);
		  }
		};

		password = new JPasswordField() {
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setPaint(getBackground());
				g2.dispose();
				super.paintComponent(g);
			}
			public void updateUI() {
				super.updateUI();
				setOpaque(false);
			}
		};
		showPasswordButton = new JToggleButton("Show");
		showPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (showPasswordButton.isSelected()) {
					password.setEchoChar((char) 0); // Show password
					showPasswordButton.setText("Hide");
				} else {
					password.setEchoChar('*'); // Hide password
					showPasswordButton.setText("Show");
				}
			}
		});


		loginButton = new JButton("LOGIN") {
			protected void paintComponent(Graphics g) {
			    super.paintComponent(g);
			  }
			  public void updateUI() {
			    super.updateUI();
			    setOpaque(false);
			  }
		};
		usernameError = new JLabel();
		passwordError = new JLabel();

		ImageIcon backgroundImage = new ImageIcon("src/main/resources/fond.png");

		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Draw the background image
				g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
			}
		};

		backgroundPanel.setLayout(new BorderLayout());
		frame.setContentPane(backgroundPanel);

		init();
	}

	public void addEventListeners() {
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String trimmedUsername = userName.getText().trim();
				String trimmedPassword = String.valueOf(password.getPassword());
				trimmedPassword = trimmedPassword.trim();

				if (!trimmedUsername.isEmpty() && !trimmedUsername.equals("Enter your userName")
						&& validatePassword(trimmedPassword)) {
					if (userLog.login(trimmedUsername, trimmedPassword)) {
						MainMenu.playSound("src/main/resources/sound/buttonClickSound.wav");
						MainMenu.createAndShowGUI(frame, trimmedUsername);
					}
				}
			}
		});


		userName.getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e) {
				if(!userName.getText().isEmpty() && !userName.getText().equals("Enter your userName")
				   && !userName.getText().trim().isEmpty()) {
					usernameError.setForeground(new Color(18, 73, 21));
						usernameError.setText("userName is valid");}
					else {
						usernameError.setForeground(Color.RED);
						usernameError.setText("userName is not valid");
						usernameError.setText("");
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if(!userName.getText().isEmpty() && !userName.getText().equals("Enter your userName")
						&& !userName.getText().trim().isEmpty()) {
					usernameError.setForeground(new Color(18, 73, 21));
					usernameError.setText("userName is valid");
				}else {
						usernameError.setForeground(Color.RED);
						usernameError.setText("userName is not valid");
						usernameError.setText("");
				}

			}

			public void changedUpdate(DocumentEvent e) {
				if(!userName.getText().isEmpty() && !userName.getText().equals("Enter your userName")
						&& !userName.getText().trim().isEmpty()) {
						usernameError.setForeground(new Color(18, 73, 21));
					usernameError.setText("userName is valid");
					} else {
						usernameError.setForeground(Color.RED);
						usernameError.setText("userName is not valid");
						usernameError.setText("");
					}
			}
		});

		password.getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e) {
				String pass = String.valueOf(password.getPassword());
				if(!pass.isEmpty() && !pass.equals("Enter your password")) {
					if(validatePassword(pass)) {
						passwordError.setForeground(new Color(18, 73, 21));
						passwordError.setText("Password is valid");
					}
				}
				else {
					passwordError.setText("");
				}
			}

			public void insertUpdate(DocumentEvent e) {
				String pass = String.valueOf(password.getPassword());
				if(!pass.isEmpty() && !pass.equals("Enter your password")) {
					if(validatePassword(pass)) {
						passwordError.setForeground(new Color(18, 73, 21));
						passwordError.setText("Password is valid");
					}
				}
				else {
					passwordError.setText("");
				}
			}

			public void changedUpdate(DocumentEvent e) {
				String pass = String.valueOf(password.getPassword());
				if(!pass.isEmpty() && !pass.equals("Enter your password")) {
					if(validatePassword(pass)) {
						passwordError.setForeground(new Color(18, 73, 21));
						passwordError.setText("Password is valid");
					}
				}
				else {
					passwordError.setText("");
				}
			}
		});

		userName.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
				if(userName.getText().equals("")) {
					userName.setText("Enter your userName");
					userName.setForeground(Color.gray);
				}
			}

			public void focusGained(FocusEvent e) {
				if(userName.getText().equals("Enter your userName")) {
					userName.setText("");
					userName.setForeground(Color.black);
				}
			}
		});

		password.addFocusListener(new FocusListener() {

			public void focusLost(FocusEvent e) {
				String pass = String.valueOf(password.getPassword());
				if(pass.equals("")) {
					password.setText("Enter your password");
					password.setForeground(Color.gray);
					password.setEchoChar((char)0);
				}
			}

			public void focusGained(FocusEvent e) {
				String pass = String.valueOf(password.getPassword());
				if(pass.equals("Enter your password")) {
					password.setText("");
					password.setEchoChar('*');
					password.setForeground(Color.black);
				}
			}
		});
	}


	private boolean validatePassword(String text) {
		passwordError.setForeground(Color.RED);
		if(text.length() < 8 || text.equals("Enter your password")) {
			passwordError.setText("Invalid Password");
			return false;
		} else
			return true;
	}

	public void init() {
		userName.setPreferredSize(new Dimension(250,35));
		password.setPreferredSize(new Dimension(250,35));
		loginButton.setPreferredSize(new Dimension(250,35));
		loginButton.setBackground(new Color(66, 245, 114));
		loginButton.setFocusPainted(false);

		userName.setText("Enter your userName");
		userName.setForeground(Color.gray);
		password.setText("Enter your password");
		password.setForeground(Color.gray);
		password.setEchoChar((char)0);

		usernameError.setFont(new Font("SansSerif", Font.BOLD, 11));
		usernameError.setForeground(Color.RED);

		passwordError.setFont(new Font("SansSerif", Font.BOLD, 11));
		passwordError.setForeground(Color.RED);

		frame.setLayout(new GridBagLayout());

		Insets textInsets = new Insets(10, 120, 5, 10);
		Insets buttonInsets = new Insets(20, 120, 10, 10);
		Insets errorInsets = new Insets(0,120,0,0);

		GridBagConstraints input = new GridBagConstraints();
		input.anchor = GridBagConstraints.CENTER;
		input.insets = textInsets;
		input.gridy = 1;
		frame.add(userName,input);

		input.gridy = 2;
		input.insets = errorInsets;
		input.anchor = GridBagConstraints.WEST;
		frame.add(usernameError,input);

		input.gridy = 3;
		input.insets = textInsets;
		input.anchor = GridBagConstraints.CENTER;
		frame.add(password,input);

		input.gridy = 4;
		input.insets = errorInsets;
		input.anchor = GridBagConstraints.WEST;
		frame.add(passwordError,input);

		input.gridx = 1;
		input.gridy = 3 ;
		input.insets = new Insets(15, 0, 10, 10);
		input.anchor = GridBagConstraints.EAST;
		frame.add(showPasswordButton, input);

		input.insets = buttonInsets;
		input.anchor = GridBagConstraints.WEST;
		input.gridx = 0;
		input.gridy = 5;
		frame.add(loginButton,input);

		frame.setSize(1065, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);

		frame.requestFocus();
		addEventListeners();
	}


	@Override
	public void notifySuccessLog() {
		String message = "<html><div style='text-align: center;'>"
				+ "<h1 style='color: #32CD32;'> Welcome Back </h1>"
				+ "</div></html>";

		JOptionPane.showMessageDialog(null, message, "Successfully Logged", JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void notifyWrongPass() {
		String message = "<html><div style='text-align: center;'>"
				+ "<h1 style='color: #FF5733;'>Password Incorrect !</h1>"
				+ "<p>Retry by clicking Yes.</p>"
				+ "</div></html>";

		int option = JOptionPane.showConfirmDialog(null, message, "Wrong Password", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.YES_OPTION) {
			password.setText("");
			password.requestFocus();
		} else {
			userName.setText("");
			userName.requestFocus();
			password.setText("");
		}
	}

	@Override
	public void notifyNewUser() {
		String message = "<html><div style='text-align: center;'>"
				+ "<h1 style='color: #32CD32;'> Welcome To Tsuro Game </h1>"
				+ "</div></html>";

		JOptionPane.showMessageDialog(null, message, "New User", JOptionPane.INFORMATION_MESSAGE);
	}
}
