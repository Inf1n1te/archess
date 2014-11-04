package controllers;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.wiringpi.Gpio;

public class I2CHandler {
	
	public I2CHandler () throws IOException {
		 System.out.println("I2C Reader [Test]");
	     final I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
	    
	     System.out.println("Get device with id [0x06]");
	     I2CDevice fpga = bus.getDevice(0x06);
	     System.out.println(fpga.toString());
	     
	     System.out.println("Setting pins to up");
	     // pins
	     Gpio.pullUpDnControl(8, Gpio.PUD_UP);     
	     Gpio.pullUpDnControl(9, Gpio.PUD_UP);   
	     
	     System.out.println("Reading data");
	     int x = fpga.read();
	     System.out.println(x);
 }
}
