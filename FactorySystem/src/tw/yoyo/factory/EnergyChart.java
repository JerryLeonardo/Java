package tw.yoyo.factory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class EnergyChart extends JFrame {

	public enum ESeries{
		ModeA("LineA"), ModeB("LineB"), ModeC("LineC"), Today("Today");
		private String msg;
		private ESeries(String m){
			this.msg = m;
		}
		@Override
		public String toString(){
			return msg;
		}
	}
	
	private static final long serialVersionUID = 1L;
	private TimeSeriesCollection resultSeriesColl;
	private ElectricityThd electricityThd = null;
	class AppAdapter extends WindowAdapter{
		public void windowsClosing(WindowEvent event){
			if(electricityThd != null){
				electricityThd.runFlag = false;
			}
		}
	}
	
	public EnergyChart(String applicationTitle, String chartTitle){
		super(applicationTitle);
		
		TimeSeriesCollection seriesCollection = createSeriesColl();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle
				, "Time HH:mm", "Electricity (kwh)", seriesCollection 
				, true, true, false);
		XYPlot plot = chart.getXYPlot();
		DateAxis axis = (DateAxis)plot.getDomainAxis();
		axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
		ChartPanel panel = new ChartPanel(chart);
		panel.setPreferredSize(new java.awt.Dimension(1300, 550));
		setContentPane(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		electricityThd = new ElectricityThd(resultSeriesColl, chart);
		new Thread(electricityThd).start();
		
	}
	
	private TimeSeriesCollection createSeriesColl() {
		
		resultSeriesColl = new TimeSeriesCollection();
		TimeSeries series1 = new TimeSeries(ESeries.ModeA);
		series1.add(new Minute(3,0,8,8,2016), 100);
		series1.add(new Minute(30,3,8,8,2016), 300);
		series1.add(new Minute(0,5,8,8,2016), 500);
		series1.add(new Minute(0,7,8,8,2016), 400);
		series1.add(new Minute(0,9,8,8,2016), 700);
		TimeSeries series2 = new TimeSeries(ESeries.ModeB);
		series2.add(new Minute(3,0,8,8,2016), 500);
		series2.add(new Minute(0,2,8,8,2016), 600);
		series2.add(new Minute(30,5,8,8,2016), 400);
		series2.add(new Minute(0,9,8,8,2016), 400);
		series2.add(new Minute(0,11,8,8,2016), 700);
		TimeSeries series3 = new TimeSeries(ESeries.ModeC);
		series3.add(new Minute(3,0,8,8,2016), 700);
		series3.add(new Minute(0,4,8,8,2016), 200);
		series3.add(new Minute(30,6,8,8,2016), 800);
		series3.add(new Minute(0,7,8,8,2016), 400);
		series3.add(new Minute(0,10,8,8,2016), 400);
		TimeSeries series4 = new TimeSeries(ESeries.Today);
		series4.add(new Minute(3,0,8,8,2016), 700);
		series4.add(new Minute(0,4,8,8,2016), 800);
		series4.add(new Minute(30,6,8,8,2016), 900);
		series4.add(new Minute(0,7,8,8,2016), 1000);
		series4.add(new Minute(0,10,8,8,2016), 900);
		resultSeriesColl.addSeries(series1);
		resultSeriesColl.addSeries(series2);
		resultSeriesColl.addSeries(series3);
		resultSeriesColl.addSeries(series4);
		return resultSeriesColl;
	}
	
	public static void main(String[] args) {
		EnergyChart energyChart = new EnergyChart(
				"Electricity Cosumption of production line"
				, "Show the daily cosumption");
		energyChart.pack();
		energyChart.setVisible(true);
	}

}
