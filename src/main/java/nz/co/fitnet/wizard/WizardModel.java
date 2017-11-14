package nz.co.fitnet.wizard;

import java.beans.PropertyChangeListener;
import java.util.Iterator;

public interface WizardModel<S extends WizardStep<?>> {
	boolean isPreviousAvailable();

	boolean isNextAvailable();

	boolean isLastAvailable();

	void nextStep();

	void previousStep();

	void lastStep();

	boolean isLastVisible();

	void reset();

	S getActiveStep();

	boolean isLastStep(S step);

	Iterator<S> stepIterator();

	void addPropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

	void refreshModelState();
}
