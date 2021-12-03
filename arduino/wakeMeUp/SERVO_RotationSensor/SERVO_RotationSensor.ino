#include<Servo.h>   //서보 라이브러리 가져옴
Servo servo;        //서보객체 생성 servo
int servoPin = 9;   //서보 9핀쓸거야
int angle = 160;    //각도는 처음 0으로 
int SoundSensor = A0;

void setup() {
  Serial.begin(9600);
  pinMode(SoundSensor, INPUT);
  servo.attach(servoPin); //servo 9번핀에서 
}

void loop() {
  int data = analogRead(SoundSensor); 
  Serial.println(data);
  delay(100);
//  servo.write(angle);   //서보에 
//  delay(500);

}
