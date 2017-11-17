package nz.co.fitnet.wizard;

public interface WizardStep<M extends WizardModel<?>> {
	String getName();

	String getSummary();

	String getIcon();

	String getView();

	boolean isComplete();

	boolean isBusy();

	void init(M model);

	void prepare();

	void applyState() throws InvalidStateException;

	void abortBusy();
}
