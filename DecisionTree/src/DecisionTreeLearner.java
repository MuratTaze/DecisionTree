import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class DecisionTreeLearner {
	public Tree actualTree;
	public ArrayList<Split> possibleSplits;

	public DecisionTreeLearner(ArrayList<Instance> data) {
		super();
		actualTree = new Tree();
		actualTree.data = data;
		possibleSplits = new ArrayList<Split>();
		findPossibleSplits(data, 10);
		buildTree(actualTree);
	}

	private void findPossibleSplits(ArrayList<Instance> data, int numberOfSplits) {

		for (int k = 0; k < 21; k++) {
			final int j = k;
			/* sort features */
			Collections.sort(data, new Comparator<Instance>() {

				@Override
				public int compare(Instance arg0, Instance arg1) {

					return arg0.getFeatures()[j].compareTo(arg1.getFeatures()[j]);
				}

			});
			/* find splits with pre-pruning */
			double min = data.get(0).getFeatures()[j];
			double max = data.get(data.size() - 1).getFeatures()[j];
			double difference = max - min;
			double delta = difference / numberOfSplits;

			for (int z = 0; z < numberOfSplits; z++) {
				min += delta;
				Split s = new Split(j, min);
				possibleSplits.add(s);
			}
		}
	}

	private void buildTree(Tree tree) {
		Split bestSplit = findBestSplit(tree);
		decomposeTree(bestSplit, tree);
		if (!tree.leftComplete)
			buildTree(tree.leftChild);
		if (!tree.rightComplete)
			buildTree(tree.rightChild);/**/
	}

	private void decomposeTree(Split bestSplit, Tree tree) {
		for (Instance ins : tree.data) {
			if (ins.getFeatures()[bestSplit.feature] < bestSplit.value) {
				if (tree.leftChild == null) {
					tree.leftChild = new Tree();
					tree.leftChild.data.add(ins);
				} else {
					tree.leftChild.data.add(ins);
				}
			} else {
				if (tree.rightChild == null) {
					tree.rightChild = new Tree();
					tree.rightChild.data.add(ins);
				} else {
					tree.rightChild.data.add(ins);
				}
			}
		}
		// burada kontrolü yap eðer left right istenen seviyedeyse left complete
		// olduysa mesela class labelý belirle complete de cýk
	}

	private Split findBestSplit(Tree tree) {
		/* find information gain */
		ArrayList<Integer> allLabels = new ArrayList<Integer>();
		for (Instance instance : tree.data) {
			allLabels.add(instance.getStateOfNature());
		}
		double actualInfoGain = entrophy(allLabels);

		/* find information gain of current split */
		double informationGain = 0;
		double max = 0;
		Split best = new Split(0, 0);
		ArrayList<Integer> labels = new ArrayList<Integer>();
		int countInstances = 0;
		for (Split split : possibleSplits) {
			for (Instance instance : tree.data) {
				if (instance.getFeatures()[split.feature] < split.value) {
					countInstances++;
					labels.add(instance.getStateOfNature());
				}

			}
			informationGain = actualInfoGain - (countInstances / tree.data.size() * entrophy(labels));
			if (informationGain > max) {
				max = informationGain;
				best = split;
			}
			countInstances = 0;
			labels.clear();
		}
		for (Iterator<Split> s = possibleSplits.iterator(); s.hasNext();) {
			if (s.next() == best)
				s.remove();
		}

		return best;
	}

	private double log2(double d) {
		return (Math.log(d) / Math.log(2));
	}

	private double entrophy(ArrayList<Integer> labels) {
		double class1 = 0, class2 = 0, class3 = 0;
		for (int i = 0; i < labels.size(); i++) {
			if (labels.get(i) == 1)
				class1++;
			if (labels.get(i) == 2)
				class2++;
			if (labels.get(i) == 3)
				class3++;
		}

		double entropyValue = -(class1 / labels.size() * log2(class1 / labels.size())
				+ class2 / labels.size() * log2(class2 / labels.size())
				+ class3 / labels.size() * log2(class3 / labels.size()));
		return entropyValue;

	}
}