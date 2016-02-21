import java.io.IOException;
import java.util.ArrayList;

public class InputReader {

	public static void main(String[] args) throws NumberFormatException, IOException {
		DataLoader dt=new DataLoader("ann-train.data");
		ArrayList<Record> data=dt.getData();
		System.out.println(data);
	}

}
