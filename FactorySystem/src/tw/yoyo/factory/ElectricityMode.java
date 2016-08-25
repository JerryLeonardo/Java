package tw.yoyo.factory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

public class ElectricityMode {

	//2016-08-08 00:03:00
	static DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	static DateTimeFormatter dayFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	static int timeSlice = 5;
	
	private TimeSeries timeSeries;  //private??
	private String seriesName;
	private int mode;
	private float modeConsumption = 0;		//store the total consumption of this Object(Mode)
	private LocalDateTime startDT;
	
	public ElectricityMode(String seriesName, int mode, int year, int month, int day) {
		
		this.seriesName = seriesName;
		this.mode = mode;
		
		timeSeries = new TimeSeries(seriesName);
		List<ElectricityCal> produceModeList = null;

		startDT = LocalDateTime.of(year, month, day, 0, 0, 0);
		produceModeList = getProduceMode(startDT, mode, timeSeries);
		
		modeConsumption = getModeConsumn(produceModeList);
		
	}
	
	public TimeSeries getTimeSeries(){
		return timeSeries;
	}
	
	public float getModeConsumption() {
		return modeConsumption;
	}
	
	public String toString(){
		return "%s%s%s%.2f%s\n" + 
				"The eletricity cosumption of " + seriesName + " on "
				+ dayFormat.format(startDT) + " with mode " + mode + " is "
				+ modeConsumption / 1000 + " kwh";
	}
	
	public static float getModeConsumn(List<ElectricityCal> produceMode){
		float modeSum = 0;
		for(int i = 0; i < produceMode.size(); i++){
			modeSum += produceMode.get(i).getEnergySum();
		}
		return modeSum;
	}
	
	public static void addIdleData(LocalDateTime startDT, int minutes, TimeSeries timeSeries){
		
		LocalDateTime tmpDT = startDT;
		int count = minutes / timeSlice -1;
		for(int i = 0 ; i < count ; i++){
			timeSeries.addOrUpdate(new Minute(tmpDT.getMinute()
					, tmpDT.getHour()
					, tmpDT.getDayOfMonth()
					, tmpDT.getMonthValue()
					, tmpDT.getYear())
					, 10);
			tmpDT = tmpDT.plusMinutes(timeSlice);
		}
		
	}
	
	public static List<ElectricityCal> getProduceMode(LocalDateTime day
			, int mode, TimeSeries timeSeries){
		
		LocalDateTime tmpDT = day;
		List<ElectricityCal> produceMode = new ArrayList<ElectricityCal>();
		
		switch (mode) {
		case 1:
			produceMode.add(new ElectricityCal(tmpDT, 300, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(330);
			produceMode.add(new ElectricityCal(tmpDT, 300, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(330);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 60, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(90);
			produceMode.add(new ElectricityCal(tmpDT, 60, timeSlice, timeSeries, mode));
			break;
		case 2:
			produceMode.add(new ElectricityCal(tmpDT, 300, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(330);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 60, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(90);
			produceMode.add(new ElectricityCal(tmpDT, 300, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(330);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 60, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(90);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			break;
		default:
			produceMode.add(new ElectricityCal(tmpDT, 60, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(90);
			produceMode.add(new ElectricityCal(tmpDT, 60, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(90);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 180, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(210);
			produceMode.add(new ElectricityCal(tmpDT, 300, timeSlice, timeSeries, mode));
			addIdleData(tmpDT, 30, timeSeries);
			tmpDT = tmpDT.plusMinutes(330);
			produceMode.add(new ElectricityCal(tmpDT, 300, timeSlice, timeSeries, mode));
			break;
		}
		return produceMode;
	}
}
