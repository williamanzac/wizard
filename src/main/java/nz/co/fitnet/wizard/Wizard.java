package nz.co.fitnet.wizard;

import java.util.Iterator;

public class Wizard {
	private WizardStep activeStep;
	private final WizardModel model;

	private final boolean overviewVisible = true;

	private boolean canceled = false;

	public Wizard(final WizardModel model) {
		if (model == null) {
			throw new NullPointerException("models is null");
		}

		this.model = model;
		this.model.addPropertyChangeListener(evt -> {
			if (evt.getPropertyName().equals("activeStep")) {
				handleStepChange();
			}
		});

		for (final Iterator<WizardStep> iter = model.stepIterator(); iter.hasNext();) {
			iter.next().init(this.model);
		}

		this.model.reset();
	}

	public void reset() {
		canceled = false;
		getModel().reset();
	};

	public WizardModel getModel() {
		return model;
	}

	public boolean isOverviewVisible() {
		return overviewVisible;
	}

	public void cancel() {
		final WizardStep activeStep = getModel().getActiveStep();
		if (activeStep != null && activeStep.isBusy()) {
			activeStep.abortBusy();
		}

		canceled = true;
	}

	public boolean wasCanceled() {
		return canceled;
	}

	public void nextStep() throws InvalidStateException {
		model.getActiveStep().applyState();
		model.nextStep();
	}

	public void previousStep() {
		model.previousStep();
	}

	public void lastStep() throws InvalidStateException {
		model.getActiveStep().applyState();
		model.lastStep();
	}

	public void finis() throws InvalidStateException {
		final WizardStep finishStep = model.getActiveStep();
		finishStep.applyState();
	}

	private void handleStepChange() {
		activeStep = model.getActiveStep();

		activeStep.prepare();
	}
}
