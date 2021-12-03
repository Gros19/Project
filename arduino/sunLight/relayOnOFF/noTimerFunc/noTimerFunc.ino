 
  /*
   * 식물 생장용 조명을 낮에는 켜두고 밤에는 끄는 코드
   * ex 오후 10시 시작
   * pm 10 ~ am 2 off
   * am 2 ~ am 6  off
   * am 6 ~ am 10 on
   * am 10 ~ pm 2 on
   * pm 2 ~ pm 6  on
   * pm 6 ~ pm 10 on
   *
   */
 
  short n = 0;
  int outpin = 8;
  unsigned long time_previous, time_current;
  boolean pattern[] = {LOW, LOW, HIGH, HIGH, HIGH, HIGH};
  
void setup() {
  Serial.begin(9600);
  Serial.println("start");
  pinMode(outpin, OUTPUT);
  time_previous = millis(); // 시작시간
}

void loop() {
    
    time_current = millis(); //현재 시간

    if (time_current - time_previous > 14400000)  //14400000 == 4시간
    {
      time_previous = millis();
      n++;
      if (n>5){ n=0;}
    }

    digitalWrite(outpin, pattern[n]);
    delay(60000);
}
