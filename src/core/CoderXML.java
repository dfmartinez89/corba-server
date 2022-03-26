package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Codificador de archivo XML (empaquetador)
 * @author Manuel Martin Gonzalez
 *
 */
public class CoderXML {
	
	private String ruta_archivo;
	private String nombre_archivo;
	
	/**
	 * Constructor de objeto empaquetador a partir de un nombre de archivo XML (el archivo se creara en una carpeta en el directorio home del usuario)
	 * @param nombre_archivo cadena que representa el nombre del archivo
	 */
	public CoderXML(String nombre_archivo){
		this.nombre_archivo = nombre_archivo;
		String dir = System.getProperty("user.home")+File.separator+"productor_consumidor_files";
		File XMLdirectory = new File(dir);
		if (!XMLdirectory.exists()) XMLdirectory.mkdir();
		this.ruta_archivo = dir+File.separator+nombre_archivo + ".xml";
	}

	/**
	 * Empaquetar datos en archivo XML
	 * @param emails lista de cadenas que representan los mails de los usuarios
	 * @param elementos lista de cadenas que representan los elementos (datos) de los usuarios
	 * @return si el archivo ha sido empaquetado correctamente
	 */
	public boolean empaquetar(ArrayList<String> emails,
			ArrayList<String> elementos) {
		if (emails.isEmpty() || elementos.isEmpty() || emails.size() != elementos.size()) {
			System.out.println("ERROR empty ArrayList");
			return false;
		} else {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				System.err.println(e.getMessage());
				return false;
			}
			DOMImplementation implementation = builder.getDOMImplementation();
			Document document = implementation.createDocument(null, nombre_archivo, null);
			document.setXmlVersion("1.0");
			// Main Node
			Element raiz = document.getDocumentElement();
			// Por cada mensaje creamos un registro mensaje que
			// contendra el email y su dato (elemento)
			for (int i = 0; i < elementos.size(); i++) {
				// Nodo mensaje
				Element punNode = document.createElement("mensaje");
				// Nodo email
				Element nomNode = document.createElement("email");
				Text nodeNomValue = document.createTextNode(emails.get(i));
				nomNode.appendChild(nodeNomValue);
				// Nodo elemento
				Element valNode = document.createElement("elemento");
				Text nodeValueValue = document.createTextNode(elementos.get(i));
				valNode.appendChild(nodeValueValue);
				// Añade los nodos email y elemento al nodo mensaje
				punNode.appendChild(nomNode);
				punNode.appendChild(valNode);
				// se añade el mensaje a la raiz
				raiz.appendChild(punNode); // pegamos el elemento mensaje a
											// la raiz "Documento"
			}
			// Generate XML
			Source source = new DOMSource(document);
			// Indicamos donde lo queremos almacenar
			
			Result result = new StreamResult(new java.io.File(ruta_archivo));
			Transformer transformer;
			try {
				transformer = TransformerFactory.newInstance()
						.newTransformer();
			} catch (TransformerConfigurationException e) {
				System.err.println(e.getMessage());
				return false;
			} catch (TransformerFactoryConfigurationError e) {
				System.err.println(e.getMessage());
				return false;
			}
			try {
				transformer.transform(source, result);
			} catch (TransformerException e) {
				System.err.println(e.getMessage());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Crear documento XML (con un mensaje) a partir de una cadena
	 * @param elemXML cadena que representa el archivo XML con un mensaje
	 * @return si el archivo ha sido creado correctamente
	 */
	public boolean empaquetar(String elemXML){
		if(ruta_archivo.compareTo("")!=0){
			try {
				PrintWriter pw = new PrintWriter(getRutaArchivoXML());
				pw.write(elemXML);
				pw.close();
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				return false;
			}
		}else{
			return false;
		}
		return true;
	}
	
	/**
	 * Crear documento XML (con varios mensajes) a partir de una cadena
	 * @param elemXML cadena que representa el archivo XML con varios mensaje
	 * @return si el archivo ha sido creado correctamente
	 */
	public boolean empaquetarTodos(String elemXML){
		if(ruta_archivo.compareTo("")!=0){
			try {
				PrintWriter pw = new PrintWriter(getRutaArchivoXML());
				StringTokenizer st = new StringTokenizer(elemXML, "|");
				while(st.hasMoreTokens()) {
					pw.write(st.nextToken());
				}
				pw.close();
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				return false;
			}
		}else{
			return false;
		}
		return true;
	}

	/**
	 * Obtener ruta del archivo XML
	 * @return cadena que representa la ruta del archivo
	 */
	public String getRutaArchivoXML() {
		return this.ruta_archivo;
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