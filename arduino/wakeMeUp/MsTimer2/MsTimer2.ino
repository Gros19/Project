#include <MsTimer2.h>

#include<MsTimer2.h>
int led1 = 11;
int led2 = 12;

void flash(){
  static boolean output = HIGH;
  digitalWrite(led1, output);
  output = !output;
}
void setup() {
  MsTimer2::set(500, flash);  //500ms period
  MsTimer2::start();
}

void loop() {
  digitalWrite(led2, HIGH);
  delay(1000);
  digitalWrite(led2, LOW);
  delay(1000);
}
