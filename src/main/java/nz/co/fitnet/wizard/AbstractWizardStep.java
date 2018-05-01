package nz.co.fitnet.wizard;

public abstract class AbstractWizardStep<M extends WizardModel<?>> implements WizardStep<M> {

	private String icon;

	private boolean busy = false;

	@Override
	public String getName() {
		return getClass().getSimpleName() + ".name";
	}

	@Override
	public String getSummary() {
		return getClass().getSimpleName() + ".summary";
	}

	@Override
	public String getIcon() {
		return icon;
	}

	public void setIcon(final String icon) {
		this.icon = icon;
	}

	@Override
	public boolean isBusy() {
		return busy;
	}

	public void setBusy(final boolean busy) {
		this.busy = busy;
	}

	@Override
	public void abortBusy() {
	}
}
