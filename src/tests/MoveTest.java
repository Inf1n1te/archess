package tests;

import static org.junit.Assert.*;

import model.Board;
import model.Move;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.TestMethod;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import utils.Piece;
import utils.TempPiece;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(Suite.class)
public class MoveTest {

	Board board;
	private static int[][] rawInitBoard;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		rawInitBoard = new int[][] { new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 },
				new int[] { 1, 1, 0, 0, 0, 0, 2, 2 } };
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		rawInitBoard = null;
	}

	@Before
	public void setUp() throws Exception {
		board = new Board(rawInitBoard);
	}

	@After
	public void tearDown() throws Exception {
		board = null;
	}

	@Test
	public final void testTempFieldEquals() {
		try {
			Method testMethod = Move.class.getDeclaredMethod("tempFieldEquals",
					new Class[] { Piece.class, TempPiece.class });
			testMethod.setAccessible(true);
			testMethod.invoke(null, new Object[] { TempPiece.EMPTY, null });
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
