package database;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.text.DefaultCaret;

public class ProgressBar extends JPanel implements ActionListener,
		PropertyChangeListener {
	
	private static final long serialVersionUID = 1L;
	private JProgressBar progressBar;
	private JButton startButton;
	private JTextArea taskOutput;
	private JLabel timeLeft;
	private WeatherData data;
	private Task task;
	private JFrame parent;
	private boolean climate;
	private Converter callback;
	final Thread exe;

	public class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			// Initialize progress property.
			setProgress(0);
			if(climate)
				data.generateClimateTable(this, taskOutput, timeLeft);
			else
				data.createNewDatabase(this, taskOutput, timeLeft);
			return null;
		}

		public void updateProgress(int p) {
			int progress = Math.min(p, 100);
			this.setProgress(progress);
		}
		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			Toolkit.getDefaultToolkit().beep();
			startButton.setEnabled(true);
			setCursor(null); // turn off the wait cursor
			taskOutput.append("Done!\n");
			this.setProgress(100);
			//close
			
			if(exe == null)
				callback.conversionComplete(data.getDatabasePath());
		}
	}
	
	public void register(Converter callback) {
		this.callback = callback;
    }

	public ProgressBar(final Thread exe, WeatherData data, JFrame parent, boolean climate) {
		super(new BorderLayout());
		this.data = data;
		this.parent = parent;
		this.exe = exe;
		this.climate = climate;

		// Create the demo's UI.
		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.addActionListener(this);

		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);

		taskOutput = new JTextArea(5, 20);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);
		DefaultCaret caret = (DefaultCaret)taskOutput.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		timeLeft = new JLabel("00.0 minute(s)");

		JPanel panel = new JPanel();
		panel.add(startButton);
		panel.add(progressBar);
		panel.add(timeLeft);

		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/**
	 * Invoked when the user presses the start button.
	 */
	public void actionPerformed(ActionEvent evt) {
		if(startButton.getText() == "Start") {
			startButton.setText("Stop");
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			timeLeft.setText("10.0 hour(s)?");
			// Instances of javax.swing.SwingWorker are not reusuable, so
			// we create new instances as needed.
			if(task == null){
				task = new Task();
				task.addPropertyChangeListener(this);
			}
			task.execute();
		} else if (startButton.getText() == "Stop") {
			boolean canceled = task.cancel(true);
			if(canceled) {
				parent.dispatchEvent(new WindowEvent(parent, WindowEvent.WINDOW_CLOSING));
			}
		}
	}

	/**
	 * Invoked when task's progress property changes.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			if(progress == 100) {
				this.parent.dispose();
				if(this.exe != null)
					exe.start();
			}
			//taskOutput.append(String.format("Completed %d%% of task.\n", task.getProgress()));
		}
	}

	/**
	 * Create the GUI and show it. As with all GUI code, this must run on the
	 * event-dispatching thread.
	 */
	public static ProgressBar createAndShowGUI(final Thread exe, WeatherData data, boolean climate) {
		// Create and set up the window.
		final JFrame frame = new JFrame("ProgressBarDemo");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                frame.dispose();
                if(exe != null)
                	exe.start();
            }
        });
		
		// Create and set up the content pane.
		ProgressBar b = new ProgressBar(exe, data, frame, climate);
		JComponent newContentPane = b;
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
		
		return b;
	}
}
