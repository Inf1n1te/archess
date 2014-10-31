package controllers;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterrupt;
import com.pi4j.wiringpi.GpioInterruptCallback;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioUtil;

public class FasterListener extends Thread {
	
	private int x;
	
	public FasterListener() {
		System.out.println("[Faster] Starting fasterlistener [5]..");
		x =0;
	    // setup wiring pi
	    if (Gpio.wiringPiSetup() == -1) {
	        System.out.println("[Error] while setting up wiringpi");
	        return;
	    }
	    
	    GpioInterrupt.addListener(new GpioInterruptListener() {
	    	@Override
	    	public void pinStateChange(GpioInterruptEvent event) {
	    		System.out.println("pin state change");
	    	}
	    });
	    
	    GpioUtil.setEdgeDetection(7, GpioUtil.EDGE_FALLING);
	    // configure GPIO 7 as an INPUT pin; enable it for callbacks
	    Gpio.pinMode(7, Gpio.INPUT);
	    Gpio.pullUpDnControl(7, Gpio.PUD_OFF);
	    GpioInterrupt.enablePinStateChangeCallback(7);
        
		System.out.println("Done initializing");
        
        // continuously loop to prevent program from exiting
        for (;;) {
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}
