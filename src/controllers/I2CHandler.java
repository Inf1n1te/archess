package controllers;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.Gpio;

public class I2CHandler extends Thread {
	
	private I2CDevice[] dataLines;
	
	public I2CHandler () throws IOException {
		System.out.println("i2cSquared loading!");
	}

	/**
	 * Gets called when thread get started
	 */
	public void run() {
		try {
			init();
		} catch (IOException e) {
			System.out.println("[Error] Initializing addresses");
		} 
		// after it has initialized
	}
	
	/**
	 * Initializes the bus and data addresses
	 * @throws IOException of it couldn't instantiate the bus or
	 * read the devices
	 */
	private void init() throws IOException {
		final I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
		
		dataLines = new I2CDevice[32];
		
		// initialize the data lines
		for (int i = 0; i < 32; i++) {
			dataLines[i] = bus.getDevice(i + 1);
		}
		
		Gpio.pullUpDnControl(8, Gpio.PUD_UP);     
		Gpio.pullUpDnControl(9, Gpio.PUD_UP);  
	}
	
	/**
	 * Reads the data from the 32 selected adress lines
	 * @return returns the read data
	 * @throws IOException
	 */
	private int[] readData() throws IOException {
		int[] data = new int[32];
		for (int i = 0; i < 32; i++) {
			data[i] = dataLines[i].read();
		}
		return data;
	}
	
	
}
