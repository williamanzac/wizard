package nz.co.fitnet.wizard;

import java.util.Iterator;

public class Wizard<M extends WizardModel<S>, S extends WizardStep<M>> {
	private S activeStep;
	private final M model;

	private final boolean overviewVisible = true;

	private boolean canceled = false;

	public Wizard(final M model) {
		if (model == null) {
			throw new NullPointerException("models is null");
		}

		this.model = model;

		for (final Iterator<S> iter = model.stepIterator(); iter.hasNext();) {
			iter.next().init(this.model);
		}

		this.model.reset();
	}

	public void reset() {
		canceled = false;
		getModel().reset();
	}

	public M getModel() {
		return model;
	}

	public boolean isOverviewVisible() {
		return overviewVisible;
	}

	public void cancel() {
		final S activeStep = getModel().getActiveStep();
		if (activeStep != null && activeStep.isBusy()) {
			activeStep.abortBusy();
		}

		canceled = true;
	}

	public boolean wasCanceled() {
		return canceled;
	}

	public S getActiveStep() {
		return getModel().getActiveStep();
	}

	public void nextStep() throws InvalidStateException {
		model.getActiveStep().applyState();
		model.nextStep();
		handleStepChange();
	}

	public void previousStep() {
		model.previousStep();
		handleStepChange();
	}

	public void lastStep() throws InvalidStateException {
		model.getActiveStep().applyState();
		model.lastStep();
		handleStepChange();
	}

	public void finish() throws InvalidStateException {
		final S finishStep = model.getActiveStep();
		finishStep.applyState();
	}

	protected void handleStepChange() {
		activeStep = model.getActiveStep();
		activeStep.prepare();
	}
}
