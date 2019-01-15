package es.ucm.fdi.tp.practica6.responses;

import es.ucm.fdi.tp.basecode.bgame.model.GameObserver;

/**
 * Represents an error response.
 * 
 * @author David Fdez Alcoba, Manuel Sanchez Torron
 *
 */
public class ErrorResponse implements Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4419686163712742040L;
	private String msg;

	public ErrorResponse(String msg) {
		this.msg = msg;
	}

	@Override
	public void run(GameObserver o) {
		// TODO Auto-generated method stub
		o.onError(msg);
	}

}
