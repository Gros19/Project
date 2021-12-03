#include <MsTimer2.h>
//식물 생장용 조명을 낮에는 켜두고 밤에는 끄는 기능
  boolean pattern[] = {LOW, LOW, HIGH, HIGH, HIGH};
  short n = 0;
  int outpin = 8;
  
void flash() {
  //Serial.println("#####interupt#######");
  n++;
  if (n>4){ n=0;}
}

void setup() {

  Serial.begin(9600);
  Serial.println("start");
  pinMode(outpin, OUTPUT);
  MsTimer2::set(14400000, flash); //14400000ms == 4시간마다 인러터럽트
  MsTimer2::start();
}

void loop() {
  digitalWrite(outpin, pattern[n]);
  delay(3600000); //1시간 delay
}
