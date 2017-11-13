package nz.co.fitnet.wizard.models;

import nz.co.fitnet.wizard.WizardModel;

public interface Condition {
	public boolean evaluate(WizardModel model);
}
