package nz.co.fitnet.wizard.models;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import nz.co.fitnet.wizard.AbstractWizardModel;
import nz.co.fitnet.wizard.WizardStep;

public class DynamicModel extends AbstractWizardModel {

	public static final Condition TRUE_CONDITION = model -> true;

	private final List<WizardStep> steps = new ArrayList<>();
	private final List<Condition> conditions = new ArrayList<>();

	private final Stack<WizardStep> history = new Stack<>();

	public DynamicModel() {
	}

	public void add(final WizardStep step) {
		if (step instanceof Condition) {
			add(step, (Condition) step);
		} else {
			add(step, TRUE_CONDITION);
		}
	}

	public void add(final WizardStep step, final Condition condition) {
		addCompleteListener(step);
		steps.add(step);
		conditions.add(condition);
	}

	@Override
	public void nextStep() {
		final WizardStep currentStep = getActiveStep();
		history.push(currentStep);
		setActiveStep(findNextVisibleStep(currentStep));
	}

	@Override
	public void previousStep() {
		final WizardStep step = history.pop();
		setActiveStep(step);
	}

	@Override
	public void lastStep() {
		final WizardStep activeStep = getActiveStep();
		history.push(activeStep);
		setActiveStep(findLastStep());
	}

	@Override
	public void reset() {
		history.clear();
		setActiveStep(findNextVisibleStep(null));
	}

	@Override
	public boolean isLastStep(final WizardStep step) {
		return findLastStep().equals(step);
	}

	@Override
	public void refreshModelState() {
		final WizardStep activeStep = getActiveStep();
		setNextAvailable(activeStep != null && activeStep.isComplete() && !isLastStep(activeStep));
		setPreviousAvailable(activeStep != null && !history.isEmpty());
		setLastAvailable(activeStep != null && allStepsComplete() && !isLastStep(activeStep));
		setCancelAvailable(true);
	}

	public boolean allStepsComplete() {
		for (int i = 0; i < steps.size(); i++) {
			final WizardStep step = steps.get(i);
			final Condition condition = conditions.get(i);
			if (condition.evaluate(this)) {
				if (!step.isComplete()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public Iterator<WizardStep> stepIterator() {
		return unmodifiableList(steps).iterator();
	}

	private WizardStep findNextVisibleStep(final WizardStep currentStep) {
		final int startIndex = currentStep == null ? 0 : steps.indexOf(currentStep) + 1;

		for (int i = startIndex; i < conditions.size(); i++) {
			final Condition condition = conditions.get(i);
			if (condition.evaluate(this)) {
				return steps.get(i);
			}
		}

		throw new IllegalStateException("Wizard contains no more visible steps");
	}

	private WizardStep findLastStep() {
		for (int i = conditions.size() - 1; i >= 0; i--) {
			final Condition condition = conditions.get(i);
			if (condition.evaluate(this)) {
				return steps.get(i);
			}
		}

		throw new IllegalStateException("Wizard contains no visible steps");
	}
}
