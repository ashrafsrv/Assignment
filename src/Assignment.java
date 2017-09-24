import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class Assignment implements SubmissionHistory {
	
	// class for submission objects
	private class StudentSubmission implements Submission, Comparable<Submission>{
		private String unikey;
		private Date submission_date;
		private Integer grade;
		
		public StudentSubmission(String unikey, Date date, Integer grade){
			this.unikey = unikey;
			this.submission_date = date;
			this.grade = grade;
		}
		
		public String getUnikey() {
			return this.unikey;
		}
		
		public void setUnikey(String unikey) {
			this.unikey = unikey;
		}
		
		public Date getTime() {
			return this.submission_date;
		}
		
		public void setTime(Date date) {
			this.submission_date = date;
		}
		
		public Integer getGrade(){
			return this.grade;
		} 
		
		public void setGrade(Integer grade) {
			this.grade = grade;
		}
		
		public int compareTo(Submission other_submission) {
			if (this.getGrade().compareTo(other_submission.getGrade()) < 0) {
				return 1;
			}
			else if(this.getGrade().compareTo(other_submission.getGrade()) > 0) {
				return -1;
			}
			else {
				return 0;
			}
		}
		
	}
	
	//Data structure for assignment
	TreeMap<String, TreeMap<Date, Submission>> all_submissions;
	
	private static Integer highest_grade; //For an assignment
	
	/**
	 * Default constructor
	 */
	public Assignment() {
		// TODO initialise your data structures
		this.all_submissions = new TreeMap<>();
		this.highest_grade = null;
	}

	@Override
	public Integer getBestGrade(String unikey) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null) {
			throw new IllegalArgumentException();
		}
		
		if(this.all_submissions.containsKey(unikey)) {
			
			PriorityQueue<Submission> submissions_list = new PriorityQueue<Submission>(this.all_submissions.get(unikey).values());
			
			if(submissions_list.size() == 0) {
				return null;
			}
			
			return submissions_list.peek().getGrade();
		}
		
		return null;
		
	}

	@Override
	public Submission getSubmissionFinal(String unikey) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null) {
			throw new IllegalArgumentException();
		}
		
		if(this.all_submissions.containsKey(unikey)) {
			TreeMap<Date, Submission> inner_treemap = this.all_submissions.get(unikey);
			
			if(inner_treemap.size() == 0) {
				return null;
			}
			
			Date latest_timestamp = inner_treemap.lastKey();
			
			if(latest_timestamp == null) {
				return null;
			}
			
			Submission latest_submission = inner_treemap.get(latest_timestamp);
			return latest_submission;
		}
		
		return null;	
	}

	@Override
	public Submission getSubmissionBefore(String unikey, Date deadline) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null || deadline == null) {
			throw new IllegalArgumentException();
		}
		
		if(this.all_submissions.containsKey(unikey)) {
			TreeMap<Date, Submission> inner_treemap = this.all_submissions.get(unikey);
			
			if(inner_treemap.size() == 0) {
				return null;
			}
			
			Date latest_timestamp = inner_treemap.floorKey(deadline);
			
			if(latest_timestamp == null) {
				return null;
			}
			
			Submission latest_submission = inner_treemap.get(latest_timestamp);
			return latest_submission;
		}
		
		return null;
	}

	@Override
	public Submission add(String unikey, Date timestamp, Integer grade) {
		// TODO Implement this, ideally in better than O(n)
		if(unikey == null || timestamp == null || grade == null) {
			throw new IllegalArgumentException();
		}
		Submission new_submission = (Submission) new StudentSubmission(unikey, timestamp, grade);
		
		if(this.highest_grade == null) {
			this.set_highest_grade(grade);
		}
		else {
			if(this.highest_grade.compareTo(grade) < 0) {
				this.set_highest_grade(grade);
			}
		}
		
		if(this.all_submissions.containsKey(unikey)) {
			(this.all_submissions.get(unikey)).put(timestamp, new_submission);
		}
		else {
			this.all_submissions.put(unikey, new TreeMap<Date, Submission>());
			(this.all_submissions.get(unikey)).put(timestamp, new_submission);
			
		}
		return new_submission;
	}

	@Override
	public void remove(Submission submission) {
		// TODO Implement this, ideally in better than O(n)
		if(submission == null) {
			throw new IllegalArgumentException();
		}
		
		Date unique_submission_time = submission.getTime();
		String unikey = submission.getUnikey();
		
		if(this.all_submissions.get(unikey).containsValue(submission)) {
			this.all_submissions.get(unikey).remove(unique_submission_time);
		}
	}

	@Override
	public List<String> listTopStudents() {
		// TODO Implement this, ideally in better than O(n)
		// (you may ignore the length of the list in the analysis)
		List<String> all_students = new ArrayList<String>(this.all_submissions.keySet());
		ArrayList<String> top_students = new ArrayList<String>();
		
		if(all_students.size() == 0) {
			return top_students;
		}
		
		for(int i = 0; i < all_students.size(); i++) {
			if(this.getBestGrade(all_students.get(i)) == this.highest_grade) {
				top_students.add(all_students.get(i));
			}
		}
		
		return top_students;
	}

	@Override
	public List<String> listRegressions() {
		// TODO Implement this, ideally in better than O(n^2)
		List<String> all_students = new ArrayList<String>(this.all_submissions.keySet());
		ArrayList<String> reg_students = new ArrayList<String>();
		
		if(all_students.size() == 0) {
			return reg_students;
		}
		
		for(int i = 0; i < all_students.size(); i++) {
			if(this.getSubmissionFinal(all_students.get(i)).getGrade() < this.getBestGrade(all_students.get(i))) {
				reg_students.add(all_students.get(i));
			}
		}
		return reg_students;
	}
	
	
	/**/
	public void set_highest_grade(Integer grade) {
		this.highest_grade = grade;
	}
}
