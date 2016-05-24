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
		findPossibleSplits(data, 200);
		buildTree(actualTree);
	}

	public void test(ArrayList<Instance> data) {
		int label;

		double correctPredicts = 0;
		double correctPredictClass1 = 0;
		double correctPredictClass2 = 0;
		double correctPredictClass3 = 0;
		double totalClass1 = 0;
		double totalClass2 = 0;
		double totalClass3 = 0;
		double class1_2 = 0;
		double class1_3 = 0;
		double class2_1 = 0;
		double class2_3 = 0;
		double class3_1 = 0;
		double class3_2 = 0;
		for (Instance instance : data) {
			if (instance.getStateOfNature() == 1)
				totalClass1++;
			if (instance.getStateOfNature() == 2)
				totalClass2++;
			if (instance.getStateOfNature() == 3)
				totalClass3++;
			label = classifyInstance(instance, actualTree);
			if (label == instance.getStateOfNature()) {
				correctPredicts++;
				if (label == 1)
					correctPredictClass1++;
				if (label == 2)
					correctPredictClass2++;
				if (label == 3)
					correctPredictClass3++;
			} else {

				if (instance.getStateOfNature() == 1) {
					if (label == 2)
						class1_2++;
					if (label == 3)
						class1_3++;
				}
				if (instance.getStateOfNature() == 2) {
					if (label == 1)
						class2_1++;
					if (label == 3)
						class2_3++;
				}
				if (instance.getStateOfNature() == 3) {
					if (label == 2)
						class3_2++;
					if (label == 1)
						class3_1++;
				}
			}

		}
		System.out.println("Accuracy:" + correctPredicts / data.size());
		System.out.println("Class 1 accuracy:" + correctPredictClass1 / totalClass1);
		System.out.println("Class 2 accuracy:" + correctPredictClass2 / totalClass2);
		System.out.println("Class 3 accuracy:" + correctPredictClass3 / totalClass3);
		System.out.println("Confusion matrix:");
		System.out.println(correctPredictClass1 + "   " + class1_2 + "   " + class1_3);
		System.out.println(class2_1 + "   " + correctPredictClass2 + "   " + class2_3);
		System.out.println(class3_1 + "   " + class3_2 + "   " + correctPredictClass3);
	}

	private int classifyInstance(Instance instance, Tree tree) {
		// go left
		Tree current = tree;
		int label = -1;
		while (current != null) {

			if (current.label != -1) {
				label = current.label;
				break;
			} else {
				if (instance.getFeatures()[current.split.feature] < current.split.value) {
					current = current.leftChild;

				} else {
					// go right
					current = current.rightChild;
				}

			}

		}
		return label;
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

	public void printTree(Tree tree) {
		// kodlanacak
		 if(tree!=null&&tree.split!=null)
			System.out.printf(" Feature: " + tree.split.feature + "  value: " + tree.split.value + " -----");
			if (tree.isLeaf)
				System.out.println("Label: " + tree.label);
		
		if (tree.leftChild != null) {
			printTree(tree.leftChild);
		}
		if (tree.rightChild != null) {
			printTree(tree.rightChild);
		}
		System.out.println();
	}

	private void buildTree(Tree tree) {
		Split bestSplit = findBestSplit(tree);

		// there is no meaningful split
		if (bestSplit.feature == 0 && bestSplit.value == 0) {
			int label = checkConfidence(tree, 0.98);
			if (label != -1) {
				tree.label = label;
				tree.isLeaf = true;
				return;
			} else {
				label = checkConfidence(tree, 0.3);
				if (label != -1) {
					tree.label = label;
					tree.isLeaf = true;
					return;
				}
			}

			// label ý set et.
			// isleaf true yap gel.
		} else {
			tree.split = bestSplit;
			decomposeTree(bestSplit, tree);
			buildTree(tree.leftChild);

			buildTree(tree.rightChild);/**/
		}
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

	}

	private int checkConfidence(Tree tree, double confidence) {
		double class1 = 0;
		double class2 = 0;
		double class3 = 0;
		int label = -1;
		for (Instance ins : tree.data) {
			if (ins.getStateOfNature() == 1)
				class1++;
			if (ins.getStateOfNature() == 2)
				class2++;
			if (ins.getStateOfNature() == 3)
				class3++;
		}
		double mostFrequent = findMax(class1, class2, class3);
		double ratio = mostFrequent / tree.data.size();
		if (mostFrequent == class1)
			label = 1;
		if (mostFrequent == class2)
			label = 2;
		if (mostFrequent == class3)
			label = 3;
		if (ratio >= confidence)
			return label;
		else
			return -1;
	}

	private double findMax(double... vals) {
		double max = Double.NEGATIVE_INFINITY;

		for (double d : vals) {
			if (d > max)
				max = d;
		}

		return max;
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
		double countInstances = 0.0;
		for (Split split : possibleSplits) {
			for (Instance instance : tree.data) {
				if (instance.getFeatures()[split.feature] < split.value) {
					countInstances++;
					labels.add(instance.getStateOfNature());
				}

			}
			if (countInstances == 0)
				continue;
			informationGain = actualInfoGain - ((countInstances / tree.data.size()) * entrophy(labels));
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