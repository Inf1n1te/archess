package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import model.Board;
import model.Move;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Suite;

import utils.Piece;
import utils.TempPiece;

//@RunWith(Suite.class)
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
// TODO FIX IT!
	private final boolean methodTempFieldEquals(TempPiece tP, Piece p) {
		boolean value = false;
		try {
			Method testMethod = Move.class.getDeclaredMethod("tempFieldEquals",
					new Class[] { TempPiece.class, Piece.class });
			testMethod.setAccessible(true);
			value = (boolean) testMethod.invoke(Move.class.newInstance(), new Object[] { tP, p });
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | InstantiationException e) {
			System.err
					.println("Testing error, try failed @MoveTest.methodTempFieldEquals");
			e.printStackTrace();
		}
		return value;
	}

	@Test
	public final void testTempFieldEquals() {
		// Testing true values
//		assertEquals(true, methodTempFieldEquals(TempPiece.EMPTY, null));
		for (int i = 0; i < Piece.values().length; i++) {
			if (Piece.values()[i].toString().contains("WHITE")) {
				assertEquals(
						true,
						methodTempFieldEquals(TempPiece.WHITE,
								Piece.values()[i]));
			} else if (Piece.values()[i].toString().contains("BLACK")) {
				assertEquals(
						true,
						methodTempFieldEquals(TempPiece.BLACK,
								Piece.values()[i]));
			} else {
				System.err
						.println("Error with toString().contains() @MoveTest.testTempFieldEquals");
			}
		}

		// Testing false values
		for (int i = 0; i < Piece.values().length; i++) {
			if (Piece.values()[i].toString().contains("BLACK")) {
				assertEquals(
						false,
						methodTempFieldEquals(TempPiece.WHITE,
								Piece.values()[i]));
			} else if (Piece.values()[i].toString().contains("WHITE")) {
				assertEquals(
						false,
						methodTempFieldEquals(TempPiece.BLACK,
								Piece.values()[i]));
			} else {
				System.err
						.println("Error with toString().contains() @MoveTest.testTempFieldEquals");
			}
			assertEquals(false,
					methodTempFieldEquals(TempPiece.EMPTY, Piece.values()[i]));
		}

	}

	@Ignore
	@Test
	public final void testMove() {
		fail("Not yet implemented"); // TODO
	}

	@Ignore
	@Test
	public final void testIsValid() {
		fail("Not yet implemented"); // TODO
	}

}
