package nz.co.fitnet.wizard.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nz.co.fitnet.wizard.WizardModel;
import nz.co.fitnet.wizard.WizardStep;

public class BranchingPath<W extends WizardModel<S>, S extends WizardStep<W>> extends Path<W, S> {
	private final Map<Condition<W>, Path<W, S>> paths = new HashMap<>();

	public BranchingPath() {
	}

	public BranchingPath(final S step) {
		addStep(step);
	}

	@Override
	protected Path<W, S> getNextPath(final W model) {
		final Path<W, S> nextPath = paths.entrySet().stream().filter(entry -> entry.getKey().evaluate(model))
				.map(Entry::getValue).findFirst().orElseThrow(() -> new IllegalStateException("No next path selected"));
		return nextPath;
	}

	public void addBranch(final Path<W, S> path, final Condition<W> condition) {
		paths.put(condition, path);
	}

	@Override
	public void acceptVisitor(final PathVisitor<W, S> visitor) {
		visitor.visitPath(this);
	}

	public void visitBranches(final PathVisitor<W, S> visitor) {
		paths.values().stream().forEach(path -> path.acceptVisitor(visitor));
	}
}
