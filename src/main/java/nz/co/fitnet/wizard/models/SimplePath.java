package nz.co.fitnet.wizard.models;

import nz.co.fitnet.wizard.WizardModel;
import nz.co.fitnet.wizard.WizardStep;

public class SimplePath<W extends WizardModel<S>, S extends WizardStep<W>> extends Path<W, S> {
	private Path<W, S> nextPath;

	public SimplePath() {
	}

	public SimplePath(final S step) {
		addStep(step);
	}

	@Override
	protected Path<W, S> getNextPath(final W model) {
		return nextPath;
	}

	public Path<W, S> getNextPath() {
		return nextPath;
	}

	public void setNextPath(final Path<W, S> nextPath) {
		this.nextPath = nextPath;
	}

	@Override
	public void acceptVisitor(final PathVisitor<W, S> visitor) {
		visitor.visitPath(this);
	}

	public void visitNextPath(final PathVisitor<W, S> visitor) {
		if (nextPath != null) {
			nextPath.acceptVisitor(visitor);
		}
	}
}
