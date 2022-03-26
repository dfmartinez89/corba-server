package server;

import org.omg.CORBA.ORB;
import org.omg.CORBA.StringHolder;

import BufferApp._BufferImplBase;

/**
 * Implementacion de la funcionalidad del componente buffer
 * @author Manuel Martin Gonzalez
 *
 */
class BufferImpl extends _BufferImplBase {

	private static final long serialVersionUID = 1L;
	private ORB orb;
	private String buf[];
	private int elementos;
	private static int maxElementos = 5;
	private static int minElementosGet = 3;

	/**
	 * Implementacion del metodo constructor del buffer
	 * @param orb_val ORB
	 */
	BufferImpl(ORB orb_val) {
		buf = new String[maxElementos];
		elementos = 0;
		this.orb = orb_val;
	}

	/**
	 * Implementacion del metodo PUT
	 */
	public boolean put(String elemento) {
		if (elementos < maxElementos) {
			buf[elementos] = elemento;
			elementos++;
			System.out
					.println(buf[elementos - 1] + "\tElementos: " + elementos);
			return true;
		} else {
			//elemento = "Pila llena";
			//System.out.println("PILA LLENA");
			return false;
		}
	}

	/**
	 * Implementacion del metodo GET
	 */
	public boolean get(org.omg.CORBA.StringHolder elemento) {
		int i;
		if (elementos > minElementosGet) {
			elemento.value = buf[0];
			for (i = 0; i < maxElementos - 1; i++)
				buf[i] = buf[i + 1];
			elementos--;
			return true;
		} else {
			elemento.value = "Solo introducir o leer";
			return false;
		}
	}

	/**
	 * Implementacion del metodo READ
	 */
	public boolean read(org.omg.CORBA.StringHolder elemento) {
		if (elementos != 0) {
			elemento.value = buf[0];
			return true;
		} else
			return false;
	}

	/**
	 * Implementacion del metodo READALL
	 */
	public boolean readAll(StringHolder total_elemento) {
		if (elementos != 0) {
			StringBuilder total = new StringBuilder();
			String mensaje = "";
			if(elementos==1){
				return read(total_elemento);
			}else{
				for (int i = 0; i < elementos; i++) {
					 if(i==elementos-1){
							mensaje = buf[i];
							mensaje = mensaje.substring(mensaje.indexOf(">") + 1); //quitar cabecera
							mensaje = mensaje.substring(mensaje.indexOf(">") + 1); //quitar segundo nodo
							total.append(mensaje);
						}
					 else if(i>=1){
						mensaje = buf[i];
						mensaje = mensaje.substring(mensaje.indexOf(">") + 1); //quitar cabecera
						mensaje = mensaje.substring(mensaje.indexOf(">") + 1); //quitar segundo nodo
						mensaje = mensaje.substring(0, mensaje.length()-"</documentoProductorConsumidor>".length()); //obtener mensaje
						total.append(mensaje);
					}
					else{ //nodo 0
						mensaje = buf[i];
						mensaje = mensaje.substring(0, mensaje.indexOf("</documentoProductorConsumidor>")); //obtener mensaje
						total.append(mensaje);
					}
					if(i<buf.length-1) total.append("|");
				}
				total_elemento.value = total.toString();
				return true;
			}
		} else
			return false;
	}

	/**
	 * Implementacion del metodo SHUTDOWN
	 */
	public void shutdown() {
		orb.shutdown(false);
	}

}