package nz.co.fitnet.wizard.models;

import nz.co.fitnet.wizard.WizardModel;

public interface Condition<W extends WizardModel<?>> {
	public boolean evaluate(W model);
}
