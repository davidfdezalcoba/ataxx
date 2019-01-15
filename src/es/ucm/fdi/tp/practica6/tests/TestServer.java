package es.ucm.fdi.tp.practica6.tests;

import es.ucm.fdi.tp.practica6.Main;

public class TestServer {

	public static void main(String[] args) {
		String[] as = { "-am", "server", "-g", "ataxx", "-d", "7x7", "-aialg", "minmaxab", "-md", "3"};
		Main.main(as);
	}
}
