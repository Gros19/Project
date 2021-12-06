package poly.dto;

public class CHWDTO {

	private int member_no; // 멤버 번호
	private String member_name; // 멤버이름 이름
	private String member_addr1; // 멤버 주소
	private String member_addr2; // 멤버 상세주소
	private String member_pw; //멤버 비밀번호
	private String member_phone; //멤버 전화번호
	
	
	
	public void setMember_no(int member_no) {
		this.member_no = member_no;
	}
	public int getMember_no() {
		return member_no;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getMember_addr1() {
		return member_addr1;
	}
	public void setMember_addr1(String member_addr1) {
		this.member_addr1 = member_addr1;
	}
	public String getMember_addr2() {
		return member_addr2;
	}
	public void setMember_addr2(String member_addr2) {
		this.member_addr2 = member_addr2;
	}
	public String getMember_pw() {
		return member_pw;
	}
	public void setMember_pw(String member_pw) {
		this.member_pw = member_pw;
	}
	public String getMember_phone() {
		return member_phone;
	}
	public void setMember_phone(String member_phone) {
		this.member_phone = member_phone;
	}
	
}