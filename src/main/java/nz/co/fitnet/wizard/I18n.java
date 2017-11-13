package nz.co.fitnet.wizard;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.KeyStroke;

public class I18n {
	private static ResourceBundle bundle = null;

	private static ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle("nz-co-fitnet-wizard");
		}

		return bundle;
	}

	public static void setBundle(final ResourceBundle bundle) {
		I18n.bundle = bundle;
	}

	public static String getString(final String key) {
		return getBundle().getString(key);
	}

	public static Object getObject(final String key) {
		return getBundle().getObject(key);
	}

	public static String[] getStringArray(final String key) {
		return getBundle().getStringArray(key);
	}

	public static int getMnemonic(final String key) {
		final String mnemonicString = getBundle().getString(key);

		if (mnemonicString == null) {
			throw new MissingResourceException("Missing resource: " + key, I18n.class.getName(), key);
		}

		if (mnemonicString.length() != 1) {
			throw new IllegalStateException("mnemonic string invalid: " + mnemonicString);
		}

		final KeyStroke ks = KeyStroke.getKeyStroke(mnemonicString.toUpperCase());

		if (ks == null) {
			throw new IllegalStateException("mnemonic string invalid: " + mnemonicString);
		}

		return ks.getKeyCode();
	}
}
