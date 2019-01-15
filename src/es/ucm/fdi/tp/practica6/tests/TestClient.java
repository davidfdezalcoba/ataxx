package es.ucm.fdi.tp.practica6.tests;

import es.ucm.fdi.tp.practica6.Main;

public class TestClient {

	public static void main(String[] args) {
		String[] as = { "-am", "client", "-aialg", "minmaxab", "-md", "5" };
		Main.main(as);
	}
}
