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
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioInterruptEvent;
import com.pi4j.wiringpi.GpioUtil;

public class DataListener extends Thread {
    
	// keep track of state of received data
	private boolean receivingData;
	private int receivedSquares;
	private int[] boardSquareData;
	private int[][] boardRawData;
	private int[][] boardData;
	private DataController receiver;
	
	/**
	 * The data listener is constantly waiting for an interrupt on pin 0 (falling edge)
	 * when it gets interrupted it reads pin 1 - 8 and parses the data to a byte
	 */
	public DataListener() {
		 System.out.println("[DataListener] Booting up...");
		 boardRawData = new int[33][8];
		 boardData = new int[8][8];
		 boardSquareData = new int[64];
		 System.out.println("[DataListener] Initialized empty board, now ignoring first byte!!!..");
	}
	
	public void run() {
		
		// create and add GPIO listener 
        GpioInterrupt.addListener(new GpioInterruptListener() {
            @Override
            public void pinStateChange(GpioInterruptEvent event) {
            	if (!event.getState()) {
            		boardRawData[receivedSquares][0] = Gpio.digitalRead(0);
            		boardRawData[receivedSquares][1] = Gpio.digitalRead(1);
            		boardRawData[receivedSquares][2] = Gpio.digitalRead(2);
            		boardRawData[receivedSquares][3] = Gpio.digitalRead(3);
            		boardRawData[receivedSquares][4] = Gpio.digitalRead(4);
            		boardRawData[receivedSquares][5] = Gpio.digitalRead(5);
            		boardRawData[receivedSquares][6] = Gpio.digitalRead(6);
            		boardRawData[receivedSquares][7] = Gpio.digitalRead(8);
            		receivedSquares++;
            		
            		if (receivedSquares == 33) {
            			receivedSquares = 0;
            			System.out.println("[DataListener] Done receiving the raw data creating matrix..");
            			createMatrix();
            		}
            	}
            }
        });
		
        // setup wiring pi
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println("[Error] while setting up wiringpi");
            return;
        }

        // set the edge state on the pins we will be listening for
        GpioUtil.setEdgeDetection(7, GpioUtil.EDGE_BOTH);
      
        // configure GPIO 7 as an INPUT pin; enable it for callbacks
        Gpio.pinMode(7, Gpio.INPUT);
        Gpio.pullUpDnControl(7, Gpio.PUD_UP);        
        GpioInterrupt.enablePinStateChangeCallback(7);
        
        //########################################################
        //set all the other pins to input pins
        Gpio.pinMode(0, Gpio.INPUT);
        Gpio.pinMode(1, Gpio.INPUT);
        Gpio.pinMode(2, Gpio.INPUT);
        Gpio.pinMode(3, Gpio.INPUT);
        Gpio.pinMode(4, Gpio.INPUT);
        Gpio.pinMode(5, Gpio.INPUT);
        Gpio.pinMode(6, Gpio.INPUT);
        Gpio.pinMode(8, Gpio.INPUT);

        System.out.println("[DataListener] booted up succesfully..");
        
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
	
	/**
	 * Parses the data from individual ints to bytes by first appending the ints
	 * and after that casting it to byte with parse int radix 2
	 */
	private void readData() {
		
		//receiver.addMessage("[Interrupt] Clock is falling going to handle the data. \n");
		//receiver.addMessage("[DataListener] Pin values: " + Gpio.digitalRead(0) + Gpio.digitalRead(1) + Gpio.digitalRead(2) + Gpio.digitalRead(3) + Gpio.digitalRead(4) + Gpio.digitalRead(5) + Gpio.digitalRead(6) + Gpio.digitalRead(8) + "\n");
		//System.out.println("[DataListener] Receiving square: " + receivedSquares + " + 2");
		// parse the first square 
		//String firstBits = "0000" + Gpio.digitalRead(0) + Gpio.digitalRead(1) + Gpio.digitalRead(2) + Gpio.digitalRead(3);
		//int firstInt = Integer.parseInt(firstBits, 2);
		
		// save the data
		boardRawData[receivedSquares][0] = Gpio.digitalRead(0);
		boardRawData[receivedSquares][1] = Gpio.digitalRead(1);
		boardRawData[receivedSquares][2] = Gpio.digitalRead(2);
		boardRawData[receivedSquares][3] = Gpio.digitalRead(3);
		boardRawData[receivedSquares][4] = Gpio.digitalRead(4);
		boardRawData[receivedSquares][5] = Gpio.digitalRead(5);
		boardRawData[receivedSquares][6] = Gpio.digitalRead(6);
		boardRawData[receivedSquares][7] = Gpio.digitalRead(8);
		receivedSquares++;
		//System.out.println(receivedSquares);
		
		/*if (!receivingData) {
			receivingData = true;
			//System.out.println("[DataListener] Start receiving data..");
			receivedSquares = 0;
		}*/
		
		//boardRawData[receivedSquares] = firstInt;
		//receivedSquares++;
		
		// parse the second square
		//String secondBits = "0000" + Gpio.digitalRead(4) + Gpio.digitalRead(5) + Gpio.digitalRead(6) + Gpio.digitalRead(8);
		//int secondInt = Integer.parseInt(secondBits, 2);
		
		//boardRawData[receivedSquares] = secondInt;
		//receivedSquares++;
		
		//System.out.println("[DataListener] First square has value: " + firstInt + " Second square has value: " + secondInt);
		
		if (receivedSquares == 33) {
			receivedSquares = 0;
			System.out.println("[DataListener] Done receiving the raw data creating matrix..");
			createMatrix();
		}
	}
	
	/**
	 * Creates from the raw data square data
	 */
	private void createSquareData() {
		int square = 0;
		for (int b = 1; b < 33; b++) {
			String firstBits = "" + boardRawData[b][0] + boardRawData[b][1] + boardRawData[b][2] + boardRawData[b][3];
			String secondBits = "" + boardRawData[b][4] + boardRawData[b][5] + boardRawData[b][6] + boardRawData[b][7];
			int firstInt = Integer.parseInt(firstBits, 2);
			int secondInt = Integer.parseInt(secondBits, 2);
			
			System.out.println("Square: " + b + " en + 1 :" + firstBits + secondBits);
			
			boardSquareData[square] = firstInt;
			square++;
			boardSquareData[square] = secondInt;
			square++;
		}
	}
	
	/**
	 * Creates a two-dimensional overview of the board from a one-dimensional array
	 */
	private void createMatrix() {
		createSquareData();
		int square = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				boardData[i][j] = boardSquareData[square];
				square++;
			}
		}
		
		dataReceived(boardData);
	}
	
	/**
	 * Registers the datacontroller as a listener on the data listener
	 * @param controller , the instantiated datacontroller
	 */
	public void register(DataController controller) {
		receiver = controller;
	}
	
	/**
	 * Called when the complete data about a board has been received
	 * @param boardData contains the boarddata in int[][]
	 */
    private void dataReceived(int[][] boardData){
    	System.out.println("Trying to call dataReceived()");
    	receiver.onDataReceived(boardData);
    }
    
    /** 
     * Sample information that simulates data received by the listener
     */
    public void sampleData(int[][] boardData) {
    	receiver.onDataReceived(boardData);
    }
}

