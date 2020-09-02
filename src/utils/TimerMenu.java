package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import main.Main;

public class TimerMenu extends JButton
{
	public static final String TIMER_KEY = "TIMER";

	private Timer timer;
	private long lastTickTime;
   	private long runningTime;
	private boolean running;

	public TimerMenu()
	{
		loadPrefs();
    	lastTickTime = System.currentTimeMillis() - runningTime;
		setTime();
		running = false;

        timer = new Timer(100, e -> setTime());	
        
        addActionListener(e -> {
        	running = !running;
        	if( running )
        	{
        		lastTickTime = System.currentTimeMillis() - runningTime;
        		timer.restart();
        	}
        	else
        	{
        		timer.stop();
        	}
        });
	}
	
	public void setTime()
	{
        runningTime = System.currentTimeMillis() - lastTickTime;
        Duration duration = Duration.ofMillis(runningTime);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long millis = duration.toMillis();
        long seconds = millis / 1000;
        millis -= (seconds * 1000);
        millis /= 100;
        setText(String.format("%04d:%02d:%02d.%1d", hours, minutes, seconds, millis));
	}
	
	public void reset()
	{
		int option = JOptionPane.showConfirmDialog(null, "Are you sure to reset timer?");
		if( option == JOptionPane.YES_OPTION )
		{
			runningTime = 0;
			lastTickTime = System.currentTimeMillis();
			setTime();
			repaint();
		}
	}
	
	public void loadPrefs()
	{
		Preferences prefs = Preferences.userRoot().node(Main.rootNodeName);
		runningTime = prefs.getLong(TIMER_KEY, 0);
	}
	
	public void savePrefs()
	{
		Preferences prefs = Preferences.userRoot().node(Main.rootNodeName);
		prefs.putLong(TIMER_KEY, runningTime);
	}
}
