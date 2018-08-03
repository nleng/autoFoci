package autoFoci;

import autoFoci.AnalyzeDialog;
import autoFoci.HistAnalyzer;
import autoFoci.GreenGUI.*;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import java.text.NumberFormat;
import javax.swing.border.EtchedBorder;

import java.util.Arrays;
import java.util.ArrayList;

import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.Range;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.Crosshair;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartColor;
import org.jfree.chart.panel.CrosshairOverlay;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.title.LegendTitle;




public class HistPanel { //  extends JPanel

    JFreeChart chart, chart_lin, chart_poisson;

    private ChartPanel chartPanel, chartPanel_lin, chartPanel_poisson;

    private Crosshair xCrosshair, xCrosshair_lin;

    private Crosshair yCrosshair, yCrosshair_lin;

    ValueMarker combined_marker, combined_marker_lin;

    NumberFormat int_format = NumberFormat.getIntegerInstance();
    NumberFormat format_4_digits = NumberFormat.getNumberInstance();

    GreenJFormattedTextField threshold_field = new GreenJFormattedTextField(format_4_digits);
    GreenJFormattedTextField threshold_field_lin = new GreenJFormattedTextField(format_4_digits);
    GreenJButton threshold_button = new GreenJButton("Set");
    GreenJFormattedTextField max_foci_field = new GreenJFormattedTextField(int_format);
    GreenJButton max_foci_button = new GreenJButton("Set");

    HistAnalyzer histAna;

    AnalyzeDialog ana;

    public HistPanel(HistAnalyzer histAna, AnalyzeDialog ana) {
        this.histAna = histAna;
        this.ana = ana;
    }

