package nz.co.fitnet.wizard;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractWizardStep<M extends WizardModel<?>> implements WizardStep<M> {
	private final PropertyChangeSupport pcs;

	private String name;

	private String summary;

	private String icon;

	private boolean complete;

	private boolean busy = false;

	public AbstractWizardStep(final String name, final String summary) {
		this(name, summary, null);
	}

	public AbstractWizardStep(final String name, final String summary, final String icon) {
		pcs = new PropertyChangeSupport(this);
		this.name = name;
		this.summary = summary;
		this.icon = icon;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		if (this.name != null && !this.name.equals(name) || this.name == null && name != null) {
			final String old = this.name;
			this.name = name;
			pcs.firePropertyChange("name", old, name);
		}
	}

	@Override
	public String getSummary() {
		return summary;
	}

	public void setSummary(final String summary) {
		if (this.summary != null && !this.summary.equals(summary) || this.summary == null && summary != null) {
			final String old = this.summary;
			this.summary = summary;
			pcs.firePropertyChange("summary", old, summary);
		}
	}

	@Override
	public String getIcon() {
		return icon;
	}

	public void setIcon(final String icon) {
		if (this.icon != null && !this.icon.equals(icon) || this.icon == null && icon != null) {
			final String old = this.icon;
			this.icon = icon;
			pcs.firePropertyChange("icon", old, icon);
		}
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	public void setComplete(final boolean complete) {
		if (this.complete != complete) {
			this.complete = complete;
			pcs.firePropertyChange("complete", !complete, complete);
		}
	}

	@Override
	public boolean isBusy() {
		return busy;
	}

	public void setBusy(final boolean busy) {
		if (this.busy != busy) {
			final boolean old = this.busy;
			this.busy = busy;
			pcs.firePropertyChange("busy", old, busy);
		}
	}

	@Override
	public abstract void init(M model);

	@Override
	public void abortBusy() {
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
}
