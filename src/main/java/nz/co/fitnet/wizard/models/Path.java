package nz.co.fitnet.wizard.models;

import java.util.ArrayList;
import java.util.List;

import nz.co.fitnet.wizard.WizardModel;
import nz.co.fitnet.wizard.WizardStep;

public abstract class Path<W extends WizardModel<S>, S extends WizardStep<W>> {
	private final List<S> steps = new ArrayList<>();

	protected Path() {
	}

	protected abstract Path<W, S> getNextPath(W model);

	public void addStep(final S step) {
		steps.add(step);
	}

	public S firstStep() {
		return steps.get(0);
	}

	public S nextStep(final S currentStep) {
		final int index = steps.indexOf(currentStep);
		return steps.get(index + 1);
	}

	public S previousStep(final S currentStep) {
		final int index = steps.indexOf(currentStep);
		return steps.get(index - 1);
	}

	public S lastStep() {
		return steps.get(steps.size() - 1);
	}

	public boolean isFirstStep(final S step) {
		return steps.indexOf(step) == 0;
	}

	public boolean isLastStep(final S step) {
		final boolean lastStep = steps.lastIndexOf(step) == steps.size() - 1;
		return lastStep;
	}

	public List<S> getSteps() {
		return steps;
	}

	public boolean contains(final S step) {
		return steps.contains(step);
	}

	public abstract void acceptVisitor(PathVisitor<W, S> visitor);
}
