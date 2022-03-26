package core;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Valida sintacticamente un archivo XML
 * @author Manuel Martin Gonzalez
 *
 */
public class Parser {
	
	private String ruta_nombre_xmlFile;
	
	/**
	 * Constructor de un objeto que valide sintacticamente un archivo XML
	 * @param nombre_xmlFile ruta (nombre incluido) del archivo XML a validar
	 */
	public Parser(String nombre_xmlFile){
		this.ruta_nombre_xmlFile = nombre_xmlFile;
	}
	
	/**
	 * Validar sintacticamente un arhivo XML
	 * @return si el arhivo es valido
	 */
	public boolean validar() {
		try {
	           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	           Document doc = dBuilder.parse(this.ruta_nombre_xmlFile);
	           doc.getDocumentElement().normalize();
	           //System.out.println("Elemento raiz :" + doc.getDocumentElement().getNodeName());
	           NodeList nList = doc.getElementsByTagName("mensaje");
	           for (int temp = 0; temp < nList.getLength(); temp++) {
	             Node nNode = nList.item(temp);
	             //System.out.println("\nCurrent Elemento :" + nNode.getNodeName());
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                     Element eElement = (Element) nNode;
	                     //System.out.println("Atributo : " + eElement.getAttribute("id"));
	                     eElement.getElementsByTagName("email").item(0).getTextContent();
	                     eElement.getElementsByTagName("elemento").item(0).getTextContent();
	              }
	          }
	          return true;
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
		} catch (SAXParseException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		}
		return false;
	}
}
