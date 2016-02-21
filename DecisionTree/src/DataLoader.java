import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DataLoader {
	private ArrayList<Record> data;

	public DataLoader(String filename) throws NumberFormatException, IOException {
		super();
		data = new ArrayList<Record>();

		FileReader inputFile = new FileReader(filename);
		BufferedReader bufferReader = new BufferedReader(inputFile);
		String line;
		int id = 1;
		while ((line = bufferReader.readLine()) != null) {
			String[] values = line.split(" ");
			Record record = new Record();
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

	public ArrayList<Record> getData() {
		return data;
	}

	public void setData(ArrayList<Record> data) {
		this.data = data;
	}

}
