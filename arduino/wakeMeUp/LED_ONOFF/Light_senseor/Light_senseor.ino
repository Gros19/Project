int cds = A0; //변수 cds를 선언하고, A0에 대입
void setup() {
  Serial.begin(9600); //시리얼 통신을 시작하며, 속도는 9600으로 설정
  
  pinMode(13, OUTPUT);  //내장 led를 13번으로 제어

  
}

void loop() {
  int val = analogRead(cds);  //변수 val을 선언, cds(A0) 아날로그 값을 저장
  if(val > 950){
    digitalWrite(13, HIGH);
  }
  Serial.println(val);        //변수 val에 저장된 값을 시리얼 통신을 통해 출력
  delay(1000); // 1초동안 딜레이
  digitalWrite(13,LOW);

}
