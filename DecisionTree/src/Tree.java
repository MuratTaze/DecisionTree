import java.util.ArrayList;

public class Tree {

	public boolean leftComplete;
	public Tree leftChild;
	public boolean rightComplete;
	public Tree rightChild;
	public Split split;
	public ArrayList<Instance> data;
	public int leftLabel;
	public int rightLabel;

	public Tree() {
		super();
		data = new ArrayList<Instance>();
		leftChild = null;
		rightChild = null;
		leftLabel = -1;
		rightLabel = -1;
		leftComplete = false;
		rightComplete = false;
	}

}
