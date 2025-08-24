package de.home_skrobanek.blockchain.simulator;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultHighLowDataset;

public class ChartFrameBlockChain extends JFrame {

    public ChartFrameBlockChain() {
        setTitle("Candlestick Chart Beispiel");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Beispiel-Daten erzeugen
        DefaultHighLowDataset dataset = createDataset();

        // Chart erzeugen
        JFreeChart chart = ChartFactory.createCandlestickChart(
                "Beispiel Aktienchart",
                "Datum",
                "Preis",
                dataset,
                false
        );

        // X-Achse auf Datum formatieren
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd-MM-yyyy"));

        // Chart in Swing-Panel einbetten
        ChartPanel panel = new ChartPanel(chart);
        panel.setMouseWheelEnabled(true);

        add(panel, BorderLayout.CENTER);
    }

    private DefaultHighLowDataset createDataset() {
        int count = 10;
        Date[] date = new Date[count];
        double[] high = new double[count];
        double[] low = new double[count];
        double[] open = new double[count];
        double[] close = new double[count];
        double[] volume = new double[count];

        long millisPerDay = 24 * 60 * 60 * 1000L;
        for (int i = 0; i < count; i++) {
            date[i] = new Date(System.currentTimeMillis() - (count - i) * millisPerDay);
            open[i] = 100 + Math.random() * 10;
            close[i] = open[i] + (Math.random() - 0.5) * 5;
            high[i] = Math.max(open[i], close[i]) + Math.random() * 2;
            low[i] = Math.min(open[i], close[i]) - Math.random() * 2;
            volume[i] = Math.random() * 1000;
        }

        return new DefaultHighLowDataset("Demo", date, high, low, open, close, volume);
    }

    public static void StartFrame(){
        SwingUtilities.invokeLater(() -> new ChartFrameBlockChain().setVisible(true));
    }

}
