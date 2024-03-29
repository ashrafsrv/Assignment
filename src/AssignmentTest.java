import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class AssignmentTest {
	
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	private static Assignment assignment = new Assignment();
	
	// This will make it a bit easier for us to make Date objects
	private static Date getDate(String s) {
		try {
			return df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("The test case is broken, invalid SimpleDateFormat parse");
		}
		// unreachable
		return null;
	}
	
	// helper method to compare two Submissions using assertions
	private static void testHelperEquals(Submission expected, Submission actual) {
		assertEquals(expected.getUnikey(), actual.getUnikey());
		assertEquals(expected.getTime(), actual.getTime());
		assertEquals(expected.getGrade(), actual.getGrade());
	}
		
	// helper method to compare two Submissions using assertions
	private static void testHelperEquals(String unikey, Date timestamp, Integer grade, Submission actual) {
		assertEquals(unikey, actual.getUnikey());
		assertEquals(timestamp, actual.getTime());
		assertEquals(grade, actual.getGrade());
	}
	
	// Helper method to build the trivial example submission history
	private SubmissionHistory buildTinyExample() {
		SubmissionHistory history = new Assignment();
		// submission A:
		history.add("mrah", getDate("2016/09/03 09:00:00"), 66);
		// submission B:
		history.add("nzeb", getDate("2016/09/03 16:00:00"), 86);
		// submission C:
		history.add("mrah", getDate("2016/09/03 16:00:00"), 73);
		// submission D:
		history.add("nzeb", getDate("2016/09/03 18:00:00"), 40);
		return history;
	}
	
	/*---------------Main test cases-----------------------------*/
	
	/*Testing illegal arguments*/
	
	@Test(expected = IllegalArgumentException.class)
	public void addUnikeyNull() {
		assignment.add(null, getDate("2016/09/03 09:00:00"), new Integer(80));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addTimestampNull() {
		assignment.add("mrah", null, new Integer(80));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addGradeNull(){
		assignment.add("mrah", getDate("2016/09/03 09:00:00"), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removeSubmissionNull() {
		assignment.remove(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getBestGradeNull() {
		assignment.getBestGrade(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getSubmissionFinalNull() {
		assignment.getSubmissionFinal(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getSubmissionBeforeUnikeyNull() {
		assignment.getSubmissionBefore(null, getDate("2016/09/03 09:00:00"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getSubmissionBeforeDateNull() {
		assignment.getSubmissionBefore("mrah", null);
	}
	
	/*Testing null return values*/
	@Test
	public void testBestGradeNull() {
		assertNull(assignment.getBestGrade("mrah"));
		
	}
	
	@Test
	public void testGetSubmissionFinalNull() {
		assertNull(assignment.getSubmissionFinal("mrah"));
	}
	
	@Test
	public void testGetSubmissionBeforeNull() {
		assertNull(assignment.getSubmissionBefore("mrah", getDate("2016/09/03 09:00:00")));
	}
	
	
	
	@Test
	public void testTopStudentsEmpty() {
		assertTrue(assignment.listTopStudents().isEmpty());
	}
	
	@Test
	public void testRegressionsEmpty() {
		assertTrue(assignment.listRegressions().isEmpty());
	}
}