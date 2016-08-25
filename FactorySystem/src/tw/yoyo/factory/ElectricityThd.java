package tw.yoyo.factory;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import tw.yoyo.factory.EnergyChart.ESeries;

public class ElectricityThd extends AbsDataThd{
	
	JFreeChart chart;
	TimeSeriesCollection seriesColl = new TimeSeriesCollection();
	
	public ElectricityThd(TimeSeriesCollection seriesColl, JFreeChart chart){
		this.chart = chart;
		this.seriesColl = seriesColl;
	}
	
	@Override
	public void run(){
		try{
			int count = showTimes;
			while(runFlag && count > 0){
				Thread.sleep(sleepPeriod);
				seriesColl.removeAllSeries();
				int day = count % 5 + 8;
				chart.setTitle("Calculating the power consumption on 2016-8-" + day + " ...");
				
				LocalDateTime dateTime = LocalDateTime.of(2016, 8, day, 0, 0, 0);
				TimeSeries timeSeries = new TimeSeries("theDay");
				
				ElectricityMode electricityMode1 = new ElectricityMode( 
						ESeries.ModeA.toString(), 1, 2016, 8, day);
				TimeSeries series1 = electricityMode1.getTimeSeries();
				seriesColl.addSeries(series1);
				ElectricityMode electricityMode2 = new ElectricityMode( 
						ESeries.ModeB.toString(), 2, 2016, 8, day);
				TimeSeries series2 = electricityMode2.getTimeSeries();
				seriesColl.addSeries(series2);
				ElectricityMode electricityMode3 = new ElectricityMode( 
						ESeries.ModeC.toString(), 3, 2016, 8, day);
				TimeSeries series3 = electricityMode3.getTimeSeries();
				seriesColl.addSeries(series3);
				
				float dailyC = new ElectricityCal(dateTime, 1440, 10, timeSeries, 3).getEnergySum() / 1000;
				float line1C = electricityMode1.getModeConsumption() / 1000;
				float line2C = electricityMode2.getModeConsumption() / 1000;
				float line3C = electricityMode3.getModeConsumption() / 1000;
				
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				chart.setTitle("On 2016-8-" + day
						+ ", the electricity consumption of \nLineA = "  
						+ df.format(line1C) 
						+ " kwh, LineB = " 
						+ df.format(line2C) 
						+ " kwh, LineC = " 
						+ df.format(line3C)  + " kwh\n"
						+ "The 24hrs consumption = " 
						+ df.format(dailyC)
						+ " kwh\n"
						+ "LineA wasted " + df.format(dailyC - line1C) 
						+ " kwh, LineB wasted " + df.format(dailyC - line2C)
						+ " kwh, LineC wasted " + df.format(dailyC - line3C) + " kwh");
				
//				String.format("%s%d%s%.2f%s%.2f%s%.2f%s"
//						, "Today is 2016-8-" ,day
//						, ", the electricity consumption of ModeA = "  
//						, electricityMode1.getModeConsumption() / 1000 
//						, " kwh, ModeB = " 
//						, electricityMode2.getModeConsumption() / 1000 
//						," kwh, ModeC = " 
//						, electricityMode3.getModeConsumption() / 1000 + " kwh")
				
				count++;
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
