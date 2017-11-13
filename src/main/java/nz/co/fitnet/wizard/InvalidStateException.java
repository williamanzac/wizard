package nz.co.fitnet.wizard;

public class InvalidStateException extends Exception {
	private static final long serialVersionUID = -7929630604327583767L;
	private boolean showUser = true;

	public InvalidStateException() {
		showUser = false;
	}

	public InvalidStateException(final String message) {
		this(message, true);
	}

	public InvalidStateException(final String message, final Throwable cause) {
		this(message, cause, true);
	}

	public InvalidStateException(final String message, final boolean showUser) {
		super(message);
		this.showUser = showUser;
	}

	public InvalidStateException(final String message, final Throwable cause, final boolean showUser) {
		super(message, cause);
		this.showUser = showUser;
	}

	public boolean isShowUser() {
		return showUser;
	}

	public void setShowUser(final boolean showUser) {
		this.showUser = showUser;
	}
}
