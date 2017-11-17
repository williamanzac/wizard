package nz.co.fitnet.wizard.models;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nz.co.fitnet.wizard.AbstractWizardModel;
import nz.co.fitnet.wizard.WizardStep;

public class StaticModel extends AbstractWizardModel<WizardStep<StaticModel>> {

	private final List<WizardStep<StaticModel>> steps = new ArrayList<>();

	private int currentStep = 0;

	public StaticModel() {
	}

	@Override
	public void reset() {
		currentStep = 0;
		setActiveStep(steps.get(currentStep));
	}

	@Override
	public void nextStep() {
		if (!isNextAvailable()) {
			throw new IllegalStateException("Next is not available");
		}
		if (currentStep >= steps.size() - 1) {
			throw new IllegalStateException("Already on last step");
		}

		currentStep++;
		setActiveStep(steps.get(currentStep));
	}

	@Override
	public void previousStep() {
		if (!isPreviousAvailable()) {
			throw new IllegalStateException("Previous is not available");
		}
		if (currentStep == 0) {
			throw new IllegalStateException("Already at first step");
		}

		currentStep--;
		setActiveStep(steps.get(currentStep));
	}

	@Override
	public void lastStep() {
		if (!isLastAvailable()) {
			throw new IllegalStateException("Last is not available");
		}
		currentStep = steps.size() - 1;
		setActiveStep(steps.get(currentStep));
	}

	@Override
	public boolean isLastStep(final WizardStep<StaticModel> step) {
		return steps.indexOf(step) == steps.size() - 1;
	}

	@Override
	public Iterator<WizardStep<StaticModel>> stepIterator() {
		return unmodifiableList(steps).iterator();
	}

	public void add(final WizardStep<StaticModel> step) {
		steps.add(step);
	}

	@Override
	public void refreshModelState() {
		setNextAvailable(getActiveStep().isComplete() && !isLastStep(getActiveStep()));
		setPreviousAvailable(currentStep > 0);
		setLastAvailable(allStepsComplete() && !isLastStep(getActiveStep()));
		setCancelAvailable(true);
	}

	public boolean allStepsComplete() {
		for (final WizardStep<StaticModel> element : steps) {
			if (!element.isComplete()) {
				return false;
			}
		}

		return true;
	}
}
