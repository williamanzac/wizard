package nz.co.fitnet.wizard.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nz.co.fitnet.wizard.WizardStep;

public class BranchingPath extends Path {
	private final Map<Condition, Path> paths = new HashMap<>();

	public BranchingPath() {
	}

	public BranchingPath(final WizardStep step) {
		addStep(step);
	}

	@Override
	protected Path getNextPath(final MultiPathModel model) {
		final Path nextPath = paths.entrySet().stream().filter(entry -> entry.getKey().evaluate(model))
				.map(Entry::getValue).findFirst().orElseThrow(() -> new IllegalStateException("No next path selected"));
		return nextPath;
	}

	public void addBranch(final Path path, final Condition condition) {
		paths.put(condition, path);
	}

	@Override
	public void acceptVisitor(final PathVisitor visitor) {
		visitor.visitPath(this);
	}

	public void visitBranches(final PathVisitor visitor) {
		paths.values().stream().forEach(path -> path.acceptVisitor(visitor));
	}
}
