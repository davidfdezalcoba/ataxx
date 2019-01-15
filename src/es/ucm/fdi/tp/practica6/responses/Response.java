package es.ucm.fdi.tp.practica6.responses;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;

/**
 * Represents a Response that the server gives to the clients connected. When a
 * client receives a response, the run method should be invoked.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public interface Response extends java.io.Serializable {

	public void run(GameObserver o);

}