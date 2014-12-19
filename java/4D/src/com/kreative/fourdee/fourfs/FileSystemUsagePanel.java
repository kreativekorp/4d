package com.kreative.fourdee.fourfs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import com.kreative.fourdee.file.FileSystem;

public class FileSystemUsagePanel extends JPanel implements TableModelListener {
	private static final long serialVersionUID = 1L;
	
	private FileSystem fs;
	private JProgressBar activeDirectoryUsage;
	private JProgressBar totalDirectoryUsage;
	private JProgressBar clusterUsage;
	
	public FileSystemUsagePanel(FileSystem fs) {
		this.fs = fs;
		this.activeDirectoryUsage = new JProgressBar();
		this.totalDirectoryUsage = new JProgressBar();
		this.clusterUsage = new JProgressBar();
		JPanel labelPanel = new JPanel(new GridLayout(3,1));
		labelPanel.add(new JLabel("Act:"));
		labelPanel.add(new JLabel("Dir:"));
		labelPanel.add(new JLabel("Spc:"));
		JPanel progressPanel = new JPanel(new GridLayout(3,1));
		progressPanel.add(activeDirectoryUsage);
		progressPanel.add(totalDirectoryUsage);
		progressPanel.add(clusterUsage);
		setLayout(new BorderLayout(4,4));
		add(labelPanel, BorderLayout.LINE_START);
		add(progressPanel, BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		update();
	}
	
	public FileSystem getFileSystem() {
		return this.fs;
	}
	
	public void setFileSystem(FileSystem fs) {
		this.fs = fs;
		update();
	}
	
	public void update() {
		int activeCount = fs.activeSize();
		activeDirectoryUsage.setMinimum(0);
		activeDirectoryUsage.setMaximum(2031);
		activeDirectoryUsage.setValue(activeCount);
		activeCount = activeCount * 100 / 2031;
		activeDirectoryUsage.setForeground(
			(activeCount >= 95) ? Color.red :
			(activeCount >= 85) ? Color.orange :
			(activeCount >= 75) ? Color.yellow :
			Color.green
		);
		int count = fs.size();
		totalDirectoryUsage.setMinimum(0);
		totalDirectoryUsage.setMaximum(2047);
		totalDirectoryUsage.setValue(count);
		count = count * 100 / 2047;
		totalDirectoryUsage.setForeground(
			(count >= 95) ? Color.red :
			(count >= 85) ? Color.orange :
			(count >= 75) ? Color.yellow :
			Color.green
		);
		int usedClusters = fs.getUsedClusterLength();
		int clusters = fs.getClusterLength();
		clusterUsage.setMinimum(0);
		clusterUsage.setMaximum(clusters);
		clusterUsage.setValue(usedClusters);
		usedClusters = usedClusters * 100 / clusters;
		clusterUsage.setForeground(
			(usedClusters >= 95) ? Color.red :
			(usedClusters >= 85) ? Color.orange :
			(usedClusters >= 75) ? Color.yellow :
			Color.green
		);
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		update();
	}
}