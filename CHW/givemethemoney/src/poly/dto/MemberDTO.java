package poly.dto;

public class MemberDTO {
	private byte[] stored_cred;
	private int member_no;
	private String member_id, member_name, member_pw, member_email, member_addr1, member_addr2,
			member_auth, team_name, member_phone, member_team, member_approve;

	public byte[] getStored_cred() {
		return stored_cred;
	}
	public void setStored_cred(byte[] stored_cred) {
		this.stored_cred = stored_cred;
	}
	public String getTeam_name() {
		return team_name;
	}
	public String getMember_approve() {
		return member_approve;
	}
	public void setMember_approve(String member_approve) {
		this.member_approve = member_approve;
	}
	public String getMember_phone() {
		return member_phone;
	}
	public void setMember_phone(String member_phone) {
		this.member_phone = member_phone;
	}
	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}
	public String getMember_team() {
		return member_team;
	}
	public void setMember_team(String member_team) {
		this.member_team = member_team;
	}
	private int member_branch;
	public int getMember_no() {
		return member_no;
	}
	public void setMember_no(int member_no) {
		this.member_no = member_no;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getMember_pw() {
		return member_pw;
	}
	public int getMember_branch() {
		return member_branch;
	}
	public void setMember_branch(int member_branch) {
		this.member_branch = member_branch;
	}
	public void setMember_pw(String member_pw) {
		this.member_pw = member_pw;
	}
	public String getMember_email() {
		return member_email;
	}
	public void setMember_email(String member_email) {
		this.member_email = member_email;
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
	public String getMember_auth() {
		return member_auth;
	}
	public void setMember_auth(String member_auth) {
		this.member_auth = member_auth;
	}

}
