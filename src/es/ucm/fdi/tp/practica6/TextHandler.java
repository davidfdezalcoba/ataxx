package es.ucm.fdi.tp.practica6;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import javax.swing.JTextArea;

public class TextHandler extends Handler {

	private JTextArea jta;

	public TextHandler(JTextArea jta) {
		this.jta = jta;
		LogManager manager = LogManager.getLogManager();
		String className = this.getClass().getName();
		String level = manager.getProperty(className + ".level");
		setLevel(level != null ? Level.parse(level) : Level.INFO);
	}

	@Override
	public void close() throws SecurityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void publish(LogRecord record) {
		// TODO Auto-generated method stub
		String message = null;
		if (!isLoggable(record))
			return;
		message = getFormatter().format(record);
		jta.append(message);
	}

}
