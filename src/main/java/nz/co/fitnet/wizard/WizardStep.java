package nz.co.fitnet.wizard;

import java.beans.PropertyChangeListener;

public interface WizardStep {
	String getName();

	String getSummary();

	String getIcon();

	String getView();

	boolean isComplete();

	boolean isBusy();

	void init(WizardModel model);

	void prepare();

	void applyState() throws InvalidStateException;

	void abortBusy();

	void addPropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
