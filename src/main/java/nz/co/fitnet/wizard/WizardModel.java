package nz.co.fitnet.wizard;

import java.util.Iterator;

public interface WizardModel<S extends WizardStep<?>> {
	boolean isPreviousAvailable();

	boolean isNextAvailable();

	boolean isLastAvailable();

	void nextStep();

	void previousStep();

	void lastStep();

	void reset();

	S getActiveStep();

	boolean isLastStep(S step);

	Iterator<S> stepIterator();

	void refreshModelState();
}
