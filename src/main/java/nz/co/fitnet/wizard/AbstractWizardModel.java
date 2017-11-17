package nz.co.fitnet.wizard;

public abstract class AbstractWizardModel<S extends WizardStep<?>> implements WizardModel<S> {
	private S activeStep;
	private boolean previousAvailable;
	private boolean nextAvailable;
	private boolean lastAvailable;
	private boolean cancelAvailable;

	@Override
	public S getActiveStep() {
		return activeStep;
	}

	protected void setActiveStep(final S activeStep) {
		this.activeStep = activeStep;
		refreshModelState();
	}

	@Override
	public boolean isPreviousAvailable() {
		return previousAvailable;
	}

	@Override
	public boolean isNextAvailable() {
		return nextAvailable;
	}

	@Override
	public boolean isLastAvailable() {
		return lastAvailable;
	}

	protected void setPreviousAvailable(final boolean previousAvailable) {
		this.previousAvailable = previousAvailable;
	}

	protected void setNextAvailable(final boolean nextAvailable) {
		this.nextAvailable = nextAvailable;
	}

	protected void setLastAvailable(final boolean lastAvailable) {
		this.lastAvailable = lastAvailable;
	}

	protected void setCancelAvailable(final boolean cancelAvailable) {
		this.cancelAvailable = cancelAvailable;
	}

	@Override
	public void refreshModelState() {
	}

	public boolean isCancelAvailable() {
		return cancelAvailable;
	}
}
