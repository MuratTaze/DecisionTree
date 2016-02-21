import java.util.Arrays;

public class Record {
	private int recordId;
	private int stateOfNature;
	private Double[] features;

	public Record() {
		super();
		features = new Double[21];
	}

	@Override
	public String toString() {
		return "Record [recordId=" + recordId + ", stateOfNature=" + stateOfNature + ", features="
				+ Arrays.toString(features) + "]\n";
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public int getStateOfNature() {
		return stateOfNature;
	}

	public void setStateOfNature(int stateOfNature) {
		this.stateOfNature = stateOfNature;
	}

	public Double[] getFeatures() {
		return features;
	}

	public void setFeatures(Double[] features) {
		this.features = features;
	}

}
