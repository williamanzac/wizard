package nz.co.fitnet.wizard.models;

import nz.co.fitnet.wizard.WizardStep;

public class SimplePath extends Path {
	private Path nextPath;

	public SimplePath() {
	}

	public SimplePath(final WizardStep step) {
		addStep(step);
	}

	@Override
	protected Path getNextPath(final MultiPathModel model) {
		return nextPath;
	}

	public Path getNextPath() {
		return nextPath;
	}

	public void setNextPath(final Path nextPath) {
		this.nextPath = nextPath;
	}

	@Override
	public void acceptVisitor(final PathVisitor visitor) {
		visitor.visitPath(this);
	}

	public void visitNextPath(final PathVisitor visitor) {
		if (nextPath != null) {
			nextPath.acceptVisitor(visitor);
		}
	}
}
