package tests;

import static org.junit.Assert.*;

import model.Board;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import utils.TempPiece;

import java.lang.reflect.Method;

@RunWith(Suite.class)
public class MoveTest {
	
	Board board;
	TempPiece[][] initTempBoard;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		initTempBoard;
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
//		board = new Board();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testMove() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testIsValid() {
		fail("Not yet implemented"); // TODO
	}

}
