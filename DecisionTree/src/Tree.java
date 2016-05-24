import java.util.ArrayList;

public class Tree {

	public Tree leftChild;
	public Tree rightChild;
	public Split split;
	public ArrayList<Instance> data;
	public int label;
	public boolean isLeaf;

	public Tree() {
		super();
		data = new ArrayList<Instance>();
		leftChild = null;
		rightChild = null;
		label = -1;
		isLeaf = false;
	}

}
