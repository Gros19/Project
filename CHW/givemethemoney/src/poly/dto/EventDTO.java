package poly.dto;

public class EventDTO {

	private String title;
	private String start;
	private String end;
	private String days;
	private String until;

	private String need_staff;
	private String now_staff;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getUntil() {
		return until;
	}

	public void setUntil(String until) {
		this.until = until;
	}

	public String getNeed_staff() {
		return need_staff;
	}

	public void setNeed_staff(String need_staff) {
		this.need_staff = need_staff;
	}

	public String getNow_staff() {
		return now_staff;
	}

	public void setNow_staff(String now_staff) {
		this.now_staff = now_staff;
	}

}
