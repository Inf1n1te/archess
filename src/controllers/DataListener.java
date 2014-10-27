package controllers;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterrupt;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioUtil;

public class DataListener {
    
	public DataListener() {
		 System.out.println("[DataListener] Booting up...");
	
        // create and add GPIO listener 
        GpioInterrupt.addListener(new GpioInterruptListener() {
            @Override
            public void pinStateChange(GpioInterruptEvent event) {
                	if (event.getState()) {
				
			} else {	
				System.out.println("[Interrupt] Clock is falling going to handle the data.");
				System.out.println("Pin 1: " + Gpio.digitalRead(1));
				System.out.println("Pin 2: " + Gpio.digitalRead(2));
				System.out.println("Pin 3: " + Gpio.digitalRead(3));			
				System.out.println("Pin 4: " + Gpio.digitalRead(4));
				System.out.println("Pin 5: " + Gpio.digitalRead(5));
				System.out.println("Pin 6: " + Gpio.digitalRead(6));
				System.out.println("Pin 7: " + Gpio.digitalRead(7));
				System.out.println("Pin 8: " + Gpio.digitalRead(8));
			}
		}
        });
        
        // setup wiring pi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println("[Error] while setting up wiringpi");
            return;
        }

        // set the edge state on the pins we will be listening for
        GpioUtil.setEdgeDetection(0, GpioUtil.EDGE_BOTH);

        // configure GPIO 0 as an INPUT pin; enable it for callbacks
        Gpio.pinMode(0, Gpio.OUTPUT);
        Gpio.pullUpDnControl(0, Gpio.PUD_DOWN);        
        GpioInterrupt.enablePinStateChangeCallback(0);
        
        // continuously loop to prevent program from exiting
        for (;;) {
            try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
}

