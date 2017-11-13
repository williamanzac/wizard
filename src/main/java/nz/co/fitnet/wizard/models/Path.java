package nz.co.fitnet.wizard.models;

import java.util.ArrayList;
import java.util.List;

import nz.co.fitnet.wizard.WizardStep;

public abstract class Path {
	private final List<WizardStep> steps = new ArrayList<>();

	protected Path() {
	}

	protected abstract Path getNextPath(MultiPathModel model);

	public void addStep(final WizardStep step) {
		steps.add(step);
	}

	public WizardStep firstStep() {
		return steps.get(0);
	}

	public WizardStep nextStep(final WizardStep currentStep) {
		final int index = steps.indexOf(currentStep);
		return steps.get(index + 1);
	}

	public WizardStep previousStep(final WizardStep currentStep) {
		final int index = steps.indexOf(currentStep);
		return steps.get(index - 1);
	}

	public WizardStep lastStep() {
		return steps.get(steps.size() - 1);
	}

	public boolean isFirstStep(final WizardStep step) {
		return steps.indexOf(step) == 0;
	}

	public boolean isLastStep(final WizardStep step) {
		final boolean lastStep = steps.lastIndexOf(step) == steps.size() - 1;
		return lastStep;
	}

	public List<WizardStep> getSteps() {
		return steps;
	}

	public boolean contains(final WizardStep step) {
		return steps.contains(step);
	}

	public abstract void acceptVisitor(PathVisitor visitor);
}
