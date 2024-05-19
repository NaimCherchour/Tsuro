package main.java.model;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Profile Manager prend un username en string et recherche si l'user existe dans users.xml afin de charger
 * ses parties sauvegardées
 */
public class ProfileManager {
    private static final String XML_FILE_PATH = "src/main/resources/sauvegarde/users.xml";

    public static Profile getProfile(String username) {
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
                    if (username.equalsIgnoreCase(storedUsername)) {
                        Element profileElement = (Element) element.getElementsByTagName("profile").item(0);
                        NodeList savedGamesNodeList = profileElement.getElementsByTagName("game");
                        List<String> savedGames = new ArrayList<>();
                        for (int j = 0; j < savedGamesNodeList.getLength(); j++) {
                            Node gameNode = savedGamesNodeList.item(j);
                            if (gameNode.getNodeType() == Node.ELEMENT_NODE) {
                                String gamePath = gameNode.getTextContent();
                                savedGames.add(gamePath);
                            }
                        }
                        // Construct Profile object
                        Profile userProfile = new Profile(storedUsername, savedGames);
                        return userProfile;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // profil non trouvé
    }

    public static void saveProfile(Profile profile) {
        try {
            File file = new File(XML_FILE_PATH);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();
            NodeList userList = doc.getElementsByTagName("user");

            for (int i = 0; i < userList.getLength(); i++) {
                Node userNode = userList.item(i);
                if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) userNode;
                    String storedUsername = userElement.getElementsByTagName("username").item(0).getTextContent();
                    if (profile.getUsername().equalsIgnoreCase(storedUsername)) {
                        // mettre à jour le fichier xml
                        Element profileElement = (Element) userElement.getElementsByTagName("profile").item(0);
                        updateSavedGamesInXML(profileElement, profile.getSavedGames());
                        saveXMLToFile(doc, file);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateSavedGamesInXML(Element profileElement, List<String> savedGames) {
        NodeList savedGamesNodeList = profileElement.getElementsByTagName("savedGames");
        if (savedGamesNodeList.getLength() > 0) {
            Element savedGamesElement = (Element) savedGamesNodeList.item(0);
            savedGamesElement.getParentNode().removeChild(savedGamesElement);
        }

        Document doc = profileElement.getOwnerDocument();
        Element newSavedGamesElement = doc.createElement("savedGames");
        for (String savedGame : savedGames) {
            Element gameElement = doc.createElement("game");
            gameElement.appendChild(doc.createTextNode(savedGame));
            newSavedGamesElement.appendChild(gameElement);
        }

        profileElement.appendChild(newSavedGamesElement);
    }

    private static void saveXMLToFile(Document doc, File file) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
