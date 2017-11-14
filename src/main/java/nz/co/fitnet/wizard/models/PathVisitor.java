package nz.co.fitnet.wizard.models;

import nz.co.fitnet.wizard.WizardModel;
import nz.co.fitnet.wizard.WizardStep;

public interface PathVisitor<W extends WizardModel<S>, S extends WizardStep<W>> {
	public void visitPath(SimplePath<W, S> path);

	public void visitPath(BranchingPath<W, S> path);
}
