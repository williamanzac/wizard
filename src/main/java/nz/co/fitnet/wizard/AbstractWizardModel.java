package nz.co.fitnet.wizard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractWizardModel<S extends WizardStep<?>> implements WizardModel<S> {
	private S activeStep;
	private boolean previousAvailable;
	private boolean nextAvailable;
	private boolean lastAvailable;
	private boolean cancelAvailable;
	private boolean lastVisible = true;
	private final PropertyChangeSupport pcs;

	private final PropertyChangeListener completeListener = evt -> {
		if (evt.getPropertyName().equals("complete")) {
			refreshModelState();
		}
	};

	public AbstractWizardModel() {
		pcs = new PropertyChangeSupport(this);
	}

	@Override
	public S getActiveStep() {
		return activeStep;
	}

	protected void setActiveStep(final S activeStep) {
		if (this.activeStep != activeStep) {
			final S old = this.activeStep;
			this.activeStep = activeStep;
			pcs.firePropertyChange("activeStep", old, activeStep);
			refreshModelState();
		}
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
		if (this.previousAvailable != previousAvailable) {
			final boolean old = this.previousAvailable;
			this.previousAvailable = previousAvailable;
			pcs.firePropertyChange("previousAvailable", old, previousAvailable);
		}
	}

	protected void setNextAvailable(final boolean nextAvailable) {
		if (this.nextAvailable != nextAvailable) {
			final boolean old = this.nextAvailable;
			this.nextAvailable = nextAvailable;
			pcs.firePropertyChange("nextAvailable", old, nextAvailable);
		}
	}

	protected void setLastAvailable(final boolean lastAvailable) {
		if (this.lastAvailable != lastAvailable) {
			final boolean old = this.lastAvailable;
			this.lastAvailable = lastAvailable;
			pcs.firePropertyChange("lastAvailable", old, lastAvailable);
		}
	}

	protected void setCancelAvailable(final boolean cancelAvailable) {
		if (this.cancelAvailable != cancelAvailable) {
			final boolean old = this.cancelAvailable;
			this.cancelAvailable = cancelAvailable;
			pcs.firePropertyChange("cancelAvailable", old, cancelAvailable);
		}
	}

	@Override
	public boolean isLastVisible() {
		return lastVisible;
	}

	public void setLastVisible(final boolean lastVisible) {
		if (this.lastVisible != lastVisible) {
			final boolean old = this.lastVisible;
			this.lastVisible = lastVisible;
			pcs.firePropertyChange("lastVisible", old, lastVisible);
		}
	}

	@Override
	public void refreshModelState() {
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(final PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	@Override
	public void addPropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}

	@Override
	public void removePropertyChangeListener(final String propertyName, final PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(propertyName, listener);
	}

	protected void addCompleteListener(final WizardStep<?> step) {
		step.addPropertyChangeListener(completeListener);
	}
}
