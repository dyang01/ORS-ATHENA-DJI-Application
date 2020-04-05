void setup() {
  Serial.begin(115200);  //initial the Serial

}

void loop() {
  
    int sensorValue = analogRead(A0);
    float voltage = sensorValue * (5.0/1023.0);
    Serial.println(voltage);   //print line feed character
}
