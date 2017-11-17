package nz.co.fitnet.wizard.models;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import nz.co.fitnet.wizard.AbstractWizardModel;
import nz.co.fitnet.wizard.WizardStep;

public class DynamicModel extends AbstractWizardModel<WizardStep<DynamicModel>> {

	public static final Condition<DynamicModel> TRUE_CONDITION = model -> true;

	private final List<WizardStep<DynamicModel>> steps = new ArrayList<>();
	private final List<Condition<DynamicModel>> conditions = new ArrayList<>();

	private final Stack<WizardStep<DynamicModel>> history = new Stack<>();

	public DynamicModel() {
	}

	@SuppressWarnings("unchecked")
	public void add(final WizardStep<DynamicModel> step) {
		if (step instanceof Condition) {
			add(step, (Condition<DynamicModel>) step);
		} else {
			add(step, TRUE_CONDITION);
		}
	}

	public void add(final WizardStep<DynamicModel> step, final Condition<DynamicModel> condition) {
		steps.add(step);
		conditions.add(condition);
	}

	@Override
	public void nextStep() {
		if (!isNextAvailable()) {
			throw new IllegalStateException("Next is not available");
		}
		final WizardStep<DynamicModel> currentStep = getActiveStep();
		history.push(currentStep);
		setActiveStep(findNextVisibleStep(currentStep));
	}

	@Override
	public void previousStep() {
		if (!isPreviousAvailable()) {
			throw new IllegalStateException("Previous is not available");
		}
		final WizardStep<DynamicModel> step = history.pop();
		setActiveStep(step);
	}

	@Override
	public void lastStep() {
		if (!isLastAvailable()) {
			throw new IllegalStateException("Last is not available");
		}
		final WizardStep<DynamicModel> activeStep = getActiveStep();
		history.push(activeStep);
		setActiveStep(findLastStep());
	}

	@Override
	public void reset() {
		history.clear();
		setActiveStep(findNextVisibleStep(null));
	}

	@Override
	public boolean isLastStep(final WizardStep<DynamicModel> step) {
		return findLastStep().equals(step);
	}

	@Override
	public void refreshModelState() {
		final WizardStep<DynamicModel> activeStep = getActiveStep();
		setNextAvailable(activeStep != null && activeStep.isComplete() && !isLastStep(activeStep));
		setPreviousAvailable(activeStep != null && !history.isEmpty());
		setLastAvailable(activeStep != null && allStepsComplete() && !isLastStep(activeStep));
		setCancelAvailable(true);
	}

	public boolean allStepsComplete() {
		for (int i = 0; i < steps.size(); i++) {
			final WizardStep<DynamicModel> step = steps.get(i);
			final Condition<DynamicModel> condition = conditions.get(i);
			if (condition.evaluate(this)) {
				if (!step.isComplete()) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public Iterator<WizardStep<DynamicModel>> stepIterator() {
		return unmodifiableList(steps).iterator();
	}

	private WizardStep<DynamicModel> findNextVisibleStep(final WizardStep<DynamicModel> currentStep) {
		final int startIndex = currentStep == null ? 0 : steps.indexOf(currentStep) + 1;

		for (int i = startIndex; i < conditions.size(); i++) {
			final Condition<DynamicModel> condition = conditions.get(i);
			if (condition.evaluate(this)) {
				return steps.get(i);
			}
		}

		throw new IllegalStateException("Wizard contains no more visible steps");
	}

	private WizardStep<DynamicModel> findLastStep() {
		for (int i = conditions.size() - 1; i >= 0; i--) {
			final Condition<DynamicModel> condition = conditions.get(i);
			if (condition.evaluate(this)) {
				return steps.get(i);
			}
		}

		throw new IllegalStateException("Wizard contains no visible steps");
	}
}
