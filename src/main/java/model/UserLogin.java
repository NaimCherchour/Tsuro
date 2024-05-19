package main.java.model;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import main.java.vue.LoginListener;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Inscrire ou connecter un utilisateur en rechant les données des utilisateurds déjà existant dans le fichier
 * users.xml à condition que le mot de passe crypté soit correct, sinon on inscrit le nouveau joueur
 */

public class UserLogin {
    private static final String XML_FILE_PATH = "src/main/resources/sauvegarde/users.xml";
    private LoginListener loginListener;

    public UserLogin(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public boolean login(String username, String password) {
        try {
            File file = new File(XML_FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String storedUsername = element.getElementsByTagName("username").item(0).getTextContent();
                    String storedPassword = element.getElementsByTagName("password").item(0).getTextContent();

                    if (username.equalsIgnoreCase(storedUsername)) {
                        if (validatePassword(password, storedPassword)) {
                            System.out.println("Login successful!");
                            loginListener.notifySuccessLog();
                            return true ;
                        } else {
                            System.out.println("Incorrect password. Please try again.");
                            loginListener.notifyWrongPass();
                            return false;
                        }
                    }
                }
            }

            System.out.println("New user created successfully!");
            createUser(username, password);
            loginListener.notifyNewUser();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
    }

    private boolean validatePassword(String password, String storedPassword) {
        String hashedPassword = PasswordEncryptor.encryptPassword(password);
        return hashedPassword.equals(storedPassword);
    }

    private void createUser(String username, String password) {
        try {
            File file = new File(XML_FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            Element root = doc.getDocumentElement();

            Element newUser = doc.createElement("user");

            Element newUsername = doc.createElement("username");
            newUsername.appendChild(doc.createTextNode(username));
            newUser.appendChild(newUsername);

            Element newPassword = doc.createElement("password");
            String encryptedPassword = PasswordEncryptor.encryptPassword(password);
            newPassword.appendChild(doc.createTextNode(encryptedPassword));
            newUser.appendChild(newPassword);

            Element profile = doc.createElement("profile");
            Element savedGames = doc.createElement("savedGames");
            profile.appendChild(savedGames);
            newUser.appendChild(profile);
            root.appendChild(newUser);

            writeXMLToFile(doc, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void writeXMLToFile(Document doc, File file) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(source, result);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(result.getWriter().toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     static class PasswordEncryptor {
        public static String encryptPassword(String password) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] hashBytes = messageDigest.digest(password.getBytes());
                StringBuilder s = new StringBuilder();
                for (byte b : hashBytes) {
                    s.append(String.format("%02x", b));
                }
                return s.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

