package tw.yoyo.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;

public class ElectricityCal {

	float energySum = 0; // in wattHour
	String url = "jdbc:mysql://localhost:3306/factory?useSSL=false";
	String user = "root";
	String password = "root";
	String sql = "SELECT ts, v1, i1, v2, i2, v3, i3 FROM line1 WHERE ts BETWEEN ? and ?";
	ResultSetMetaData metaDate;
	int columnCount;
	int rowCount;
	List<String> columnName = new ArrayList<String>();
	DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private LocalDateTime startDT = null;
	private LocalDateTime endDT = null;
	int durationMins;
	int timeSlice = 5; //default accuracy of time slice
	private TimeSeries timeSeries;

	public ElectricityCal(LocalDateTime startDT, int durationMins
			, int timeSlice, TimeSeries timeSeries, int mode){
		this.startDT = startDT;
		this.durationMins = durationMins;
		this.timeSlice = timeSlice;
		this.timeSeries = timeSeries;
		
		switch (mode) {
		case 1:
			sql = "SELECT ts, v1, i1, v2, i2, v3, i3 FROM line1 WHERE ts BETWEEN ? and ?";
			break;
		case 2:
			sql = "SELECT ts, v1, i1, v2, i2, v3, i3 FROM line2 WHERE ts BETWEEN ? and ?";
			break;
		case 3:
			sql = "SELECT ts, v1, i1, v2, i2, v3, i3 FROM line3 WHERE ts BETWEEN ? and ?";
			break;
		default:
			sql = "SELECT ts, v1, i1, v2, i2, v3, i3 FROM line1 WHERE ts BETWEEN ? and ?";
			break;
		}
		
		endDT = startDT.plusMinutes(durationMins);
		calEnergySum();
	}
	
	public void calEnergySum(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		int period = durationMins / timeSlice - 1; //split by 10 minutes, -1 time
		
		try(Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement ps = conn.prepareStatement(sql);){
			
			LocalDateTime startSlice = startDT;
			LocalDateTime endSlice = startSlice.plusMinutes(timeSlice);
			
			for(int i = 0; i < period; i++){	// in the i number of period
				
				ps.setString(1, df.format(startSlice));
				ps.setString(2, df.format(endSlice));
				System.out.println(df.format(startSlice));
				
				try(ResultSet rs = ps.executeQuery();){
					
					metaDate = ps.getMetaData();
					columnCount = metaDate.getColumnCount();
						
					rs.next();
					for(int j = 2; j <= columnCount; j += 2){
						float tmp = energySlice((float)rs.getObject(j)
								, (float)rs.getObject(j + 1), timeSlice);
						energySum += tmp;
						timeSeries.addOrUpdate(new Minute(startSlice.getMinute()
								, startSlice.getHour()
								, startSlice.getDayOfMonth()
								, startSlice.getMonthValue()
								, startSlice.getYear())
								, tmp);
					}
					
					System.out.println("sum of energy:" + energySum);
					
					startSlice = startSlice.plusMinutes(timeSlice);
					endSlice = startSlice.plusMinutes(timeSlice);
					
				}

			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public float energySlice(float voltage, float current, int timeSlice){
		return voltage * current * timeSlice;
	}
	
	public float getEnergySum(){
		return energySum;
	}
	
	public LocalDateTime getStartDT(){
		return startDT;
	}

	public void printResult(){
		System.out.println("--------------The order information--------------");
		System.out.println("The order start from " + df.format(startDT));
		System.out.println("The order end   at   " + df.format(endDT));
		System.out.printf("%s%.2f%s\n","The total electricity cost of this order = ",
				energySum / 1000 , " kwh");
	}
}