    public JPanel hist_panel(double[] hist_data, double[] hist_data_lin, String title, String xlabel, String xlabel_lin, String ylabel, double stDev_value, double range_value, ArrayList < Double > threshy_list, boolean log_scale) {

        //         Color dark_white = new Color(150,150,150);
        ArrayList < Double > hist_data_lin_cut_list = new ArrayList();
        for (int i = 0; i < hist_data_lin.length; i++)
            if (hist_data_lin[i] < 11000)
                hist_data_lin_cut_list.add(hist_data_lin[i]);
        double[] hist_data_lin_cut = new double[hist_data_lin_cut_list.size()];
        for (int i = 0; i < hist_data_lin_cut_list.size(); i++)
            hist_data_lin_cut[i] = hist_data_lin_cut_list.get(i);
        create_histogram(hist_data, hist_data_lin_cut, title, xlabel, xlabel_lin, ylabel);
        this.format_4_digits.setMaximumFractionDigits(4);
        this.chartPanel = new ChartPanel(this.chart);
        // 			this.chartPanel.addChartMouseListener(this);
        CrosshairOverlay crosshairOverlay = new CrosshairOverlay();
        this.xCrosshair = new Crosshair(Double.NaN, GreenGUI.fg.darker(), new BasicStroke(0f));
        this.xCrosshair.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        this.xCrosshair.setLabelVisible(true);
        this.xCrosshair.setLabelBackgroundPaint(GreenGUI.fg.darker());
        this.yCrosshair = new Crosshair(Double.NaN, GreenGUI.fg.darker(), new BasicStroke(0f));
        this.yCrosshair.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        this.yCrosshair.setLabelVisible(true);
        this.yCrosshair.setLabelBackgroundPaint(GreenGUI.fg.darker());
        crosshairOverlay.addDomainCrosshair(xCrosshair);
        crosshairOverlay.addRangeCrosshair(yCrosshair);
        chartPanel.addOverlay(crosshairOverlay);
        this.chart.setBackgroundPaint(GreenGUI.bg);

        XYPlot xyPlot = (XYPlot) this.chart.getPlot();
        xyPlot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));
        xyPlot.setBackgroundPaint(GreenGUI.bg);

        ValueAxis xAxis = xyPlot.getDomainAxis();
        xAxis.setAxisLinePaint(GreenGUI.fg);
        xAxis.setTickMarkPaint(GreenGUI.fg);
        xAxis.setTickLabelPaint(GreenGUI.fg);
        xAxis.setLabelPaint(GreenGUI.fg);

        ValueAxis yAxis = xyPlot.getRangeAxis();
        yAxis.setAxisLinePaint(GreenGUI.fg);
        yAxis.setTickMarkPaint(GreenGUI.fg);
        yAxis.setTickLabelPaint(GreenGUI.fg);
        yAxis.setLabelPaint(GreenGUI.fg);

        // everythin for chartPanel_lin
        this.chartPanel_lin = new ChartPanel(this.chart_lin);
        CrosshairOverlay crosshairOverlay_lin = new CrosshairOverlay();
        this.xCrosshair_lin = new Crosshair(Double.NaN, GreenGUI.fg.darker(), new BasicStroke(0f));
        this.xCrosshair_lin.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        this.xCrosshair_lin.setLabelVisible(true);
        this.xCrosshair_lin.setLabelBackgroundPaint(GreenGUI.fg.darker());
        this.yCrosshair_lin = new Crosshair(Double.NaN, GreenGUI.fg.darker(), new BasicStroke(0f));
        this.yCrosshair_lin.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        this.yCrosshair_lin.setLabelVisible(true);
        this.yCrosshair_lin.setLabelBackgroundPaint(GreenGUI.fg.darker());
        crosshairOverlay_lin.addDomainCrosshair(xCrosshair_lin);
        crosshairOverlay_lin.addRangeCrosshair(yCrosshair_lin);
        chartPanel_lin.addOverlay(crosshairOverlay_lin);
        this.chart_lin.setBackgroundPaint(GreenGUI.bg);

        XYPlot xyPlot_lin = (XYPlot) this.chart_lin.getPlot();
        xyPlot_lin.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));
        xyPlot_lin.setBackgroundPaint(GreenGUI.bg);

        ValueAxis xAxis_lin = xyPlot_lin.getDomainAxis();
        xAxis_lin.setAxisLinePaint(GreenGUI.fg);
        xAxis_lin.setTickMarkPaint(GreenGUI.fg);
        xAxis_lin.setTickLabelPaint(GreenGUI.fg);
        xAxis_lin.setLabelPaint(GreenGUI.fg);

        if (log_scale) {
            LogarithmicAxis yAxis_lin = new LogarithmicAxis("");
            // if we use LogarithmicAxis, this is needed:
            yAxis_lin.setAllowNegativesFlag(true);
            yAxis_lin.setAxisLinePaint(GreenGUI.fg);
            yAxis_lin.setTickMarkPaint(GreenGUI.fg);
            yAxis_lin.setTickLabelPaint(GreenGUI.fg);
            yAxis_lin.setLabelPaint(GreenGUI.fg);
            xyPlot_lin.setRangeAxis(yAxis_lin);
        } else {
            ValueAxis yAxis_lin = xyPlot_lin.getRangeAxis();
            yAxis_lin.setAxisLinePaint(GreenGUI.fg);
            yAxis_lin.setTickMarkPaint(GreenGUI.fg);
            yAxis_lin.setTickLabelPaint(GreenGUI.fg);
            yAxis_lin.setLabelPaint(GreenGUI.fg);
        }


        Color aqua = new Color(20, 150, 130);

        // 	Color yellow = new Color(150,150,20);
        // fffff
        Color yellow = new Color(100, 100, 20);

        vertical_marker(xyPlot, stDev_value, yellow, "stDev", 37, new float[] {2.0f, 3.0f});
        vertical_marker(xyPlot, range_value, yellow, "range", 37 + 16, new float[] {10.0f, 8.0f});

        String[] labels = {"Pearson", "Poisson", "MaxEnt", "RenyiEnt", "Yen", "Intermodes", "Triangle"};
        for (int i = 2; i < threshy_list.size(); i++)
            vertical_marker(xyPlot_lin, threshy_list.get(i), yellow, labels[i], 37 + i * 16, new float[] {2.0f, 3.0f});

        Color red = new Color(170, 50, 20);
        this.combined_marker = new ValueMarker(this.histAna.inverse(this.ana.oep_thresh), red, new BasicStroke(2.0f));
        this.combined_marker.setLabelFont(new Font("San Serif", Font.BOLD, 12));
        this.combined_marker.setLabelPaint(red);
        this.combined_marker.setLabel("selected");
        this.combined_marker.setLabelOffset(new RectangleInsets(21, 3, 3, 3));
        this.combined_marker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        this.combined_marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        xyPlot.addDomainMarker(this.combined_marker);

        this.combined_marker_lin = new ValueMarker(this.ana.oep_thresh, red, new BasicStroke(2.0f));
        this.combined_marker_lin.setLabelFont(new Font("San Serif", Font.BOLD, 12));
        this.combined_marker_lin.setLabelPaint(red);
        this.combined_marker_lin.setLabel("selected");
        this.combined_marker_lin.setLabelOffset(new RectangleInsets(21, 3, 3, 3));
        this.combined_marker_lin.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        this.combined_marker_lin.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        xyPlot_lin.addDomainMarker(this.combined_marker_lin);


        for (int i = 0; i < 2; i++) vertical_marker(xyPlot_lin, threshy_list.get(i), yellow, labels[i], 37 + i * 16, new float[] {2.0f, 3.0f});
        GreenJPanel both_charts = new GreenJPanel(new GridLayout(0, 2));
        both_charts.add(this.chartPanel);
        both_charts.add(this.chartPanel_lin);

        this.chartPanel.addChartMouseListener(new ChartMouseListener() {
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = HistPanel.this.chartPanel.getScreenDataArea();
                JFreeChart event_chart = event.getChart();
                XYPlot plot = (XYPlot) event_chart.getPlot();
                ValueAxis xAxis = plot.getDomainAxis();
                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
                set_crosshair(x);
            }


            public void chartMouseClicked(ChartMouseEvent event) {
                Rectangle2D dataArea = HistPanel.this.chartPanel.getScreenDataArea();
                JFreeChart event_chart = event.getChart();
                XYPlot plot = (XYPlot) event_chart.getPlot();
                ValueAxis xAxis = plot.getDomainAxis();
                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
                // if x<0 the panel resizes (?)
                if (x > 0.) {
                    HistPanel.this.threshold_field.setValue(x);
                    HistPanel.this.threshold_field_lin.setValue(HistPanel.this.histAna.inverse(x));
                    set_x();
                }
            }
        });

        this.chartPanel_lin.addChartMouseListener(new ChartMouseListener() {
            public void chartMouseMoved(ChartMouseEvent event) {
                Rectangle2D dataArea = HistPanel.this.chartPanel_lin.getScreenDataArea();
                JFreeChart event_chart = event.getChart();
                XYPlot plot = (XYPlot) event_chart.getPlot();
                ValueAxis xAxis = plot.getDomainAxis();
                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
                x = HistPanel.this.histAna.inverse(x);
                set_crosshair(x);
            }
            public void chartMouseClicked(ChartMouseEvent event) {
                Rectangle2D dataArea = HistPanel.this.chartPanel_lin.getScreenDataArea();
                JFreeChart event_chart = event.getChart();
                XYPlot plot = (XYPlot) event_chart.getPlot();
                ValueAxis xAxis = plot.getDomainAxis();
                double x = xAxis.java2DToValue(event.getTrigger().getX(), dataArea, RectangleEdge.BOTTOM);
                // if x<0 the panel resizes, problem solved another way, so this is not needed
                // 			if(x > 0.){
                HistPanel.this.threshold_field.setValue(HistPanel.this.histAna.inverse(x));
                HistPanel.this.threshold_field_lin.setValue(x);
                set_x();
                // 			}
            }
        });

        threshold_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                set_x();
            }
        });
        // so that you can also set the threshold by pressing enter
        this.threshold_field.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                set_x();
            }
        });

        this.threshold_field_lin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                set_x();
            }
        });

        this.max_foci_field.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                max_foci_changed();
            }
        });
        // so that you can also set the threshold by pressing enter
        this.max_foci_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                max_foci_changed();
            }
        });
        return both_charts;
    } // END hist_panel


    public void vertical_marker(XYPlot xyPlot, double value, Color color, String title, int vertical_offset, float[] stroke) {
        ValueMarker marker = new ValueMarker(value, color, new BasicStroke(1.f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, stroke, 0.0f));
        marker.setLabelFont(new Font("San Serif", Font.BOLD, 10));
        marker.setLabelPaint(color);
        marker.setLabel(title);
        marker.setLabelAnchor(RectangleAnchor.TOP_RIGHT);
        marker.setLabelTextAnchor(TextAnchor.TOP_LEFT);
        marker.setLabelOffset(new RectangleInsets(vertical_offset, 3, 3, 3));
        xyPlot.addDomainMarker(marker);

    } // END vertical_marker

    public void set_x() {
        double new_thresh = ((Number) this.threshold_field.getValue()).doubleValue();
        double new_thresh_lin = ((Number) this.threshold_field_lin.getValue()).doubleValue();
        if (new_thresh_lin != this.ana.oep_thresh) {
            this.ana.oep_thresh = new_thresh_lin;
        } else {
            this.ana.oep_thresh = this.histAna.inverse(new_thresh);
        }
        this.combined_marker.setValue(this.histAna.inverse(this.ana.oep_thresh));
        this.combined_marker_lin.setValue(this.ana.oep_thresh);

        if (!this.ana.use_overlay_images && this.ana.skip_cells_with_many_foci)
            this.ana.foci = this.histAna.count_foci_skip_cells(this.ana.oep_all, this.ana.oep_thresh, this.ana.max_foci);
        else
            this.ana.foci = this.histAna.count_foci(this.ana.oep_all, this.ana.oep_thresh);

        this.histAna.colocalization_pearson_difference(this.ana.oep_all, this.ana.oep_thresh);

        this.ana.combined_foci_title.setText("Foci/cell: " + String.valueOf(this.ana.foci[1]));

        refresh_poisson_chart();
    }

    public void max_foci_changed() {
        this.ana.max_foci = ((Number) this.max_foci_field.getValue()).intValue();
        this.ana.foci = this.histAna.count_foci_skip_cells(this.ana.oep_all, this.ana.oep_thresh, this.ana.max_foci);
        // 	this.ana.combined_foci_title.setText("<html><p style='width: 120px; font-family: san-serif;'>Foci/cell: "+String.valueOf(this.ana.foci[1])+"</p></html>");
        this.ana.combined_foci_title.setText("Foci/cell: " + String.valueOf(this.ana.foci[1]));
    }

    public void create_histogram(double[] hist_data, double[] hist_data_lin, String title, String xlabel, String xlabel_lin, String ylabel) {
        HistogramDataset dataset = new HistogramDataset();
        //       int bin = 100;
        int bin = 100;
        dataset.addSeries("Hula", hist_data, bin);

        this.chart = ChartFactory.createHistogram(title, xlabel, ylabel, dataset, PlotOrientation.VERTICAL, true, false, false);
        // don't need legend for histogram
        this.chart.removeLegend();

        XYPlot xyplot = (XYPlot) this.chart.getPlot();
        XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
        xybarrenderer.setShadowVisible(false);
        xybarrenderer.setBarPainter(new StandardXYBarPainter());

        Color orange = new Color(170, 100, 0);
        xybarrenderer.setSeriesPaint(0, orange);
        //       xybarrenderer.setSeriesPaint(0, new Color(75, 0, 75, 133));

        xyplot.setForegroundAlpha(0.7f);
        xyplot.setBackgroundPaint(Color.WHITE);
        xyplot.setDomainGridlinePaint(new Color(100, 100, 100));
        xyplot.setRangeGridlinePaint(new Color(100, 100, 100));
        // some padding bottom
        xyplot.setInsets(new RectangleInsets(3, 3, 25, 3));

        // everything for the chart_lin
        HistogramDataset dataset_lin = new HistogramDataset();
        //       int bin_lin = 100;
        int bin_lin = 85;
        dataset_lin.addSeries("Hula", hist_data_lin, bin_lin);

        this.chart_lin = ChartFactory.createHistogram(title, xlabel_lin, ylabel, dataset_lin, PlotOrientation.VERTICAL, true, false, false);
        // don't need legend for histogram
        this.chart_lin.removeLegend();

        XYPlot xyplot_lin = (XYPlot) this.chart_lin.getPlot();
        XYBarRenderer xybarrenderer_lin = (XYBarRenderer) xyplot_lin.getRenderer();
        xybarrenderer_lin.setShadowVisible(false);
        xybarrenderer_lin.setBarPainter(new StandardXYBarPainter());

        xybarrenderer_lin.setSeriesPaint(0, orange);

        xyplot_lin.setForegroundAlpha(0.7f);
        xyplot_lin.setBackgroundPaint(GreenGUI.bg);
        xyplot_lin.setDomainGridlinePaint(new Color(100, 100, 100));
        xyplot_lin.setRangeGridlinePaint(new Color(100, 100, 100));
        // some padding bottom
        xyplot_lin.setInsets(new RectangleInsets(3, 3, 25, 3));

    }

    public void set_crosshair(double x) {
        XYPlot plot = (XYPlot) this.chart.getPlot();
        double y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        this.xCrosshair.setValue(x);
        this.yCrosshair.setValue((int) y); // since we are in a histogram

        x = this.histAna.inverse(x);
        plot = (XYPlot) this.chart_lin.getPlot();
        y = DatasetUtilities.findYValue(plot.getDataset(), 0, x);
        this.xCrosshair_lin.setValue(x);
        this.yCrosshair_lin.setValue((int) y); // since we are in a histogram
    }
    private IntervalXYDataset createDataset(double[] poisson_exp, double[] poisson_theo) {
        final XYSeries series = new XYSeries("Experiment");
        for (int i = 0; i < poisson_exp.length; i++) {
            if (poisson_exp[i] != 0)
                series.add(i - 0.2, poisson_exp[i]);
        }
        final XYSeries series_theo = new XYSeries("Theo. Poisson");
        for (int i = 0; i < poisson_exp.length; i++) {
            if (poisson_exp[i] != 0) // here experiment not theo correct
                series_theo.add(0.2 + i, poisson_theo[i]);
        }

        final XYSeriesCollection dataset = new XYSeriesCollection(series);
        dataset.addSeries(series_theo);
        return dataset;
    }

    public void create_poisson_histogram(double[] poisson_exp, double[] poisson_theo, String title, String xlabel, String ylabel) {
        IntervalXYDataset dataset = createDataset(poisson_exp, poisson_theo);


        this.chart_poisson = ChartFactory.createXYBarChart(title, xlabel, false, ylabel, dataset, PlotOrientation.VERTICAL, true, true, false);

        XYPlot xyplot = (XYPlot) this.chart_poisson.getPlot();
        XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
        xybarrenderer.setShadowVisible(false);
        xybarrenderer.setBarPainter(new StandardXYBarPainter());
        xybarrenderer.setMargin(0.6);

        Color orange = new Color(170, 100, 0);
        Color violet = new Color(120, 30, 120);
        xybarrenderer.setSeriesPaint(0, orange);
        xybarrenderer.setSeriesPaint(1, violet);

        xyplot.setForegroundAlpha(0.7f);
        xyplot.setBackgroundPaint(GreenGUI.bg);
        xyplot.setDomainGridlinePaint(new Color(100, 100, 100));
        xyplot.setRangeGridlinePaint(new Color(100, 100, 100));
        xyplot.getRenderer().setSeriesStroke(0, new BasicStroke(3.0f));
        xyplot.setBackgroundPaint(GreenGUI.bg);
        // some padding bottom
        xyplot.setInsets(new RectangleInsets(3, 3, 25, 3));
    }

    public JPanel poisson_panel(double[] poisson_exp, double[] poisson_theo, String title, String xlabel, String ylabel) {
        create_poisson_histogram(poisson_exp, poisson_theo, title, xlabel, ylabel);

        this.chartPanel_poisson = new ChartPanel(this.chart_poisson);

        this.chart_poisson.setBackgroundPaint(GreenGUI.bg);

        XYPlot xyplot = (XYPlot) this.chart_poisson.getPlot();

        LegendTitle lt = new LegendTitle(xyplot);
        // 			lt.setItemFont(new Font("Dialog", Font.PLAIN, 9));
        lt.setBackgroundPaint(GreenGUI.bg);
        lt.setItemPaint(GreenGUI.fg);
        // 			lt.setFrame(GreenGUI.fg);
        lt.setPosition(RectangleEdge.BOTTOM);
        XYTitleAnnotation ta = new XYTitleAnnotation(0.98, 0.98, lt, RectangleAnchor.TOP_RIGHT); // BOTTOM_RIGHT, CENTER
        ta.setMaxWidth(0.48);
        xyplot.addAnnotation(ta);

        this.chart_poisson.removeLegend();

        ValueAxis xAxis = xyplot.getDomainAxis();
        xAxis.setAxisLinePaint(GreenGUI.fg);
        xAxis.setTickMarkPaint(GreenGUI.fg);
        xAxis.setTickLabelPaint(GreenGUI.fg);
        xAxis.setLabelPaint(GreenGUI.fg);

        ValueAxis yAxis = xyplot.getRangeAxis();
        yAxis.setAxisLinePaint(GreenGUI.fg);
        yAxis.setTickMarkPaint(GreenGUI.fg);
        yAxis.setTickLabelPaint(GreenGUI.fg);
        yAxis.setLabelPaint(GreenGUI.fg);

        return this.chartPanel_poisson;
    }

    private void refresh_poisson_chart() {
        this.ana.poisson_chart.removeAll();
        this.ana.poisson_chart.revalidate(); // This removes the old chart 
        poisson_panel(this.ana.poisson_exp, this.ana.poisson_theo, "", "Foci per cell (Poisson check)", "Relative frequency");
        this.ana.poisson_chart.setLayout(new BorderLayout());
        this.ana.poisson_chart.add(this.chartPanel_poisson);
        this.ana.poisson_chart.repaint();
        this.ana.poisson_label_setText();
    }

    public void toggle_log_scale(boolean log_scale, double stDev_value, double range_value, ArrayList < Double > threshy_list) {

        this.ana.oep_chart.removeAll();
        this.ana.oep_chart.revalidate(); // This removes the old chart
        this.ana.oep_chart.setLayout(new BorderLayout());
        if (log_scale)
            this.ana.oep_chart.add(hist_panel(this.ana.oep_all[1], this.ana.oep_all[3], "", "Parameter 1/OEP", "OEP, linear with foci quality", "Frequency", stDev_value, range_value, threshy_list, true));
        else
            this.ana.oep_chart.add(hist_panel(this.ana.oep_all[1], this.ana.oep_all[3], "", "Parameter 1/OEP", "OEP, linear with foci quality", "Frequency", stDev_value, range_value, threshy_list, false));
        this.ana.oep_chart.repaint();

    }


}