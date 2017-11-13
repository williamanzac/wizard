package nz.co.fitnet.wizard.models;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractPathVisitor implements PathVisitor {
	private final Set<Path> paths = new HashSet<>();

	protected boolean enter(final Path path) {
		return paths.add(path);
	}
}
