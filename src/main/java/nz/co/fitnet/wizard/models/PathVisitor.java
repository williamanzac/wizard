package nz.co.fitnet.wizard.models;

public interface PathVisitor {
	public void visitPath(SimplePath path);

	public void visitPath(BranchingPath path);
}
