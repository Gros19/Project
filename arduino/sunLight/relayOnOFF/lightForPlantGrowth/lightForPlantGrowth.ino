#include <MsTimer2.h>
//식물 생장용 조명을 낮에는 켜두고 밤에는 끄는 기능
  boolean pattern[] = {LOW, LOW, HIGH, HIGH, HIGH, HIGH};
  /*
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
  
void flash() {
  Serial.println("#####interupt#######");
  n++;
  if (n>5){ n=0;}
}

void setup() {

  Serial.begin(9600);
  Serial.println("start");
  pinMode(outpin, OUTPUT);
  //MsTimer2::set(3000, flash); //14400000ms == 3s마다 인러터럽트
  /*
   * MsTimer2::set(14400000, flash); //14400000ms == 4시간마다 인러터럽트
   */
  MsTimer2::start();
}

void loop() {
//  Serial.print(n);
//  Serial.print("번째 배열 ");
//  Serial.print("[");
//  Serial.print(pattern[n]);
//  Serial.print("]");
  digitalWrite(outpin, pattern[n]);
  delay(1000); //
}
