package nz.co.fitnet.wizard.models;

import java.util.HashSet;
import java.util.Set;

import nz.co.fitnet.wizard.WizardModel;
import nz.co.fitnet.wizard.WizardStep;

public abstract class AbstractPathVisitor<W extends WizardModel<S>, S extends WizardStep<W>>
		implements PathVisitor<W, S> {
	private final Set<Path<W, S>> paths = new HashSet<>();

	protected boolean enter(final Path<W, S> path) {
		return paths.add(path);
	}
}
