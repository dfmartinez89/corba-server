package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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
 * Decodificador de archivo XML (desempaquetador)
 * @author Manuel Martin Gonzalez
 *
 */
public class DecoderXML {

	private File archivoXML;

	/**
	 * Constructor de objeto desempaquetador a partir de una ruta de archivo (nombre incluido) XML
	 * @param ruta_archivo cadena que representa la ruta del archivo (nombre incluido)
	 */
	public DecoderXML(String ruta_archivo) {
		this.archivoXML = new File(ruta_archivo);
	}

	/**
	 * Desempaquetar datos de archivo XML
	 * @return par de valores que contienen los mensajes del archivo XML y nos indica si el archivo ha sido desempaquetado correctamente
	 */
	public Pair<Boolean, ArrayList<Mensaje>> desempaquetar() {
		ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
		Mensaje nuevo = null;
		String mail = "";
		String elemento = "";
		try {
	           DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	           DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	           Document doc = dBuilder.parse(archivoXML);
	           doc.getDocumentElement().normalize();
	           //System.out.println("Elemento raiz :" + doc.getDocumentElement().getNodeName());
	           NodeList nList = doc.getElementsByTagName("mensaje");
	           for (int temp = 0; temp < nList.getLength(); temp++) {
	             Node nNode = nList.item(temp);
	             //System.out.println("\nCurrent Elemento :" + nNode.getNodeName());
	             if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	                     Element eElement = (Element) nNode;
	                     //System.out.println("Atributo : " + eElement.getAttribute("id"));
	                     mail = eElement.getElementsByTagName("email").item(0).getTextContent();
	                     elemento = eElement.getElementsByTagName("elemento").item(0).getTextContent();
	                     nuevo = new Mensaje(mail, elemento);
	                     mensajes.add(nuevo);
	              }
	          }
	          return new Pair<Boolean, ArrayList<Mensaje>>(true, mensajes);
		} catch (SAXParseException e) {
			System.err.println(e.getMessage());
		} catch (SAXException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
		} catch (NullPointerException e) {
			System.err.println(e.getMessage());
		}
		return new Pair<Boolean, ArrayList<Mensaje>>(false, null);
	}

	/**
	 * Obtener ruta del archivo XML
	 * @return cadena que representa la ruta del archivo
	 */
	public String getRutaArchivoXML() {
		return archivoXML.getAbsolutePath();
	}

	/**
	 * Obtener documento XML en forma de cadena
	 * @return cadena que representa el documento XML
	 */
	public String obtenerXMLComoString() {
		StringBuilder sb = new StringBuilder();
		BufferedReader br;
		String sCurrentLine;
		try {
			br = new BufferedReader(new FileReader(getRutaArchivoXML()));
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}
			br.close();
		} catch (FileNotFoundException e1) {
			System.err.println(e1.getMessage());
		} catch (IOException e2) {
			System.err.println(e2.getMessage());
		}
		return sb.toString();
	}
}
