package nz.co.fitnet.wizard.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import nz.co.fitnet.wizard.AbstractWizardModel;
import nz.co.fitnet.wizard.WizardStep;

public class MultiPathModel extends AbstractWizardModel {
	private final Path firstPath;
	private final Path lastPath;
	private final Map<WizardStep, Path> pathMapping;

	private final Stack<WizardStep> history = new Stack<>();

	public MultiPathModel(final Path firstPath) {
		this.firstPath = firstPath;

		final PathMapVisitor visitor = new PathMapVisitor();
		firstPath.acceptVisitor(visitor);
		pathMapping = visitor.getMap();

		final LastPathVisitor v = new LastPathVisitor();
		firstPath.acceptVisitor(v);
		lastPath = v.getPath();

		if (lastPath == null) {
			throw new IllegalStateException("Unable to locate last path");
		}

		for (final WizardStep element : pathMapping.keySet()) {
			addCompleteListener(element);
		}
	}

	public Path getFirstPath() {
		return firstPath;
	}

	public Path getLastPath() {
		return lastPath;
	}

	@Override
	public void nextStep() {
		final WizardStep currentStep = getActiveStep();
		final Path currentPath = getPathForStep(currentStep);

		if (currentPath.isLastStep(currentStep)) {
			final Path nextPath = currentPath.getNextPath(this);
			setActiveStep(nextPath.firstStep());
		} else {
			setActiveStep(currentPath.nextStep(currentStep));
		}

		history.push(currentStep);
	}

	@Override
	public void previousStep() {
		final WizardStep step = history.pop();
		setActiveStep(step);
	}

	@Override
	public void lastStep() {
		history.push(getActiveStep());
		final WizardStep lastStep = getLastPath().lastStep();
		setActiveStep(lastStep);
	}

	@Override
	public void reset() {
		history.clear();
		final WizardStep firstStep = firstPath.firstStep();
		setActiveStep(firstStep);
		history.push(firstStep);
	}

	@Override
	public boolean isLastStep(final WizardStep step) {
		final Path path = getPathForStep(step);
		return path.equals(getLastPath()) && path.isLastStep(step);
	}

	@Override
	public void refreshModelState() {
		final WizardStep activeStep = getActiveStep();
		final Path activePath = getPathForStep(activeStep);

		setNextAvailable(activeStep.isComplete() && !isLastStep(activeStep));
		setPreviousAvailable(!(activePath.equals(firstPath) && activePath.isFirstStep(activeStep)));
		setLastAvailable(allStepsComplete() && !isLastStep(activeStep));
		setCancelAvailable(true);
	}

	public boolean allStepsComplete() {
		for (final Iterator<WizardStep> iterator = stepIterator(); iterator.hasNext();) {
			if (!iterator.next().isComplete()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Iterator<WizardStep> stepIterator() {
		return pathMapping.keySet().iterator();
	}

	protected Path getPathForStep(final WizardStep step) {
		return pathMapping.get(step);
	}

	private class LastPathVisitor extends AbstractPathVisitor {
		private Path last;

		@Override
		public void visitPath(final SimplePath p) {
			if (enter(p)) {
				if (p.getNextPath() == null) {
					if (last != null) {
						throw new IllegalStateException("Two paths have empty values for nextPath");
					}
					last = p;
				} else {
					p.visitNextPath(this);
				}
			}
		}

		@Override
		public void visitPath(final BranchingPath path) {
			if (enter(path)) {
				path.visitBranches(this);
			}
		}

		public Path getPath() {
			return last;
		}
	}

	private class PathMapVisitor extends AbstractPathVisitor {
		private final Map<WizardStep, Path> map = new HashMap<>();

		public PathMapVisitor() {
		}

		@Override
		public void visitPath(final SimplePath path) {
			if (enter(path)) {
				populateMap(path);
				path.visitNextPath(this);
			}
		}

		@Override
		public void visitPath(final BranchingPath path) {
			if (enter(path)) {
				populateMap(path);
				path.visitBranches(this);
			}
		}

		private void populateMap(final Path path) {
			for (final WizardStep step : path.getSteps()) {
				map.put(step, path);
			}
		}

		public Map<WizardStep, Path> getMap() {
			return map;
		}
	}
}
