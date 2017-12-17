package nz.co.fitnet.wizard.models;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

import nz.co.fitnet.wizard.AbstractWizardModel;
import nz.co.fitnet.wizard.WizardStep;

public class MultiPathModel<M extends MultiPathModel<?, S>, S extends WizardStep<M>> extends AbstractWizardModel<S> {
	private final Path<M, S> firstPath;
	private final Path<M, S> lastPath;
	private final Map<S, Path<M, S>> pathMapping;

	private final Stack<S> history = new Stack<>();

	public MultiPathModel(final Path<M, S> firstPath) {
		this.firstPath = firstPath;

		final PathMapVisitor<M, S> visitor = new PathMapVisitor<>();
		firstPath.acceptVisitor(visitor);
		pathMapping = visitor.getMap();

		final LastPathVisitor<M, S> v = new LastPathVisitor<>();
		firstPath.acceptVisitor(v);
		lastPath = v.getPath();

		if (lastPath == null) {
			throw new IllegalStateException("Unable to locate last path");
		}
	}

	public Path<M, S> getFirstPath() {
		return firstPath;
	}

	public Path<M, S> getLastPath() {
		return lastPath;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void nextStep() {
		if (!isNextAvailable()) {
			throw new IllegalStateException("Next is not available");
		}
		final S currentStep = getActiveStep();
		final Path<M, S> currentPath = getPathForStep(currentStep);

		if (currentPath.isLastStep(currentStep)) {
			final Path<M, S> nextPath = currentPath.getNextPath((M) this);
			setActiveStep(nextPath.firstStep());
		} else {
			setActiveStep(currentPath.nextStep(currentStep));
		}

		history.push(currentStep);
	}

	@Override
	public void previousStep() {
		if (!isPreviousAvailable()) {
			throw new IllegalStateException("Previous is not available");
		}
		final S step = history.pop();
		setActiveStep(step);
	}

	@Override
	public void lastStep() {
		if (!isLastAvailable()) {
			throw new IllegalStateException("Last is not available");
		}
		history.push(getActiveStep());
		final S lastStep = getLastPath().lastStep();
		setActiveStep(lastStep);
	}

	@Override
	public void reset() {
		history.clear();
		final S firstStep = firstPath.firstStep();
		setActiveStep(firstStep);
		history.push(firstStep);
	}

	@Override
	public boolean isLastStep(final S step) {
		final Path<M, S> path = getPathForStep(step);
		return path.equals(getLastPath()) && path.isLastStep(step);
	}

	@Override
	public void refreshModelState() {
		final S activeStep = getActiveStep();
		final Path<M, S> activePath = getPathForStep(activeStep);

		setNextAvailable(activeStep.isComplete() && !isLastStep(activeStep));
		setPreviousAvailable(!(activePath.equals(firstPath) && activePath.isFirstStep(activeStep)));
		setLastAvailable(allStepsComplete() && !isLastStep(activeStep));
		setCancelAvailable(true);
	}

	public boolean allStepsComplete() {
		for (final Iterator<S> iterator = stepIterator(); iterator.hasNext();) {
			if (!iterator.next().isComplete()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Iterator<S> stepIterator() {
		return pathMapping.keySet().iterator();
	}

	protected Path<M, S> getPathForStep(final S step) {
		return pathMapping.get(step);
	}

	private class LastPathVisitor<W extends MultiPathModel<?, T>, T extends WizardStep<W>>
			extends AbstractPathVisitor<W, T> {
		private Path<W, T> last;

		@Override
		public void visitPath(final SimplePath<W, T> p) {
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
		public void visitPath(final BranchingPath<W, T> path) {
			if (enter(path)) {
				path.visitBranches(this);
			}
		}

		public Path<W, T> getPath() {
			return last;
		}
	}

	private class PathMapVisitor<W extends MultiPathModel<?, T>, T extends WizardStep<W>>
			extends AbstractPathVisitor<W, T> {
		private final Map<T, Path<W, T>> map = new LinkedHashMap<>();

		public PathMapVisitor() {
		}

		@Override
		public void visitPath(final SimplePath<W, T> path) {
			if (enter(path)) {
				populateMap(path);
				path.visitNextPath(this);
			}
		}

		@Override
		public void visitPath(final BranchingPath<W, T> path) {
			if (enter(path)) {
				populateMap(path);
				path.visitBranches(this);
			}
		}

		private void populateMap(final Path<W, T> path) {
			for (final T step : path.getSteps()) {
				map.put(step, path);
			}
		}

		public Map<T, Path<W, T>> getMap() {
			return map;
		}
	}
}
