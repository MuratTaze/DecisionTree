import java.io.IOException;
import java.util.ArrayList;

public class InputReader {
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		DataLoader dt=new DataLoader("ann-train.data");
		ArrayList<Instance> data=dt.getData();
	
	DecisionTreeLearner learner=new DecisionTreeLearner(data);
		
	}


}
