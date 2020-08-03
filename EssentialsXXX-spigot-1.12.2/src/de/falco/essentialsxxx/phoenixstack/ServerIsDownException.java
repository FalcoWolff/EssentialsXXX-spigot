package de.falco.essentialsxxx.phoenixstack;

public class ServerIsDownException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ServerIsDownException(String me) {
		super(me);
	}

}
