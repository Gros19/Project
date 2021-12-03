void setup() {

  Serial.begin(9600); //Set serial baud rate to 9600 bps

}

void loop() {
    int val;
    val = analogRead(0);  //Read rotation senseor value from analog 3
    Serial.println((int)(val/5.6833)); //Print the value to serial port
    // 270-> val/3.788
    // 180-> val/5.683
    delay(100);

}
