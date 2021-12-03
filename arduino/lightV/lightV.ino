
void setup() {
}

void loop() {
  for(int light=255; light > 0; light--){
    analogWrite(3, light);
    analogWrite(5, light);
    analogWrite(6, light);
    analogWrite(9, light);
    analogWrite(11, light);
    delay(50);
  }
}
