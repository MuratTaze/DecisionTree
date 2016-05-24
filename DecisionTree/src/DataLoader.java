import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DataLoader {
	private ArrayList<Instance> data;

	public DataLoader(String filename) throws NumberFormatException, IOException {
		super();
		data = new ArrayList<Instance>();

		FileReader inputFile = new FileReader(filename);
		BufferedReader bufferReader = new BufferedReader(inputFile);
		String line;
		int id = 1;
		while ((line = bufferReader.readLine()) != null) {
			String[] values = line.split(" ");
			Instance record = new Instance();
			record.setRecordId(id);
			for (int i = 0; i < values.length - 1; i++) {
				record.getFeatures()[i] = new Double(values[i]);

			}
			record.setStateOfNature(new Integer(values[21]));
			id++;
			data.add(record);
		}

		bufferReader.close();
	}

	public ArrayList<Instance> underSampling() {
		int class1_count = 0;
		int class2_count = 0;
		int class3_count = 0;

		Collections.shuffle(data);
		ArrayList<Instance> balanced_data = new ArrayList<Instance>(93 * 3);
		for (int i = 0; i < 93 * 3; i++) {
			for (Instance instance : data) {
				if (instance.getStateOfNature() == 1 && class1_count != 93) {
					class1_count++;
					balanced_data.add(instance);

				}
				if (instance.getStateOfNature() == 2 && class2_count != 93) {
					class2_count++;
					balanced_data.add(instance);
				}
				if (instance.getStateOfNature() == 3 && class3_count != 93) {
					class3_count++;
					balanced_data.add(instance);
				}
			}
		}
		return balanced_data;
	}

	public ArrayList<Instance> getData() {
		return data;
	}

	public void setData(ArrayList<Instance> data) {
		this.data = data;
	}

}
